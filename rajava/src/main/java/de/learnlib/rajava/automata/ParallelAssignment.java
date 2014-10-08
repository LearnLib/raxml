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

import de.learnlib.rajava.automata.domain.Register;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ParallelAssignment {

    private final List<Assignment> assignments;

    public ParallelAssignment() {
        this.assignments = new LinkedList<>();
    }

    public void assign(Register r) {
        for (Assignment a : assignments) {
            a.prepare(r);
        }

        for (Assignment a : assignments) {
            a.assign(r);
        }
    }

    public void addAssignment(Assignment a) {
        this.assignments.add(a);
    }

    public List<Assignment> getAssignments() {
        return this.assignments;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Assignment a : this.assignments) {
            b.append(a);
        }

        return b.toString();
    }
}
