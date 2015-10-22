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
package it.cnr.istc.iloc.pddl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class OpTerm implements Term {

    private final Op op;
    private final List<Term> terms;

    OpTerm(Op op, List<Term> terms) {
        this.op = op;
        this.terms = terms;
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return terms.stream().flatMap(term -> term.containsFunction(function).stream()).collect(Collectors.toList());
    }

    @Override
    public Term negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        switch (op) {
            case Add:
                return terms.stream().map(term -> term.toString(file, known_terms, to_skip, mode)).collect(Collectors.joining(" + "));
            case Sub:
                return terms.get(0).toString(file, known_terms, to_skip, mode) + " - " + terms.get(1).toString(file, known_terms, to_skip, mode);
            case Mul:
                return terms.stream().map(term -> term.toString(file, known_terms, to_skip, mode)).collect(Collectors.joining(" * "));
            case Div:
                return terms.get(0).toString(file, known_terms, to_skip, mode) + " / " + terms.get(1).toString(file, known_terms, to_skip, mode);
            default:
                throw new AssertionError(op.name());
        }
    }

    enum Op {

        Add, Sub, Mul, Div
    }
}
