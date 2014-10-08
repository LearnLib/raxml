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

import de.learnlib.rajava.automata.expressions.AtomicPredicate;
import de.learnlib.rajava.automata.expressions.BinaryEquality;
import de.learnlib.rajava.automata.expressions.BinaryInEquality;
import de.learnlib.rajava.automata.expressions.Conjunction;
import de.learnlib.rajava.automata.expressions.Disjunction;
import de.learnlib.rajava.automata.expressions.Predicate;
import de.learnlib.rajava.automata.domain.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 */
public class ExpressionParser {

    private final String expLine;
    private final Map<String, Integer> pMap;

    private Predicate predicate;

    public ExpressionParser(String exp, ArrayList<String> pnames) {
        expLine = exp.trim();
        pMap = new HashMap<>();
        int i = 0;
        for (String pname : pnames) {
            pMap.put(pname, ++i);
        }

        buildExpression();
    }

    private void buildExpression() {
        predicate = buildDisjunction(expLine);
    }

    private Predicate buildDisjunction(String dis) {
        StringTokenizer tok = new StringTokenizer(dis, "||");
        if (tok.countTokens() < 2) {
            return buildConjunction(dis);
        }

        Disjunction d = new Disjunction();
        while (tok.hasMoreTokens()) {
            d.addDisjunct(buildConjunction(tok.nextToken().trim()));
        }

        return d;
    }

    private Predicate buildConjunction(String con) {
        StringTokenizer tok = new StringTokenizer(con, "&&");
        if (tok.countTokens() < 2) {
            return buildPredicate(con);
        }

        Conjunction c = new Conjunction();
        while (tok.hasMoreTokens()) {
            c.addConjunct(buildPredicate(tok.nextToken().trim()));
        }

        return c;
    }

    private Predicate buildPredicate(String pred) {
        pred = pred.replace("!=", "<>");

        if (pred.contains("==")) {
            String[] related = pred.split("==");
            AtomicPredicate ap = new AtomicPredicate(new BinaryEquality(), new Reference[]{
                getReference(related[0].trim()),
                getReference(related[1].trim())
            });
            return ap;

        } else if (pred.contains("<>")) {
            String[] related = pred.split("<>");
            AtomicPredicate ap = new AtomicPredicate(new BinaryInEquality(), new Reference[]{
                getReference(related[0].trim()),
                getReference(related[1].trim())
            });
            return ap;
        }
        return null;
    }

    private Reference getReference(String name) {
        if (pMap.containsKey(name)) {
            return new Reference("p" + pMap.get(name));
        } else {
            return new Reference(name);
        }
    }

    /**
     * @return the predicate
     */
    public Predicate getPredicate() {
        return predicate;
    }

}
