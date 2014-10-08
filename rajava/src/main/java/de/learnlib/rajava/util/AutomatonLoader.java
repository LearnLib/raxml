//Copyright (C) 2014 TU Dortmund
//This file is part of LearnLib, http://www.learnlib.de/.
//
//LearnLib is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License version 3.0 as published by the Free Software Foundation.
//
//LearnLib is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with LearnLib; if not, see
//http://www.gnu.de/documents/lgpl.en.html.
package de.learnlib.rajava.util;

import automata.xml.RegisterAutomaton;
import de.learnlib.rajava.automata.Assignment;
import de.learnlib.rajava.automata.expressions.Conjunction;
import de.learnlib.rajava.automata.domain.Constants;
import de.learnlib.rajava.automata.domain.DataValue;
import de.learnlib.rajava.automata.IORAAutomaton;
import de.learnlib.rajava.automata.OTransition;
import de.learnlib.rajava.automata.ParallelAssignment;
import de.learnlib.rajava.automata.ParameterizedSymbol;
import de.learnlib.rajava.automata.expressions.Predicate;
import de.learnlib.rajava.automata.RALocation;
import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.Transition;
import de.learnlib.rajava.automata.domain.Variable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXB;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.SimpleAlphabet;

/**
 *
 */
public class AutomatonLoader {

    private final Map<String, ParameterizedSymbol> sigmaMap
            = new HashMap<>();

    private final Map<String, RALocation> stateMap
            = new HashMap<>();

    private final Map<String, Reference> constMap
            = new HashMap<>();

    private IORAAutomaton iora;

    public AutomatonLoader(InputStream is) {
        loadModel(is);
    }

    public IORAAutomaton getIORA() {
        return this.iora;
    }

    private void loadModel(InputStream is) {

        RegisterAutomaton a = unmarschall(is);

        Alphabet sigma = getAlphabet(a.getAlphabet());
        Constants consts = getConstants(a.getConstants());
        Register regs = getRegisters(a.getGlobals());

        iora = new IORAAutomaton(sigma, regs, consts);

        // create loc map
        for (automata.xml.RegisterAutomaton.Locations.Location l : a.getLocations().getLocation()) {
            if (l.getInitial() != null && l.getInitial().equals("true")) {
                stateMap.put(l.getName(), iora.getInitial());
            } else {
                stateMap.put(l.getName(), iora.addNewLocation(true));
            }
        }

        // transitions
        for (automata.xml.RegisterAutomaton.Transitions.Transition t : a.getTransitions().getTransition()) {

            ParameterizedSymbol ps = sigmaMap.get(t.getSymbol());
            RALocation from = stateMap.get(t.getFrom());
            RALocation to = stateMap.get(t.getTo());

            List<String> pnames = new ArrayList<>();
            if (t.getParams() != null) {
                pnames = Arrays.asList(t.getParams().split(","));
            }

            ParallelAssignment pAss = new ParallelAssignment();
            String gstring = t.getGuard();
            Predicate p = new Conjunction();
            if (gstring != null) {
                for (Entry<String, Reference> e : constMap.entrySet()) {
                    gstring = gstring.replace(e.getKey(), e.getValue().getName());
                }
                ExpressionParser parser = new ExpressionParser(
                        gstring, new ArrayList<>(pnames));
                p = parser.getPredicate();
            }

            if (t.getAssignments() != null) {
                for (automata.xml.RegisterAutomaton.Transitions.Transition.Assignments.Assign assign
                        : t.getAssignments().getAssign()) {
                    Assignment ass;
                    if (pnames.contains(assign.getValue())) {
                        ass = new Assignment(
                                new Reference(assign.getTo()),
                                new Reference("p" + (1 + pnames.indexOf(assign.getValue()))));
                    } else if (constMap.containsKey(assign.getValue())) {
                        ass = new Assignment(
                                new Reference(assign.getTo()),
                                new Reference(constMap.get(assign.getValue()).getName()));
                    } else {
                        ass = new Assignment(
                                new Reference(assign.getTo()),
                                new Reference(assign.getValue()));

                    }
                    pAss.addAssignment(ass);
                }
            }

            // output
            if (((String) ps.getName()).startsWith("O")) {

                Map<Integer, Reference> outMap = new HashMap<>();
                for (String s : pnames) {
                    String sx = s;
                    // check if there was an assignment,
                    // these seem to be meant to 
                    // happen before the output
                    if (regs.getKeys().contains(s)) {
                        for (Assignment ass : pAss.getAssignments()) {
                            if (ass.getLeft().getName().equals(sx)) {
                                sx = ass.getRight().getName();
                                break;
                            }
                        }
                    }
                    if (!regs.getKeys().contains(s)) {
                        sx = constMap.get(s).getName();
                    }
                    outMap.put(outMap.size() + 1, new Reference(sx));
                }

                OTransition tOut = new OTransition(from, ps, p, pAss, to, outMap);
                iora.addEdge(from, tOut, to);
                //System.out.println("Loading: " + tOut);
            } // input
            else {
                //System.out.println("Guard: " + gstring);
                Transition tIn = new Transition(from, ps, p, pAss, to);
                //System.out.println("Loading: " + tIn);
                iora.addEdge(from, tIn, to);
            }
        }
    }

    private Alphabet getAlphabet(automata.xml.RegisterAutomaton.Alphabet a) {
        Alphabet<ParameterizedSymbol> ret = new SimpleAlphabet<>();
        for (RegisterAutomaton.Alphabet.Inputs.Symbol s : a.getInputs().getSymbol()) {
            int pcount = s.getParam().size();
            ParameterizedSymbol ps = new ParameterizedSymbol(
                    s.getName().startsWith("I") ? s.getName() : "I" + s.getName(),
                    pcount);
            ret.add(ps);
            sigmaMap.put(s.getName(), ps);
            //System.out.println("Loading: " + ps);
        }
        for (RegisterAutomaton.Alphabet.Outputs.Symbol s : a.getOutputs().getSymbol()) {
            int pcount = s.getParam().size();
            ParameterizedSymbol ps = new ParameterizedSymbol(
                    s.getName().startsWith("O") ? s.getName() : "O" + s.getName(),
                    pcount);
            ret.add(ps);
            sigmaMap.put(s.getName(), ps);
            //System.out.println("Loading: " + ps);
        }
        return ret;
    }

    private Constants getConstants(automata.xml.RegisterAutomaton.Constants c) {
        Constants ret = new Constants();
        for (automata.xml.RegisterAutomaton.Constants.Constant def : c.getConstant()) {
            int cv = Integer.parseInt(def.getValue());
            ret.addReference(new Reference("" + cv),
                    new Variable(new DataValue(cv)));

            constMap.put(def.getValue(), new Reference("" + cv));
            constMap.put(def.getName(), new Reference("" + cv));
        }
        //System.out.println("Loading: " + ret);
        return ret;
    }

    private Register getRegisters(automata.xml.RegisterAutomaton.Globals g) {
        Register ret = new Register();
        for (automata.xml.RegisterAutomaton.Globals.Variable def : g.getVariable()) {
            ret.addReference(new Reference(def.getName()),
                    new Variable(new DataValue(Integer.parseInt(def.getValue()))));
        }
        //System.out.println("Loading: " + ret);
        return ret;
    }

    private RegisterAutomaton unmarschall(InputStream is) {
        return JAXB.unmarshal(is, RegisterAutomaton.class);
    }

}
