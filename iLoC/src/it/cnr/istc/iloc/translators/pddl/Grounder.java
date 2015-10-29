/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.iloc.translators.pddl;

import it.cnr.istc.iloc.translators.pddl.parser.AndTerm;
import it.cnr.istc.iloc.translators.pddl.parser.AssignOpTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Constant;
import it.cnr.istc.iloc.translators.pddl.parser.ConstantTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Domain;
import it.cnr.istc.iloc.translators.pddl.parser.EqTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Function;
import it.cnr.istc.iloc.translators.pddl.parser.FunctionTerm;
import it.cnr.istc.iloc.translators.pddl.parser.OrTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Predicate;
import it.cnr.istc.iloc.translators.pddl.parser.PredicateTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Problem;
import it.cnr.istc.iloc.translators.pddl.parser.Term;
import it.cnr.istc.iloc.translators.pddl.parser.VariableTerm;
import it.cnr.istc.iloc.utils.CartesianProductGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Grounder {

    private final Domain domain;
    private final Problem problem;
    private final Set<Predicate> static_predicates;
    private final Set<Function> static_functions;
    private final Map<String, Constant> assignments = new HashMap<>();
    private final Map<String, StateVariable> stateVariables = new LinkedHashMap<>();
    private final Map<String, GroundAction> actions = new LinkedHashMap<>();
    private GD gd = null;

    Grounder(Domain domain, Problem problem) {
        this.domain = domain;
        this.problem = problem;

        // Check for static predicates and functions..
        this.static_predicates = domain.getPredicates().values().stream().filter(predicate -> domain.getActions().values().stream().noneMatch(action -> Utils.containsPredicate(action.getEffect(), predicate)) && domain.getDurativeActions().values().stream().noneMatch(action -> Utils.containsPredicate(action.getEffect(), predicate))).collect(Collectors.toSet());
        this.static_functions = domain.getFunctions().values().stream().filter(function -> domain.getActions().values().stream().noneMatch(action -> Utils.containsFunction(action.getEffect(), function)) && domain.getDurativeActions().values().stream().noneMatch(action -> Utils.containsFunction(action.getEffect(), function))).collect(Collectors.toSet());

        domain.getActions().values().forEach(action -> {
            CartesianProductGenerator<Constant> cartesian_product = new CartesianProductGenerator<>(action.getVariables().stream().map(var -> var.getType().getInstances().toArray(new Constant[var.getType().getInstances().size()])).toArray(Constant[][]::new));
            for (Constant[] cs : cartesian_product) {
                assignments.clear();
                for (int i = 0; i < cs.length; i++) {
                    assignments.put(action.getVariables().get(i).getName(), cs[i]);
                }
                GroundAction ga = new GroundAction(action.getName() + "_" + Stream.of(cs).map(c -> c.getName()).collect(Collectors.joining("_")));
                actions.put(ga.getName(), ga);
                gd = ga.getPrecondition();
                visitPreconditionTerm(ga, action.getPrecondition());
                gd = ga.getEffect();
                visitEffectTerm(ga, action.getEffect());
            }
        });
    }

    public Domain getDomain() {
        return domain;
    }

    public Problem getProblem() {
        return problem;
    }

    public List<StateVariable> getStateVariables() {
        return new ArrayList<>(stateVariables.values());
    }

    public List<GroundAction> getActions() {
        return new ArrayList<>(actions.values());
    }

    String ground(Term term) {
        if (term instanceof VariableTerm) {
            return assignments.get(((VariableTerm) term).getVariable().getName()).getName();
        } else if (term instanceof ConstantTerm) {
            return ((ConstantTerm) term).getConstant().getName();
        } else if (term instanceof PredicateTerm) {
            String predicate_name = ((PredicateTerm) term).getArguments().isEmpty() ? ((PredicateTerm) term).getPredicate().getName() : ((PredicateTerm) term).getPredicate().getName() + "_" + ((PredicateTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            if (!stateVariables.containsKey(predicate_name)) {
                StateVariable new_state_variable = new StateVariable(predicate_name);
                new_state_variable.addValue(new StateVariableValue(new_state_variable, "True"));
                new_state_variable.addValue(new StateVariableValue(new_state_variable, "False"));
                stateVariables.put(predicate_name, new_state_variable);
            }
            return predicate_name;
        } else if (term instanceof FunctionTerm) {
            String function_name = ((FunctionTerm) term).getArguments().isEmpty() ? ((FunctionTerm) term).getFunction().getName() : ((FunctionTerm) term).getFunction().getName() + "_" + ((FunctionTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            if (!stateVariables.containsKey(function_name)) {
                StateVariable new_state_variable = new StateVariable(function_name);
                ((FunctionTerm) term).getFunction().getType().getInstances().stream().forEach(c -> {
                    new_state_variable.addValue(new StateVariableValue(new_state_variable, c.getName()));
                });
                stateVariables.put(function_name, new_state_variable);
            }
            return function_name;
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    void visitPreconditionTerm(GroundAction action, Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ground(term);
            if (((PredicateTerm) term).isDirected()) {
                gd.addGD(stateVariables.get(predicate_name).getValue("True"));
            } else {
                gd.addGD(stateVariables.get(predicate_name).getValue("False"));
            }
        } else if (term instanceof AndTerm) {
            ((AndTerm) term).getTerms().stream().forEach(t -> visitPreconditionTerm(action, t));
        } else if (term instanceof OrTerm) {
            gd = new OR(gd);
            ((OrTerm) term).getTerms().stream().forEach(t -> visitPreconditionTerm(action, t));
            gd = gd.getEnclosingDG();
        } else if (term instanceof EqTerm) {
            if (((EqTerm) term).isDirected()) {
                if (((EqTerm) term).getFirstTerm() instanceof FunctionTerm && (((EqTerm) term).getSecondTerm() instanceof ConstantTerm || ((EqTerm) term).getSecondTerm() instanceof VariableTerm)) {
                    String function_name = ground(((EqTerm) term).getFirstTerm());
                    gd.addGD(stateVariables.get(function_name).getValue(ground(((EqTerm) term).getSecondTerm())));
                } else {
                    throw new UnsupportedOperationException(((EqTerm) term).getFirstTerm().getClass().getName() + " = " + ((EqTerm) term).getSecondTerm().getClass().getName());
                }
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    void visitEffectTerm(GroundAction action, Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ground(term);
            if (((PredicateTerm) term).isDirected()) {
                gd.addGD(stateVariables.get(predicate_name).getValue("True"));
            } else {
                gd.addGD(stateVariables.get(predicate_name).getValue("False"));
            }
        } else if (term instanceof AndTerm) {
            ((AndTerm) term).getTerms().stream().forEach(t -> visitEffectTerm(action, t));
        } else if (term instanceof AssignOpTerm) {
            String function_name = ground(((AssignOpTerm) term).getFunctionTerm());
            gd.addGD(stateVariables.get(function_name).getValue(ground(((AssignOpTerm) term).getValue())));
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    void visitAtStartConditionTerm(Term term) {

    }

    void visitOverallConditionTerm(Term term) {

    }

    void visitAtEndConditionTerm(Term term) {

    }

    void visitAtStartEffectTerm(Term term) {

    }

    void visitAtEndEffectTerm(Term term) {

    }
}
