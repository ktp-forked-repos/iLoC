/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package it.cnr.istc.iloc.translators.pddl;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.translators.pddl.core.AndTerm;
import it.cnr.istc.iloc.translators.pddl.core.AssignOpTerm;
import it.cnr.istc.iloc.translators.pddl.core.ConstantTerm;
import it.cnr.istc.iloc.translators.pddl.core.Function;
import it.cnr.istc.iloc.translators.pddl.core.FunctionTerm;
import it.cnr.istc.iloc.translators.pddl.core.Predicate;
import it.cnr.istc.iloc.translators.pddl.core.PredicateTerm;
import it.cnr.istc.iloc.translators.pddl.core.Term;
import it.cnr.istc.iloc.translators.pddl.core.VariableTerm;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Utils {

    private static final String KEYWORDS_EXPR;

    static {
        Keyword[] values = Keyword.values();
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                regex.append("|");
            }
            regex.append(values[i].name);
        }
        KEYWORDS_EXPR = regex.toString();
    }

    static String capitalize(final String str) {
        final char[] buffer = str.toLowerCase().replace('-', '_').toCharArray();
        buffer[0] = Character.toTitleCase(buffer[0]);
        // We don't need to check for keywords since all DDL keywords start lower case..
        return new String(buffer);
    }

    static String lowercase(final String str) {
        final char[] buffer = str.replace('-', '_').toCharArray();
        buffer[0] = Character.toLowerCase(buffer[0]);
        String c_str = new String(buffer);
        // We check for DDL keywords
        if (c_str.matches(KEYWORDS_EXPR)) {
            c_str = "_" + c_str;
        }
        return c_str;
    }

    static boolean containsPredicate(Term term, Predicate predicate) {
        if (term instanceof PredicateTerm) {
            return ((PredicateTerm) term).getPredicate() == predicate;
        } else if (term instanceof FunctionTerm) {
            return false;
        } else if (term instanceof AndTerm) {
            return ((AndTerm) term).getTerms().stream().anyMatch(t -> containsPredicate(t, predicate));
        } else if (term instanceof AssignOpTerm) {
            return false;
        } else if (term instanceof VariableTerm) {
            return false;
        } else if (term instanceof ConstantTerm) {
            return false;
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    static boolean containsFunction(Term term, Function function) {
        if (term instanceof FunctionTerm) {
            return ((FunctionTerm) term).getFunction() == function;
        } else if (term instanceof PredicateTerm) {
            return false;
        } else if (term instanceof AndTerm) {
            return ((AndTerm) term).getTerms().stream().anyMatch(t -> containsFunction(t, function));
        } else if (term instanceof AssignOpTerm) {
            return containsFunction(((AssignOpTerm) term).getFunctionTerm(), function) || containsFunction(((AssignOpTerm) term).getValue(), function);
        } else if (term instanceof VariableTerm) {
            return false;
        } else if (term instanceof ConstantTerm) {
            return false;
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private Utils() {
    }

    enum Keyword {

        TYPEDEF("typedef"),
        NUMBER(Constants.NUMBER),
        INT(Constants.INT),
        REAL(Constants.REAL),
        BOOL(Constants.BOOL),
        STRING(Constants.STRING),
        ENUM("enum"),
        CLASS("class"),
        GOAL("goal"),
        FACT("fact"),
        EXTENDS("extends"),
        PREDICATE("predicate"),
        NEW("new"),
        OR("or"),
        THIS("this"),
        SUPER("super"),
        VOID("void"),
        INF("inf"),
        TRUE("true"),
        FALSE("false"),
        STATE("state"),
        RETURN("return"),
        AT("at"),
        START("start"),
        END("end"),
        DURATION("duration");
        public final String name;

        private Keyword(String name) {
            this.name = name;
        }
    }
}
