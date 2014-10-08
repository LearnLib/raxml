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

import de.learnlib.rajava.automata.expressions.Predicate;
import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;
import de.learnlib.rajava.automata.domain.ValueGenerator;
import java.util.ArrayList;
import java.util.Map;

/**
 * output transition.
 */
public class OTransition extends Transition {

    private final Map<Integer, Reference> outputBinding;

    public OTransition(RALocation src, ParameterizedSymbol input, Predicate guard,
            ParallelAssignment assignment, RALocation dest,
            Map<Integer, Reference> outputBinding) {
        super(src, input, guard, assignment, dest);
        this.outputBinding = outputBinding;
    }

    /**
     * executes transition on register for concrete input
     *
     * @param a
     * @param r
     * @return
     */
    @Override
    public RALocation execute(PSymbolInstance a, Register r) {
        // FIXME: this is a hack!!!
        return getDest();
    }

    public Pair<RALocation, PSymbolInstance> executeOutput(Register r, ValueGenerator gen) {
        ArrayList<DataValue> blocked = new ArrayList<>();
        DataValue[] paramValues = new DataValue[getInput().getArity()];
        for (int i = 1; i <= getInput().getArity(); i++) {
            if (outputBinding.containsKey(i)) {
                paramValues[i - 1] = r.resolve(outputBinding.get(i)).getValue();
            } else {
                paramValues[i - 1] = gen.generateValue(r, blocked.toArray(new DataValue[]{}));
            }

            blocked.add(paramValues[i - 1]);
        }

        PSymbolInstance output = new PSymbolInstance(this.getInput(), paramValues);
        Register r2 = wrapParametersInRegister(output, r);
        this.getAssignment().assign(r2);

        return new Pair<>(getDest(), output);
    }

    @Override
    public boolean isEnabled(PSymbolInstance a, Register r) {
        // FIXME: this is a hack!!!
        return true;
    }

    public boolean isEnabledO(Register r) {
        return getGuard().holds(r);
    }

    @Override
    public String toString() {
        //A1 -> A2 [label=f]
        StringBuilder b = new StringBuilder();
        //b.append("(");
        b.append(this.getSource()).append(" -> ");
        b.append(this.getDest()).append(" [label= <");
        b.append(this.getInput().toString()).append(" | ");
        b.append(this.getGuard().toString()).append(" | ");
        for (Integer i : outputBinding.keySet()) {
            b.append("p").append(i).append("/").append(outputBinding.get(i).toString()).append("; ");
        }
        b.append("<br/>");
        b.append(this.getAssignment()).append(" >]; ");
        return b.toString();
    }

    /**
     * @return the outputBinding
     */
    public Map<Integer, Reference> getOutputBinding() {
        return outputBinding;
    }
}
