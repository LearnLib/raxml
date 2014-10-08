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
 * Pair of objects
 *
 * @author fh
 * @param <P1>
 * @param <P2>
 */
public class Pair<P1, P2> {

    private P1 first;
    private P2 second;

    public Pair(P1 p1, P2 p2) {
        this.first = p1;
        this.second = p2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<P1, P2> other = (Pair<P1, P2>) obj;
        if (this.first != other.first && (this.first == null || !this.first.equals(other.first))) {
            return false;
        }
        if (this.second != other.second && (this.second == null || !this.second.equals(other.second))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 71 * hash + (this.second != null ? this.second.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "," + second.toString() + ")";
    }

    /**
     * @return the first
     */
    public P1 getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(P1 first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public P2 getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(P2 second) {
        this.second = second;
    }

}
