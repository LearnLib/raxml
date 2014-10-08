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
import de.learnlib.rajava.automata.domain.Variable;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;

/**
 * RTS Transition
 */
public class Transition {

    private final RALocation source;

    private final RALocation dest;

    private final Predicate guard;

    private final ParallelAssignment assignment;

    private final ParameterizedSymbol input;

    public Transition(RALocation src, ParameterizedSymbol input, Predicate guard,
            ParallelAssignment assignment, RALocation dest) {
        this.source = src;
        this.input = input;
        this.guard = guard;
        this.assignment = assignment;
        this.dest = dest;
    }

    /**
     * checks whether transition is enabled
     *
     * @param a
     * @param r
     * @return
     */
    public boolean isEnabled(PSymbolInstance a, Register r) {
        if (!input.equals(a.getBaseSymbol())) {
            return false;
        }

        if (!guard.holds(wrapParametersInRegister(a, r))) {
            return false;
        }

        return true;
    }

    /**
     * executes transition on register for concrete input
     *
     * @param a
     * @param r
     * @return
     */
    public RALocation execute(PSymbolInstance a, Register r) {
        Register r2 = wrapParametersInRegister(a, r);
        this.getAssignment().assign(r2);
        return getDest();
    }

    @Override
    public String toString() {
        //A1 -> A2 [label=f]
        StringBuilder b = new StringBuilder();
        //b.append("(");
        b.append(this.getSource()).append(" -> ");
        b.append(this.getDest()).append(" [label= <");
        b.append(this.getInput().toString()).append(" | ");
        b.append(this.getGuard()).append("<br/>");
        b.append(this.getAssignment()).append(" >]; ");
        return b.toString();
    }

    /**
     * helper method to put parameters of input into new scope on top of
     * register
     *
     * @param i
     * @param r
     * @return
     */
    public static Register wrapParametersInRegister(PSymbolInstance i, Register r) {
        int count = 0;
        Register p = new Register(r);

        for (Object o : i.getParameters()) {
            p.addReference(new Reference("p" + (++count)),
                    new Variable((DataValue) o));
        }

        return p;
    }

    /**
     * @return the source
     */
    public RALocation getSource() {
        return source;
    }

    /**
     * @return the dest
     */
    public RALocation getDest() {
        return dest;
    }

    /**
     * @return the guard
     */
    public Predicate getGuard() {
        return guard;
    }

    /**
     * @return the assignment
     */
    public ParallelAssignment getAssignment() {
        return assignment;
    }

    /**
     * @return the input
     */
    public ParameterizedSymbol getInput() {
        return input;
    }

}
