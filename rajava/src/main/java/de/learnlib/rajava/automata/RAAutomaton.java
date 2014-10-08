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
import java.util.ArrayList;
import java.util.List;
import net.automatalib.words.Alphabet;

/**
 * Register automaton
 */
public class RAAutomaton extends RTS {

    private int ids;

    public RAAutomaton(Alphabet sigma, Register r, boolean acceptInit) {
        super();
        this.ids = 0;
        setSigma(sigma);
        setRegister(r);

        RALocation initloc = (RALocation) addNewLocation(acceptInit);
        setInitial(initloc);
    }

    public final RALocation addNewLocation(boolean accepting) {
        RALocation loc = new RALocation(++ids, accepting);
        addVertex(loc);
        return loc;
    }

    public List<RALocation> getAllStates() {
        return new ArrayList<>(getVertexes());
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("digraph IORA {").append("\n");
        for (Transition t : getEdges()) {
            b.append(t.toString()).append("\n");
        }

        b.append("}\n");
        return b.toString();
    }

}
