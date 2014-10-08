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

import de.learnlib.rajava.automata.domain.Constants;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.ValueGenerator;
import java.util.HashSet;
import java.util.Set;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;

/**
 * IO-Register Automaton
 */
public class IORAAutomaton extends RAAutomaton {

    private final Set<ParameterizedSymbol> inputs;
    private final Set<ParameterizedSymbol> outputs;
    private final Constants constants;

    public IORAAutomaton(Alphabet<ParameterizedSymbol> sigma,
            Register r, Constants c) {
        super(sigma, r, true);
        this.constants = c;

        this.inputs = new HashSet<>();
        this.outputs = new HashSet<>();
        for (ParameterizedSymbol s : sigma) {
            ParameterizedSymbol ps = (ParameterizedSymbol) s;
            if (ps.getName().startsWith("I")) {
                inputs.add(ps);
            } else {
                outputs.add(ps);
            }
        }
    }

    /**
     * @return the inputs
     */
    public Set<ParameterizedSymbol> getInputs() {
        return inputs;
    }

    /**
     * @return the outputs
     */
    public Set<ParameterizedSymbol> getOutputs() {
        return outputs;
    }

    public Constants getConstants() {
        return constants;
    }

    public Word<PSymbolInstance> trace(Word<PSymbolInstance> input,
            ValueGenerator gen) {
        //System.out.println("========================== trace =========================>");
        Word<PSymbolInstance> trace = Word.epsilon();

        RALocation loc = getInitial();
        Register r = Register.cloneRegister(getRegister(), constants);
        //System.out.println(r);

        for (PSymbolInstance ps : input) {
            trace = trace.append(ps);
            //System.out.println("Input: " + ps);

            // input
            boolean found = false;
            for (Pair<Transition, RALocation> t : getAdjacentOut(loc)) {
                if (t.getFirst().isEnabled(ps, r)) {
                    //System.out.println("Transition: " + t.getFirst());                    
                    loc = t.getSecond();
                    t.getFirst().execute(ps, r);
                    //System.out.println(r);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException("No transition enabled");
            }

            // output
            found = false;
            for (Pair<Transition, RALocation> t : getAdjacentOut(loc)) {
                OTransition ot = (OTransition) t.getFirst();
                if (ot.isEnabledO(r)) {
                    //System.out.println("Transition: " + ot);                    
                    loc = t.getSecond();
                    Pair<RALocation, PSymbolInstance> out = ot.executeOutput(r, gen);
                    trace = trace.append(out.getSecond());
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException("No transition enabled");
            }

        }
        //System.out.println("<========================= trace ==========================");        
        return trace;
    }
}
