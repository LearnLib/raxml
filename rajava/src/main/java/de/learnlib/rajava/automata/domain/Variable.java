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
package de.learnlib.rajava.automata.domain;

/**
 * Holder for data values
 */
public class Variable {

    private DataValue value;

    public Variable(DataValue v) {
        this.value = v;
    }

    public Variable() {
        this.value = DataValue.NULL;
    }

    public DataValue getValue() {
        return this.value;
    }

    public void setValue(DataValue v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
