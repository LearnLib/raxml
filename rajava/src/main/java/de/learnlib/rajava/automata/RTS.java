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

import de.learnlib.rajava.automata.graphs.DirectedMultiGraph;
import de.learnlib.rajava.automata.domain.Register;
import net.automatalib.words.Alphabet;

/**
 * Register transition system
 */
public class RTS extends DirectedMultiGraph<RALocation, Transition> {

    private RALocation initial;
    private Alphabet sigma;
    private Register register;

    /**
     * @return the sigma
     */
    public Alphabet getSigma() {
        return sigma;
    }

    /**
     * @param sigma the sigma to set
     */
    public void setSigma(Alphabet sigma) {
        this.sigma = sigma;
    }

    /**
     * @return the initial
     */
    public RALocation getInitial() {
        return initial;
    }

    /**
     * @param initial the initial to set
     */
    public void setInitial(RALocation initial) {
        this.initial = initial;
    }

    /**
     * @return the register
     */
    public Register getRegister() {
        return register;
    }

    /**
     * @param register the register to set
     */
    public void setRegister(Register register) {
        this.register = register;
    }

}
