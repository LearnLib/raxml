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
 * A disjunction. Empty disjunctions are true.
 *
 */
public class Disjunction implements Predicate {

    private final Collection<Predicate> disjuncts;

    public Disjunction() {
        this.disjuncts = new ArrayList<>();
    }

    /**
     * adds a predictae to the disjunction
     *
     * @param p
     */
    public void addDisjunct(Predicate p) {
        this.disjuncts.add(p);
    }

    @Override
    public boolean holds(Register r) {
        for (Predicate p : disjuncts) {
            if (p.holds(r)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        Iterator<Predicate> iter = this.disjuncts.iterator();

        if (!iter.hasNext()) {
            return "false";
        }

        //b.append("(");
        b.append(iter.next());

        while (iter.hasNext()) {
            b.append(" || ");
            b.append(iter.next());
        }
        //b.append(")");
        return b.toString();
    }

    @Override
    public Collection<AtomicPredicate> getAtoms() {
        LinkedList<AtomicPredicate> l = new LinkedList<>();
        for (Predicate p : disjuncts) {
            l.addAll(p.getAtoms());
        }

        return l;
    }

    /**
     * @return the disjuncts
     */
    public Collection<Predicate> getDisjuncts() {
        return disjuncts;
    }

}
