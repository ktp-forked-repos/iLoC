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

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class AtEndTerm implements Term {

    private final Term enclosingTerm;
    private Term term;

    AtEndTerm(Term enclosingTerm) {
        this.enclosingTerm = enclosingTerm;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        assert term != null;
        this.term = term;
    }

    @Override
    public Term getEnclosingTerm() {
        return enclosingTerm;
    }

    @Override
    public Term negate(Term enclosingTerm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Term ground(Domain domain, Term enclosingTerm, Map<String, Term> known_terms) {
        AtEndTerm at_end_term = new AtEndTerm(enclosingTerm);
        at_end_term.setTerm(term.ground(domain, at_end_term, known_terms));
        return at_end_term;
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return term.containsPredicate(directed, predicate);
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return term.containsFunction(function);
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        switch (mode) {
            case Condition:
                return term.toString(file, known_terms, to_skip, Mode.AtEndCondition);
            case Effect:
                return term.toString(file, known_terms, to_skip, Mode.AtEndEffect);
            default:
                throw new AssertionError(mode.name());
        }
    }

    @Override
    public String toString() {
        return "(at end " + term + ')';
    }
}
