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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class AtTerm implements Term {

    private final Term enclosingTerm;
    private final BigDecimal at;
    private PredicateTerm predicateTerm;

    AtTerm(Term enclosingTerm, BigDecimal at) {
        this.enclosingTerm = enclosingTerm;
        this.at = at;
    }

    public BigDecimal getAt() {
        return at;
    }

    public PredicateTerm getPredicateTerm() {
        return predicateTerm;
    }

    public void setPredicateTerm(PredicateTerm predicateTerm) {
        assert predicateTerm != null;
        this.predicateTerm = predicateTerm;
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
        AtTerm at_term = new AtTerm(enclosingTerm, at);
        at_term.setPredicateTerm((PredicateTerm) predicateTerm.ground(domain, at_term, known_terms));
        return at_term;
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return predicateTerm.containsPredicate(directed, predicate);
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return Collections.emptyList();
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
