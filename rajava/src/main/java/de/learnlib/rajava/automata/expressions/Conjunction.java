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
 * A conjunction. Empty disjunctions are false.
 */
public class Conjunction implements Predicate {

    private final Collection<Predicate> conjuncts;

    public Conjunction() {
        this.conjuncts = new ArrayList<>();
    }

    /**
     * adds a predictae to the conjunction
     *
     * @param p
     */
    public void addConjunct(Predicate p) {
        this.conjuncts.add(p);
    }

    @Override
    public boolean holds(Register r) {
        for (Predicate p : conjuncts) {
            if (!p.holds(r)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<Predicate> iter = this.conjuncts.iterator();

        if (!iter.hasNext()) {
            return "true";
        }

        b.append(iter.next());

        while (iter.hasNext()) {
            b.append(" && ");
            b.append(iter.next());
        }

        return b.toString();
    }

    @Override
    public Collection<AtomicPredicate> getAtoms() {
        LinkedList<AtomicPredicate> l = new LinkedList<AtomicPredicate>();
        for (Predicate p : conjuncts) {
            l.addAll(p.getAtoms());
        }

        return l;
    }

    /**
     * @return the conjuncts
     */
    public Collection<Predicate> getConjuncts() {
        return conjuncts;
    }
}
