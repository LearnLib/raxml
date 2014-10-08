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

import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * atomic predicate (n-ary relation)
 */
public class AtomicPredicate implements Predicate {

    private final Relation relation;

    private final Reference[] related;

    public AtomicPredicate(Relation relation, Reference[] related) {
        this.relation = relation;
        this.related = related;
    }

    @Override
    public boolean holds(Register r) {
        DataValue[] refs = new DataValue[getRelated().length];
        for (int i = 0; i < refs.length; i++) {

            if (r.resolve(getRelated()[i]) == null) {
                throw new IllegalStateException("could not resolve " + getRelated()[i]);
            }

            refs[i] = r.resolve(getRelated()[i]).getValue();
        }

        return getRelation().contains(refs);
    }

    @Override
    public String toString() {
        // TODO: re-enable n-ary version
        return related[0] + " " + getRelation().toString() + " " + related[1];
    }

    @Override
    public Collection<AtomicPredicate> getAtoms() {
        LinkedList<AtomicPredicate> l = new LinkedList<>();
        l.add(this);
        return l;
    }

    /**
     * @return the relation
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * @return the related
     */
    public Reference[] getRelated() {
        return related;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AtomicPredicate other = (AtomicPredicate) obj;
        if (this.relation.getClass() != other.relation.getClass()) {
            return false;
        }
        if (!Arrays.deepEquals(this.related, other.related)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.relation.getClass() != null ? this.relation.getClass().hashCode() : 0);
        hash = 41 * hash + Arrays.deepHashCode(this.related);
        return hash;
    }
}
