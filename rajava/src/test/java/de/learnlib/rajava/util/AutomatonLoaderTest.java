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
package de.learnlib.rajava.util;

import de.learnlib.rajava.automata.domain.DataValue;
import de.learnlib.rajava.automata.IORAAutomaton;
import de.learnlib.rajava.automata.PSymbolInstance;
import de.learnlib.rajava.automata.ParameterizedSymbol;
import de.learnlib.rajava.automata.domain.Register;
import de.learnlib.rajava.automata.domain.ValueGenerator;
import java.io.InputStream;
import net.automatalib.words.Word;
import org.testng.annotations.Test;

/**
 *
 * @author falk
 */
public class AutomatonLoaderTest {

    public AutomatonLoaderTest() {
    }

    @Test
    public void testLoadAutomaton() {

        InputStream is = AutomatonLoaderTest.class.getResourceAsStream(
                "/de/learnlib/rajava/model.register.xml");

        AutomatonLoader loader = new AutomatonLoader(is);

        IORAAutomaton iora = loader.getIORA();
        System.out.println(iora);

        ParameterizedSymbol iLogin = new ParameterizedSymbol("ILogin", 2);
        ParameterizedSymbol iRegister = new ParameterizedSymbol("IRegister", 2);

        Word<PSymbolInstance> query = Word.epsilon();

        query = query.append(new PSymbolInstance(iLogin, new DataValue("user"), new DataValue("pass")));
        query = query.append(new PSymbolInstance(iRegister, new DataValue("user"), new DataValue("pass")));

        ValueGenerator gen = new ValueGenerator() {

            @Override
            public DataValue generateValue(Register r, DataValue... blocked) {
                throw new UnsupportedOperationException("Not needed in this example.");
            }
        };

        System.out.println("Query: " + query);
        System.out.println("Trace: " + iora.trace(query, gen));
    }

}
