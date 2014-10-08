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

import de.learnlib.rajava.automata.graphs.DirectedMultiGraph;
import de.learnlib.rajava.automata.domain.Register;
import net.automatalib.words.Alphabet;

/**
 * Register transition system
 */
public class RTS extends DirectedMultiGraph<RALocation, Transition> {

    private RALocation initial;
    private Alphabet sigma;
    private Register register;

    /**
     * @return the sigma
     */
    public Alphabet getSigma() {
        return sigma;
    }

    /**
     * @param sigma the sigma to set
     */
    public void setSigma(Alphabet sigma) {
        this.sigma = sigma;
    }

    /**
     * @return the initial
     */
    public RALocation getInitial() {
        return initial;
    }

    /**
     * @param initial the initial to set
     */
    public void setInitial(RALocation initial) {
        this.initial = initial;
    }

    /**
     * @return the register
     */
    public Register getRegister() {
        return register;
    }

    /**
     * @param register the register to set
     */
    public void setRegister(Register register) {
        this.register = register;
    }

}
