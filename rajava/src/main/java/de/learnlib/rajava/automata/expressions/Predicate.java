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
import java.util.Collection;

/**
 * Interface for boolean predicates
 */
public interface Predicate {

    /**
     * checks whether the predicate holds for a given register. The register has
     * to contain values for *all* symbolic names: variables as well as
     * parameters.
     *
     * @param r
     * @return
     */
    public boolean holds(Register r);

    /**
     * returns all atomic predicates that contribute to this predicate
     *
     * @return
     */
    public Collection<AtomicPredicate> getAtoms();

}
