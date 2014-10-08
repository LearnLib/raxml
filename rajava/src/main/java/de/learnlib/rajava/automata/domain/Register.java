//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions are met:
//
//1. Redistributions of source code must retain the above copyright notice, this
//   list of conditions and the following disclaimer. 
//2. Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
//ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
//(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
//LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
//ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
//SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//The views and conclusions contained in the software and documentation are those
//of the authors and should not be interpreted as representing official policies, 
//either expressed or implied, of the FreeBSD Project.
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
