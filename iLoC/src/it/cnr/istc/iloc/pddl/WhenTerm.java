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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class WhenTerm implements Term {

    private final Term condition;
    private final Term effect;

    WhenTerm(Term condition, Term effect) {
        this.condition = condition;
        this.effect = effect;
    }

    @Override
    public Term negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return effect.containsPredicate(directed, predicate);
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return effect.containsFunction(function);
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        List<String> disjuncts = new ArrayList<>();
        switch (mode) {
            case Effect:
                disjuncts.add(condition.toString(file, known_terms, to_skip, Mode.Condition));
                break;
            case AtStartEffect:
                disjuncts.add(condition.toString(file, known_terms, to_skip, Mode.AtStartCondition));
                break;
            case AtEndEffect:
                disjuncts.add(condition.toString(file, known_terms, to_skip, Mode.AtEndCondition));
                break;
            default:
                throw new AssertionError(mode.name());
        }
        disjuncts.add(effect.toString(file, known_terms, to_skip, mode));

        ST disjunction = file.getInstanceOf("Disjunction");
        disjunction.add("disjuncts", disjuncts);
        return disjunction.render();
    }

    @Override
    public String toString() {
        return "(when " + condition + " " + effect + ")";
    }
}
