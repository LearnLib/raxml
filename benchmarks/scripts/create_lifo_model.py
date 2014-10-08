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
      <location name="s{endState}"/>
"""

end_loc="""\
      <location name="me"/>
   </locations>
"""


begin_trans="""\
   <transitions>
      <transition from="s0" to="ms" symbol="IGet" />
      <transition from="ms" to="s0" symbol="ONOK" />
"""

template_trans="""\
      <transition from="s{startState}" to="m{startState}_{endState}" symbol="IPut" params="p">
         <assignments>
            <assign to="r{startState}">p</assign>
         </assignments>
      </transition>
      <transition from="m{startState}_{endState}" to="s{endState}" symbol="OOK" />
      <transition from="m{endState}_{startState}" to="s{startState}" symbol="OGet" params="r{startState}"/>
      <transition from="s{endState}" to="m{endState}_{startState}" symbol="IGet" />
"""

end_trans="""\
      <transition from="s2" to="me" symbol="IPut" params="p"/>
      <transition from="me" to="s2" symbol="ONOK" />
   </transitions>
""".replace("2",str(stacksize))



mid_loc=""
for i in range(0,stacksize):
    mid_loc=mid_loc+template_loc.format(startState=i,endState=i+1)

mid_trans=""
for i in range(0,stacksize):
    mid_trans=mid_trans+template_trans.format(startState=i,endState=i+1)

mid_vardecls=""
for i in range(0,stacksize):
    mid_vardecls=mid_vardecls+vardecl_template.format(startState=i,endState=i+1)

str=begin_str + begin_vardecls + mid_vardecls + end_vardecls +  \
      begin_loc + mid_loc + end_loc + begin_trans + mid_trans + end_trans + end_str


f=open(model_file_out,'w')
f.write(str)
f.close()
