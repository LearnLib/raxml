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

import de.learnlib.rajava.automata.domain.DataValue;
import java.util.Arrays;

/**
 * concrete data symbol.
 */
public class PSymbolInstance {

    private final ParameterizedSymbol baseSymbol;
    private final DataValue[] parameters;

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PSymbolInstance)) {
            return false;
        }

        PSymbolInstance os = (PSymbolInstance) other;

        return (os.baseSymbol.equals(baseSymbol)
                && Arrays.equals(os.parameters, parameters));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.baseSymbol != null ? this.baseSymbol.hashCode() : 0);
        hash = 47 * hash + Arrays.deepHashCode(this.parameters);
        return hash;
    }

    public PSymbolInstance(ParameterizedSymbol baseSymbol, DataValue... parameters) {
        this.baseSymbol = baseSymbol;
        this.parameters = parameters;
    }

    public ParameterizedSymbol getBaseSymbol() {
        return this.baseSymbol;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        return baseSymbol.getName() + Arrays.toString(parameters);
    }

}
