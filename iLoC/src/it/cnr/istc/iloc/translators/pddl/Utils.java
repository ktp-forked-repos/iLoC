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

import it.cnr.istc.iloc.translators.pddl.parser.AndTerm;
import it.cnr.istc.iloc.translators.pddl.parser.AssignOpTerm;
import it.cnr.istc.iloc.translators.pddl.parser.ConstantTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Function;
import it.cnr.istc.iloc.translators.pddl.parser.FunctionTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Predicate;
import it.cnr.istc.iloc.translators.pddl.parser.PredicateTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Term;
import it.cnr.istc.iloc.translators.pddl.parser.VariableTerm;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Utils {

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
}
