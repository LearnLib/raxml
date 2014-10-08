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
package de.learnlib.rajava.automata.expressions;

import de.learnlib.rajava.automata.domain.Register;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A conjunction. Empty disjunctions are false.
 */
public class Conjunction implements Predicate {

    private final Collection<Predicate> conjuncts;

    public Conjunction() {
        this.conjuncts = new ArrayList<>();
    }

    /**
     * adds a predictae to the conjunction
     *
     * @param p
     */
    public void addConjunct(Predicate p) {
        this.conjuncts.add(p);
    }

    @Override
    public boolean holds(Register r) {
        for (Predicate p : conjuncts) {
            if (!p.holds(r)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<Predicate> iter = this.conjuncts.iterator();

        if (!iter.hasNext()) {
            return "true";
        }

        b.append(iter.next());

        while (iter.hasNext()) {
            b.append(" && ");
            b.append(iter.next());
        }

        return b.toString();
    }

    @Override
    public Collection<AtomicPredicate> getAtoms() {
        LinkedList<AtomicPredicate> l = new LinkedList<AtomicPredicate>();
        for (Predicate p : conjuncts) {
            l.addAll(p.getAtoms());
        }

        return l;
    }

    /**
     * @return the conjuncts
     */
    public Collection<Predicate> getConjuncts() {
        return conjuncts;
    }
}
