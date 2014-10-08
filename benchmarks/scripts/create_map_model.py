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
         <symbol name="IGet">
            <param type="int" name="p0"/>
         </symbol>   
         <symbol name="IPut"> 
            <param type="int" name="p0"/>
            <param type="int" name="p1"/> 
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
      <variable type="int" name="key{startState}">0</variable>
      <variable type="int" name="val{startState}">0</variable>
"""
end_vardecls="""\
   </globals>
"""



begin_loc="""\
   <locations>      
      <location name="s0" initial="true"/>
      <location name="ms"/>
      <location name="m0_put_key0"/>
"""


end_loc="""\
   </locations>
"""


begin_trans="""\
   <transitions>
      <transition from="s0" to="ms" symbol="IGet" params="p0"/>
      <transition from="ms" to="s0" symbol="ONOK" />
      <transition from="s0" to="m0_put_key0" symbol="IPut" params="p0,p1">
         <assignments>
            <assign to="key0">p0</assign>
            <assign to="val0">p1</assign>
         </assignments>             
      </transition>          
      <transition from="m0_put_key0" to="s1" symbol="OOK" />      
"""



end_trans="""\
   </transitions>
"""


def generate_neq(max_key_index):
    guards=[]
    for i in range(max_key_index+1):
       guards.append("p0!=key"+str(i))
    return " &amp;&amp; ".join(guards)
    
def generate_states_and_transitions(size):
    locations=[]
    trans=[]
    for state in range(1,size+1):
        (l,t)=generate_state_and_trans(state)   
        locations=locations+l
        trans=trans+t
    return (locations,trans)     
      
def generate_state_and_trans(state):
    locations=[]
    trans=[]
    locations.append('<location name="s{0}"/>'.format(state) )
    for key in range(0,state):
        result=generate_get_key(state,key)
        locations.append(result[0])
        trans.append(result[1][0])
        trans.append(result[1][1])
    
    result=generate_get_else(state) 
    locations.append(result[0])
    trans.append(result[1][0])
    trans.append(result[1][1]) 
    
    for key in range(0,state):
        result=generate_put_key(state,key)
        locations.append(result[0])
        trans.append(result[1][0])
        trans.append(result[1][1])    
     
    if state == stacksize:
       result=generate_put_else(state) 
    else:
       result=generate_put_fresh(state)
    locations.append(result[0])
    trans.append(result[1][0])
    trans.append(result[1][1])         
  
    return (locations,trans)
    
def generate_get_key(state_i,index):
    
    midlocname='m{0}_get_key{1}'.format(state_i,index)
    midloc='<location name="{0}"/>'.format(midlocname)
    trans_to_mid="""\
      <transition from="{startState}" to="{midlocname}" symbol="IGet" params="p0">
         <guard>p0==key{index}</guard>         
      </transition>    
    """.format(startState="s"+str(state_i),midlocname=midlocname,index=index)
    trans_from_mid="""\
      <transition from="{midlocname}" to="{startState}" symbol="OGet" params="val{index}"/>
    """.format(startState="s"+str(state_i),midlocname=midlocname,index=index)    
    return (midloc,[trans_to_mid,trans_from_mid])

def generate_get_else(state_i):
    midlocname='m{0}_get_else'.format(state_i)
    midloc='<location name="{0}"/>'.format(midlocname)
    trans_to_mid="""\
      <transition from="{startState}" to="{midlocname}" symbol="IGet" params="p0">
         <guard>{neq}</guard>         
      </transition>    
    """.format(startState="s"+str(state_i),midlocname=midlocname,neq=generate_neq(state_i-1))
    trans_from_mid="""\
      <transition from="{midlocname}" to="{startState}" symbol="ONOK" />
    """.format(startState="s"+str(state_i),midlocname=midlocname)    
    return (midloc,[trans_to_mid,trans_from_mid])

def generate_put_key(state_i,index):
    midlocname='m{0}_put_key{1}'.format(state_i,index)
    midloc='<location name="{0}"/>'.format(midlocname)
    trans_to_mid="""\
      <transition from="{startState}" to="{midlocname}" symbol="IPut" params="p0,p1">
         <guard>p0==key{index}</guard>     
         <assignments>
            <assign to="val{index}">p1</assign>
         </assignments>             
      </transition>    
    """.format(startState="s"+str(state_i),midlocname=midlocname,index=index)
    trans_from_mid="""\
      <transition from="{midlocname}" to="{startState}" symbol="OOK" params=""/>
    """.format(startState="s"+str(state_i),midlocname=midlocname)    
    return (midloc,[trans_to_mid,trans_from_mid])



def generate_put_fresh(state_i):
    midlocname='m{0}_put_fresh'.format(state_i)
    midloc='<location name="{0}"/>'.format(midlocname)
    trans_to_mid="""\
      <transition from="{startState}" to="{midlocname}" symbol="IPut" params="p0,p1">
         <guard>{neq}</guard>     
         <assignments>
            <assign to="key{index}">p0</assign>
            <assign to="val{index}">p1</assign>
         </assignments>             
      </transition>    
    """.format(startState="s"+str(state_i),midlocname=midlocname,index=state_i,neq=generate_neq(state_i-1))
    trans_from_mid="""\
      <transition from="{midlocname}" to="{endState}" symbol="OOK" params=""/>
    """.format(startState="s"+str(state_i),endState="s"+str(state_i+1),midlocname=midlocname)    
    return (midloc,[trans_to_mid,trans_from_mid])


def generate_put_else(state_i):
    midlocname='m{0}_put_else'.format(state_i)
    midloc='<location name="{0}"/>'.format(midlocname)
    trans_to_mid="""\
      <transition from="{startState}" to="{midlocname}" symbol="IPut" params="p0,p1">
         <guard>{neq}</guard>               
      </transition>    
    """.format(startState="s"+str(state_i),midlocname=midlocname,index=state_i,neq=generate_neq(state_i-1))
    trans_from_mid="""\
      <transition from="{midlocname}" to="{startState}" symbol="ONOK" />
    """.format(startState="s"+str(state_i),midlocname=midlocname)    
    return (midloc,[trans_to_mid,trans_from_mid])

(locations,transitions)=generate_states_and_transitions(stacksize)

mid_loc="".join(locations)
mid_trans="".join(transitions)

mid_vardecls=""
for i in range(0,stacksize):
    mid_vardecls=mid_vardecls+vardecl_template.format(startState=i,endState=i+1)

str=begin_str + begin_vardecls + mid_vardecls + end_vardecls +  \
      begin_loc + mid_loc + end_loc + begin_trans + mid_trans + end_trans + end_str


f=open(model_file_out,'w')
f.write(str)
f.close()
