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

/**
 * Location of register automaton
 */
public class RALocation {

    private final int id;

    private final boolean accepting;

    RALocation(int id, boolean accepting) {
        this.id = id;
        this.accepting = accepting;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "l" + this.id;
    }

    public boolean isAccepting() {
        return this.accepting;
    }

}
