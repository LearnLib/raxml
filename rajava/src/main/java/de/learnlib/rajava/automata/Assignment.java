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

import de.learnlib.rajava.automata.domain.Reference;
import de.learnlib.rajava.automata.domain.Variable;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.DataValue;

/**
 * Basic assignment
 *
 */
public class Assignment {

    private Reference left;

    private Reference right;

    private DataValue preparedValue = null;

    public Assignment(Reference left, Reference right) {
        this.left = left;
        this.right = right;
    }

    /**
     * prepare is used in parallel assignments it retrieves the value from the
     * right-hand side of the expression.
     *
     * @param r
     */
    public void prepare(Register r) {
        this.preparedValue = getRight(r).getValue();
    }

    /**
     * performs the actual assignment
     *
     * @param r
     */
    public void assign(Register r) {
        Variable lRef = getLeft(r);

        if (this.preparedValue == null) {
            this.preparedValue = getRight(r).getValue();
        }

        lRef.setValue(this.preparedValue);

        this.preparedValue = null;
    }

    /**
     * @return the left
     */
    public Variable getLeft(Register r) {
        return r.resolve(left);
    }

    /**
     * @param left the left to set
     */
    public void setLeft(Reference left) {
        this.left = left;
    }

    /**
     * @return the right
     */
    public Variable getRight(Register r) {
        return r.resolve(right);
    }

    /**
     * @param right the right to set
     */
    public void setRight(Reference right) {
        this.right = right;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.left).append(":=").append(this.right).append(";");
        return b.toString();
    }

    /**
     * @return the left
     */
    public Reference getLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public Reference getRight() {
        return right;
    }

}
