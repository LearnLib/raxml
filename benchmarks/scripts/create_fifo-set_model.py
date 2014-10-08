#!/usr/bin/env python

import os,sys,re

narg=len(sys.argv)
if narg < 2 or narg > 2 :
  print("usage: " + sys.argv[0] + " size")
  sys.exit(0)

stacksize=int(sys.argv[1])
model_file_out="model.xml"


begin_str="""\
<?xml version="1.0" encoding="UTF-8" ?>
<register-automaton>
   <alphabet>
      <inputs>
         <symbol name="IGet"/>
         <symbol name="IPut"> 
            <param type="int" name="p0"/> 
         </symbol>
      </inputs>
      <outputs>
         <symbol name="OGet"> 
            <param type="int" name="p0"/> 
         </symbol>
         <symbol name="ONOK"/>
         <symbol name="OOK"/>
      </outputs>
   </alphabet>
   <constants>
   </constants>
"""

end_str="""\
</register-automaton>
"""

begin_vardecls="""\
   <globals>
"""
vardecl_template="""\
      <variable type="int" name="r{startState}">0</variable>
"""
end_vardecls="""\
      <variable type="int" name="out">0</variable>
   </globals>
"""



begin_loc="""\
   <locations>
      <location name="ms"/>
      <location name="s0" initial="true"/>
"""

template_loc="""\
      <location name="m{startState}_{endState}"/>
      <location name="m{endState}_{startState}"/>
      <location name="m{endState}_{endState}"/>
      <location name="s{endState}"/>
"""

end_loc="""\
   </locations>
"""


begin_trans="""\
   <transitions>
      <transition from="s0" to="ms" symbol="IGet" />
      <transition from="ms" to="s0" symbol="ONOK" />
"""

template_assignment="""\
            <assign to="{Variable}">{Value}</assign>
"""

template_guard="""\
         <guard>{TransitionGuardMid}</guard>
"""

mid_guard="""\
{Variable}{Equals}p"""

template_trans="""\
      <transition from="s{startState}" to="m{startState}_{endState}" symbol="IPut" params="p">
{InsertTransitionGuard}         <assignments>
            <assign to="r{startState}">p</assign>
         </assignments>
      </transition>
      <transition from="m{startState}_{endState}" to="s{endState}" symbol="OOK" />                                                               
      <transition from="m{endState}_{endState}" to="s{endState}" symbol="ONOK" />
      <transition from="s{endState}" to="m{endState}_{endState}" symbol="IPut" params="p">
{NoInsertTransitionGuard}      </transition>      
      <transition from="m{endState}_{startState}" to="s{startState}" symbol="OGet" params="out">
         <assignments>
{TransitionAssignments}         </assignments>
      </transition>
      <transition from="s{endState}" to="m{endState}_{startState}" symbol="IGet" />
"""

end_trans="""\
   </transitions>
""".replace("2",str(stacksize))



mid_loc=""
for i in range(0,stacksize):
    mid_loc=mid_loc+template_loc.format(startState=i,endState=i+1)

mid_trans=""
for i in range(0,stacksize):
    insertguardmid=""
    for x in range(0,i):
        insertguardmid=insertguardmid+mid_guard.format(Variable="r"+str(x),Equals="!=")
        if x+1<i:
            insertguardmid=insertguardmid+" &amp;&amp; "   
    insertguard=template_guard.format(TransitionGuardMid=insertguardmid)        
    noinsertguardmid=""
    for x in range(0,i+1):
        noinsertguardmid=noinsertguardmid+mid_guard.format(Variable="r"+str(x),Equals="==")
        if x<i:
            noinsertguardmid=noinsertguardmid+" || "
    if i<(stacksize-1):
        noinsertguard=template_guard.format(TransitionGuardMid=noinsertguardmid)
    else:
        noinsertguard=""
    shiftupdate=template_assignment.format(Variable="out",Value="r0")
    #shiftupdate="out=r0;"
    for x in range(0,i):
        shiftupdate=shiftupdate+template_assignment.format(Variable="r"+str(x),Value="r"+str(x+1))
        #shiftupdate=shiftupdate+"r{0}=r{1};".format(x,x+1)
    mid_trans=mid_trans+template_trans.format(startState=i,endState=i+1,InsertTransitionGuard=insertguard,NoInsertTransitionGuard=noinsertguard,TransitionAssignments=shiftupdate)

mid_vardecls=""
for i in range(0,stacksize):
    mid_vardecls=mid_vardecls+vardecl_template.format(startState=i,endState=i+1)

str=begin_str + begin_vardecls + mid_vardecls + end_vardecls +  \
      begin_loc + mid_loc + end_loc + begin_trans + mid_trans + end_trans + end_str


f=open(model_file_out,'w')
f.write(str)
f.close()
