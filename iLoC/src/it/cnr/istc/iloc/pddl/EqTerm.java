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
import java.util.stream.Stream;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EqTerm implements Term {

    private final boolean directed;
    private final Term firstTerm, secondTerm;

    EqTerm(boolean directed, Term firstTerm, Term secondTerm) {
        assert firstTerm != null;
        assert secondTerm != null;
        this.directed = directed;
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
    }

    public boolean isDirected() {
        return directed;
    }

    public Term getFirstTerm() {
        return firstTerm;
    }

    public Term getSecondTerm() {
        return secondTerm;
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return Stream.concat(firstTerm.containsFunction(function).stream(), secondTerm.containsFunction(function).stream()).collect(Collectors.toList());
    }

    @Override
    public Term negate() {
        return new EqTerm(!directed, firstTerm, secondTerm);
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        if (firstTerm instanceof FunctionTerm && secondTerm instanceof FunctionTerm) {
            if (directed) {
                StringBuilder sb = new StringBuilder();
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode)).append("\n");
                known_terms.put("value", Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName()) + ".value");
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode));
                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode));
                sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value").append(" != ").append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value").append(";\n");
                return sb.toString();
            }
        } else if (firstTerm instanceof FunctionTerm && !(secondTerm instanceof FunctionTerm)) {
            if (directed) {
                known_terms.put("value", secondTerm.toString(file, known_terms, to_skip, mode));
                return firstTerm.toString(file, known_terms, to_skip, mode);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(firstTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                sb.append(Utils.lowercase(((FunctionTerm) firstTerm).getFunction().getName())).append(".value").append(" != ").append(secondTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                return sb.toString();
            }
        } else if (secondTerm instanceof FunctionTerm && !(firstTerm instanceof FunctionTerm)) {
            if (directed) {
                known_terms.put("value", firstTerm.toString(file, known_terms, to_skip, mode));
                return secondTerm.toString(file, known_terms, to_skip, mode);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(secondTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                sb.append(Utils.lowercase(((FunctionTerm) secondTerm).getFunction().getName())).append(".value").append(" != ").append(firstTerm.toString(file, known_terms, to_skip, mode)).append(";\n");
                return sb.toString();
            }
        } else {
            return firstTerm.toString(file, known_terms, to_skip, mode) + (directed ? " == " : " != ") + secondTerm.toString(file, known_terms, to_skip, mode);
        }
    }

    @Override
    public String toString() {
        if (directed) {
            return "(= " + firstTerm.toString() + " " + secondTerm.toString() + ")";
        } else {
            return "(not (= " + firstTerm.toString() + " " + secondTerm.toString() + "))";
        }
    }
}
