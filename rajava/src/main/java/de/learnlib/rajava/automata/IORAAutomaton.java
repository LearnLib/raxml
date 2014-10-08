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
package de.learnlib.rajava.automata;

import de.learnlib.rajava.automata.domain.Constants;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.ValueGenerator;
import java.util.HashSet;
import java.util.Set;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

/**
 * IO-Register Automaton
 */
public class IORAAutomaton extends RAAutomaton {

    private final Set<ParameterizedSymbol> inputs;
    private final Set<ParameterizedSymbol> outputs;
    private final Constants constants;

    public IORAAutomaton(Alphabet<ParameterizedSymbol> sigma,
            Register r, Constants c) {
        super(sigma, r, true);
        this.constants = c;

        this.inputs = new HashSet<>();
        this.outputs = new HashSet<>();
        for (ParameterizedSymbol s : sigma) {
            ParameterizedSymbol ps = (ParameterizedSymbol) s;
            if (ps.getName().startsWith("I")) {
                inputs.add(ps);
            } else {
                outputs.add(ps);
            }
        }
    }

    /**
     * @return the inputs
     */
    public Set<ParameterizedSymbol> getInputs() {
        return inputs;
    }

    /**
     * @return the outputs
     */
    public Set<ParameterizedSymbol> getOutputs() {
        return outputs;
    }

    public Constants getConstants() {
        return constants;
    }

    public Word<PSymbolInstance> trace(Word<PSymbolInstance> input,
            ValueGenerator gen) {
        //System.out.println("========================== trace =========================>");
        Word<PSymbolInstance> trace = Word.epsilon();

        RALocation loc = getInitial();
        Register r = Register.cloneRegister(getRegister(), constants);
        //System.out.println(r);

        for (PSymbolInstance ps : input) {
            trace = trace.append(ps);
            //System.out.println("Input: " + ps);

            // input
            boolean found = false;
            for (Pair<Transition, RALocation> t : getAdjacentOut(loc)) {
                if (t.getFirst().isEnabled(ps, r)) {
                    //System.out.println("Transition: " + t.getFirst());                    
                    loc = t.getSecond();
                    t.getFirst().execute(ps, r);
                    //System.out.println(r);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException("No transition enabled");
            }

            // output
            found = false;
            for (Pair<Transition, RALocation> t : getAdjacentOut(loc)) {
                OTransition ot = (OTransition) t.getFirst();
                if (ot.isEnabledO(r)) {
                    //System.out.println("Transition: " + ot);                    
                    loc = t.getSecond();
                    Pair<RALocation, PSymbolInstance> out = ot.executeOutput(r, gen);
                    trace = trace.append(out.getSecond());
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException("No transition enabled");
            }

        }
        //System.out.println("<========================= trace ==========================");        
        return trace;
    }
}
