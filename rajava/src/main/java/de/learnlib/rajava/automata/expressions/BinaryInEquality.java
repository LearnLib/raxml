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

import de.learnlib.rajava.automata.domain.DataValue;

/**
 * a != b
 *
 */
public class BinaryInEquality implements Relation {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public boolean contains(DataValue[] tuple) {
        return !tuple[0].equals(tuple[1]);
    }

    @Override
    public String toString() {
        return "!=";
    }

}
