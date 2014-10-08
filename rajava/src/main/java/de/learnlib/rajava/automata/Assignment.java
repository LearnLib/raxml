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

import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Variable;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;

/**
 * Basic assignment
 *
 */
public class Assignment {

    private Reference left;

    private Reference right;

    private DataValue preparedValue = null;

    public Assignment(Reference left, Reference right) {
        this.left = left;
        this.right = right;
    }

    /**
     * prepare is used in parallel assignments it retrieves the value from the
     * right-hand side of the expression.
     *
     * @param r
     */
    public void prepare(Register r) {
        this.preparedValue = getRight(r).getValue();
    }

    /**
     * performs the actual assignment
     *
     * @param r
     */
    public void assign(Register r) {
        Variable lRef = getLeft(r);

        if (this.preparedValue == null) {
            this.preparedValue = getRight(r).getValue();
        }

        lRef.setValue(this.preparedValue);

        this.preparedValue = null;
    }

    /**
     * @return the left
     */
    public Variable getLeft(Register r) {
        return r.resolve(left);
    }

    /**
     * @param left the left to set
     */
    public void setLeft(Reference left) {
        this.left = left;
    }

    /**
     * @return the right
     */
    public Variable getRight(Register r) {
        return r.resolve(right);
    }

    /**
     * @param right the right to set
     */
    public void setRight(Reference right) {
        this.right = right;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.left).append(":=").append(this.right).append(";");
        return b.toString();
    }

    /**
     * @return the left
     */
    public Reference getLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public Reference getRight() {
        return right;
    }

}
