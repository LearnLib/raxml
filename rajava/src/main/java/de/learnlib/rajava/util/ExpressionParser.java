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
