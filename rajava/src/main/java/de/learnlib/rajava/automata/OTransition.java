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
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;
import de.learnlib.rajava.automata.domain.ValueGenerator;
import java.util.ArrayList;
import java.util.Map;

/**
 * output transition.
 */
public class OTransition extends Transition {

    private final Map<Integer, Reference> outputBinding;

    public OTransition(RALocation src, ParameterizedSymbol input, Predicate guard,
            ParallelAssignment assignment, RALocation dest,
            Map<Integer, Reference> outputBinding) {
        super(src, input, guard, assignment, dest);
        this.outputBinding = outputBinding;
    }

    /**
     * executes transition on register for concrete input
     *
     * @param a
     * @param r
     * @return
     */
    @Override
    public RALocation execute(PSymbolInstance a, Register r) {
        // FIXME: this is a hack!!!
        return getDest();
    }

    public Pair<RALocation, PSymbolInstance> executeOutput(Register r, ValueGenerator gen) {
        ArrayList<DataValue> blocked = new ArrayList<>();
        DataValue[] paramValues = new DataValue[getInput().getArity()];
        for (int i = 1; i <= getInput().getArity(); i++) {
            if (outputBinding.containsKey(i)) {
                paramValues[i - 1] = r.resolve(outputBinding.get(i)).getValue();
            } else {
                paramValues[i - 1] = gen.generateValue(r, blocked.toArray(new DataValue[]{}));
            }

            blocked.add(paramValues[i - 1]);
        }

        PSymbolInstance output = new PSymbolInstance(this.getInput(), paramValues);
        Register r2 = wrapParametersInRegister(output, r);
        this.getAssignment().assign(r2);

        return new Pair<>(getDest(), output);
    }

    @Override
    public boolean isEnabled(PSymbolInstance a, Register r) {
        // FIXME: this is a hack!!!
        return true;
    }

    public boolean isEnabledO(Register r) {
        return getGuard().holds(r);
    }

    @Override
    public String toString() {
        //A1 -> A2 [label=f]
        StringBuilder b = new StringBuilder();
        //b.append("(");
        b.append(this.getSource()).append(" -> ");
        b.append(this.getDest()).append(" [label= <");
        b.append(this.getInput().toString()).append(" | ");
        b.append(this.getGuard().toString()).append(" | ");
        for (Integer i : outputBinding.keySet()) {
            b.append("p").append(i).append("/").append(outputBinding.get(i).toString()).append("; ");
        }
        b.append("<br/>");
        b.append(this.getAssignment()).append(" >]; ");
        return b.toString();
    }

    /**
     * @return the outputBinding
     */
    public Map<Integer, Reference> getOutputBinding() {
        return outputBinding;
    }
}
