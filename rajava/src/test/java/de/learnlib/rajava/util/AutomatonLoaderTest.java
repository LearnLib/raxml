//Copyright (C) 2014 TU Dortmund
//This file is part of LearnLib, http://www.learnlib.de/.
//
//LearnLib is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License version 3.0 as published by the Free Software Foundation.
//
//LearnLib is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with LearnLib; if not, see
//http://www.gnu.de/documents/lgpl.en.html.
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
