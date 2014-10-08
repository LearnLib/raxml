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
package de.learnlib.rajava.automata.expressions;

import de.learnlib.rajava.automata.domain.Register;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A disjunction. Empty disjunctions are true.
 *
 */
public class Disjunction implements Predicate {

    private final Collection<Predicate> disjuncts;

    public Disjunction() {
        this.disjuncts = new ArrayList<>();
    }

    /**
     * adds a predictae to the disjunction
     *
     * @param p
     */
    public void addDisjunct(Predicate p) {
        this.disjuncts.add(p);
    }

    @Override
    public boolean holds(Register r) {
        for (Predicate p : disjuncts) {
            if (p.holds(r)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<Predicate> iter = this.disjuncts.iterator();

        if (!iter.hasNext()) {
            return "false";
        }

        //b.append("(");
        b.append(iter.next());

        while (iter.hasNext()) {
            b.append(" || ");
            b.append(iter.next());
        }
        //b.append(")");
        return b.toString();
    }

    @Override
    public Collection<AtomicPredicate> getAtoms() {
        LinkedList<AtomicPredicate> l = new LinkedList<>();
        for (Predicate p : disjuncts) {
            l.addAll(p.getAtoms());
        }

        return l;
    }

    /**
     * @return the disjuncts
     */
    public Collection<Predicate> getDisjuncts() {
        return disjuncts;
    }

}
