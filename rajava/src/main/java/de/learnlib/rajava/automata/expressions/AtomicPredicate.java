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

import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * atomic predicate (n-ary relation)
 */
public class AtomicPredicate implements Predicate {

    private final Relation relation;

    private final Reference[] related;

    public AtomicPredicate(Relation relation, Reference[] related) {
        this.relation = relation;
        this.related = related;
    }

    @Override
    public boolean holds(Register r) {
        DataValue[] refs = new DataValue[getRelated().length];
        for (int i = 0; i < refs.length; i++) {

            if (r.resolve(getRelated()[i]) == null) {
                throw new IllegalStateException("could not resolve " + getRelated()[i]);
            }

            refs[i] = r.resolve(getRelated()[i]).getValue();
        }

        return getRelation().contains(refs);
    }

    @Override
    public String toString() {
        // TODO: re-enable n-ary version
        return related[0] + " " + getRelation().toString() + " " + related[1];
    }

    @Override
    public Collection<AtomicPredicate> getAtoms() {
        LinkedList<AtomicPredicate> l = new LinkedList<>();
        l.add(this);
        return l;
    }

    /**
     * @return the relation
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * @return the related
     */
    public Reference[] getRelated() {
        return related;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AtomicPredicate other = (AtomicPredicate) obj;
        if (this.relation.getClass() != other.relation.getClass()) {
            return false;
        }
        if (!Arrays.deepEquals(this.related, other.related)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.relation.getClass() != null ? this.relation.getClass().hashCode() : 0);
        hash = 41 * hash + Arrays.deepHashCode(this.related);
        return hash;
    }
}
