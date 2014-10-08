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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A register is a collection of named variables.
 */
public class Register {

    private Register next = null;

    protected Map<String, Variable> variables;

    public Register(Register next) {
        this();
        this.next = next;
    }

    public Register() {
        // it is important that the keyset is iterated
        // in the same order!!!!
        // this assumptions is used for constants
        this.variables = new LinkedHashMap<String, Variable>();
    }

    /**
     * Retrieve reference locally (only in current scope)
     *
     * @param key
     * @return
     */
    public Variable resolveLocal(Reference key) {
        return this.variables.get(key.getName());
    }

    /**
     * Retrieve reference (in all scopes)
     *
     * @param key
     * @return
     */
    public Variable resolve(Reference key) {
        Variable ref = resolveLocal(key);
        if (ref == null && this.next != null) {
            return this.next.resolve(key);
        }

        return ref;
    }

    /**
     * Add new reference to local scope
     *
     * @param key
     * @param ref
     */
    public void addReference(Reference key, Variable ref) {
        this.variables.put(key.getName(), ref);
    }

    /**
     * Clear current scope
     *
     */
    public void clear() {
        this.variables.clear();
    }

    /**
     * returns number of variables in current scope
     *
     * @return
     */
    public int size() {
        return this.variables.size();
    }

    /**
     * get names of variables in current scope
     *
     * @return
     */
    public Collection<String> getKeys() {
        return this.variables.keySet();
    }

    @Override
    public String toString() {
        int level = 0;
        StringBuilder b = new StringBuilder();
        b.append(toString(++level));
        b.append("#=#=#=#=====#=#=#=#");
        return b.toString();
    }

    private String toString(int level) {
        StringBuilder b = new StringBuilder();
        b.append("#=#=# Level ").append(level).append(" #=#=#").append("\n");
        for (Map.Entry<String, Variable> v : this.variables.entrySet()) {
            b.append(v.getKey()).append(" = ").append(v.getValue()).append("\n");
        }

        if (this.next != null) {
            b.append(this.next.toString(++level));
        }

        return b.toString();
    }

    /**
     * Get next scope
     *
     * @return
     */
    public Register getNext() {
        return this.next;
    }

    /**
     * deep-clones the register
     *
     * @param r
     * @return
     */
    public static Register cloneRegister(Register r) {
        if (r == null) {
            return null;
        }

        Register cloned = new Register(cloneRegister(r.getNext()));
        for (String s : r.getKeys()) {
            Reference ref = new Reference(s);
            DataValue val = r.resolveLocal(ref).getValue();
            cloned.addReference(ref, new Variable(val));
        }

        return cloned;
    }

    public static Register cloneRegister(Register r, Constants constants) {
        Register cloned = new Register(constants);
        for (String s : r.getKeys()) {
            Reference ref = new Reference(s);
            DataValue val = r.resolveLocal(ref).getValue();
            cloned.addReference(ref, new Variable(val));
        }

        return cloned;
    }
}
