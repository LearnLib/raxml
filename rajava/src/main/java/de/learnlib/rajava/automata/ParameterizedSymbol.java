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

import java.util.Objects;

/**
 * abstract data symbol.
 */
public class ParameterizedSymbol {

    private final String name;

    private final int arity;

    public ParameterizedSymbol(String name, int arity) {
        this.name = name;
        this.arity = arity;
    }

    /**
     * @return the arity
     */
    public int getArity() {
        return arity;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name).append("(");
        for (int i = 0; i < arity; i++) {
            b.append("p").append((i + 1));
            if (i < arity - 1) {
                b.append(",");
            }
        }

        b.append(")");
        return b.toString();
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + this.arity;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParameterizedSymbol other = (ParameterizedSymbol) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.arity != other.arity) {
            return false;
        }
        return true;
    }

}
