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

import it.cnr.istc.iloc.utils.CartesianProductGenerator;
import java.util.ArrayList;
import java.util.HashMap;
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
class ExistsTerm implements Term {

    private final List<Variable> variables;
    private final Term formula;

    ExistsTerm(List<Variable> variables, Term formula) {
        this.variables = variables;
        this.formula = formula;
    }

    @Override
    public Term negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return formula.containsPredicate(directed, predicate);
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return formula.containsFunction(function);
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        List<String> disjuncts = new ArrayList<>();

        CartesianProductGenerator<Constant> cartesian_product = new CartesianProductGenerator<>(variables.stream().map(var -> var.getType().getInstances().toArray(new Constant[var.getType().getInstances().size()])).toArray(Constant[]::new));
        for (Constant[] cs : cartesian_product) {
            assert cs.length == variables.size();
            Map<String, String> c_known_terms = new HashMap<>(known_terms);
            for (int i = 0; i < variables.size(); i++) {
                c_known_terms.put((variables.get(i)).getName(), Utils.lowercase(cs[i].getName()));
            }
            disjuncts.add(formula.toString(file, c_known_terms, to_skip, mode));
        }

        if (disjuncts.isEmpty()) {
            return "";
        } else if (disjuncts.size() == 1) {
            return disjuncts.get(0);
        } else {
            ST disjunction = file.getInstanceOf("Disjunction");
            disjunction.add("disjuncts", disjuncts);
            return disjunction.render();
        }
    }

    @Override
    public String toString() {
        return "(exists " + variables.stream().map(variable -> variable.getName() + " - " + variable.getType().getName()).collect(Collectors.joining(" ")) + " " + formula + ")";
    }
}
