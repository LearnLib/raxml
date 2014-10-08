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
package de.learnlib.rajava.automata;

import de.learnlib.rajava.automata.expressions.Predicate;
import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Variable;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;

/**
 * RTS Transition
 */
public class Transition {

    private final RALocation source;

    private final RALocation dest;

    private final Predicate guard;

    private final ParallelAssignment assignment;

    private final ParameterizedSymbol input;

    public Transition(RALocation src, ParameterizedSymbol input, Predicate guard,
            ParallelAssignment assignment, RALocation dest) {
        this.source = src;
        this.input = input;
        this.guard = guard;
        this.assignment = assignment;
        this.dest = dest;
    }

    /**
     * checks whether transition is enabled
     *
     * @param a
     * @param r
     * @return
     */
    public boolean isEnabled(PSymbolInstance a, Register r) {
        if (!input.equals(a.getBaseSymbol())) {
            return false;
        }

        if (!guard.holds(wrapParametersInRegister(a, r))) {
            return false;
        }

        return true;
    }

    /**
     * executes transition on register for concrete input
     *
     * @param a
     * @param r
     * @return
     */
    public RALocation execute(PSymbolInstance a, Register r) {
        Register r2 = wrapParametersInRegister(a, r);
        this.getAssignment().assign(r2);
        return getDest();
    }

    @Override
    public String toString() {
        //A1 -> A2 [label=f]
        StringBuilder b = new StringBuilder();
        //b.append("(");
        b.append(this.getSource()).append(" -> ");
        b.append(this.getDest()).append(" [label= <");
        b.append(this.getInput().toString()).append(" | ");
        b.append(this.getGuard()).append("<br/>");
        b.append(this.getAssignment()).append(" >]; ");
        return b.toString();
    }

    /**
     * helper method to put parameters of input into new scope on top of
     * register
     *
     * @param i
     * @param r
     * @return
     */
    public static Register wrapParametersInRegister(PSymbolInstance i, Register r) {
        int count = 0;
        Register p = new Register(r);

        for (Object o : i.getParameters()) {
            p.addReference(new Reference("p" + (++count)),
                    new Variable((DataValue) o));
        }

        return p;
    }

    /**
     * @return the source
     */
    public RALocation getSource() {
        return source;
    }

    /**
     * @return the dest
     */
    public RALocation getDest() {
        return dest;
    }

    /**
     * @return the guard
     */
    public Predicate getGuard() {
        return guard;
    }

    /**
     * @return the assignment
     */
    public ParallelAssignment getAssignment() {
        return assignment;
    }

    /**
     * @return the input
     */
    public ParameterizedSymbol getInput() {
        return input;
    }

}
