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

import de.learnlib.rajava.automata.domain.Register;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ParallelAssignment {

    private final List<Assignment> assignments;

    public ParallelAssignment() {
        this.assignments = new LinkedList<>();
    }

    public void assign(Register r) {
        for (Assignment a : assignments) {
            a.prepare(r);
        }

        for (Assignment a : assignments) {
            a.assign(r);
        }
    }

    public void addAssignment(Assignment a) {
        this.assignments.add(a);
    }

    public List<Assignment> getAssignments() {
        return this.assignments;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Assignment a : this.assignments) {
            b.append(a);
        }

        return b.toString();
    }
}
