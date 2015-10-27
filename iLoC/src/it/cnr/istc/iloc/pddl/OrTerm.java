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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class OrTerm implements Term {

    private final Term enclosingTerm;
    private final List<Term> terms = new ArrayList<>();

    OrTerm(Term enclosingTerm) {
        this.enclosingTerm = enclosingTerm;
    }

    public void addTerm(Term term) {
        assert term != null;
        terms.add(term);
    }

    public List<Term> getTerms() {
        return Collections.unmodifiableList(terms);
    }

    @Override
    public Term getEnclosingTerm() {
        return enclosingTerm;
    }

    @Override
    public Term negate(Term enclosingTerm) {
        AndTerm and = new AndTerm(enclosingTerm);
        terms.stream().map(term -> term.negate(and)).forEach(term -> and.addTerm(term));
        return and;
    }

    @Override
    public Term ground(Domain domain, Term enclosingTerm, Map<String, Term> known_terms) {
        OrTerm or = new OrTerm(enclosingTerm);
        terms.stream().map(term -> term.ground(domain, or, known_terms)).forEach(term -> or.addTerm(term));
        return or;
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return terms.stream().flatMap(term -> term.containsPredicate(directed, predicate).stream()).collect(Collectors.toList());
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return terms.stream().flatMap(term -> term.containsFunction(function).stream()).collect(Collectors.toList());
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        List<Term> c_terms = terms.stream()
                .filter(term -> !to_skip.contains(term) || (term instanceof AssignOpTerm && !to_skip.contains(((AssignOpTerm) term).getFunctionTerm()) && !to_skip.contains(((AssignOpTerm) term).getValue())))
                .collect(Collectors.toList());

        if (c_terms.isEmpty()) {
            return "";
        } else if (c_terms.size() == 1) {
            return c_terms.get(0).toString(file, known_terms, to_skip, mode);
        } else {
            ST disjunction = file.getInstanceOf("Disjunction");
            disjunction.add("disjuncts", c_terms.stream().map(term -> term.toString(file, known_terms, to_skip, mode)).collect(Collectors.toList()));
            return disjunction.render();
        }
    }

    @Override
    public String toString() {
        return "(or (" + terms.stream().map(term -> term.toString()).collect(Collectors.joining(" ")) + "))";
    }
}
