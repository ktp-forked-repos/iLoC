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
import it.cnr.istc.iloc.translators.pddl.parser.PDDLInstance;
import it.cnr.istc.iloc.translators.pddl.parser.Parser;
import it.cnr.istc.iloc.translators.pddl.parser.Predicate;
import it.cnr.istc.iloc.translators.pddl.parser.PredicateTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Problem;
import it.cnr.istc.iloc.translators.pddl.parser.Term;
import it.cnr.istc.iloc.translators.pddl.parser.VariableTerm;
import it.cnr.istc.iloc.utils.CartesianProductGenerator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PDDLTranslator {

    private static final STGroupFile GROUP_FILE = new STGroupFile(PDDLTranslator.class.getResource("TranslationTemplate.stg").getPath());

    static {
        GROUP_FILE.registerRenderer(String.class, new StringRenderer());
        GROUP_FILE.registerRenderer(Env.class, new EnvRenderer(GROUP_FILE));
    }
    private final Domain domain;
    private final Problem problem;
    private Agent agent;
    private Set<Predicate> static_predicates;
    private Set<Function> static_functions;
    private final Map<String, Constant> assignments = new HashMap<>();
    private Env env;

    public PDDLTranslator(Domain domain, Problem problem) {
        this.domain = domain;
        this.problem = problem;
    }

    public PDDLTranslator(File pddl_domain, File pddl_problem) throws IOException {
        PDDLInstance instance = Parser.parse(pddl_domain, pddl_problem);
        this.domain = instance.getDomain();
        this.problem = instance.getProblem();
    }

    public Domain getDomain() {
        return domain;
    }

    public Problem getProblem() {
        return problem;
    }

    public Agent getAgent() {
        return agent;
    }

    public String translate() {
        agent = new Agent(domain.getName() + "Agent");

        // Check for static predicates and functions..
        static_predicates = domain.getPredicates().values().stream().filter(predicate -> domain.getActions().values().stream().noneMatch(action -> Utils.containsPredicate(action.getEffect(), predicate)) && domain.getDurativeActions().values().stream().noneMatch(action -> Utils.containsPredicate(action.getEffect(), predicate))).collect(Collectors.toSet());
        static_functions = domain.getFunctions().values().stream().filter(function -> domain.getActions().values().stream().noneMatch(action -> Utils.containsFunction(action.getEffect(), function)) && domain.getDurativeActions().values().stream().noneMatch(action -> Utils.containsFunction(action.getEffect(), function))).collect(Collectors.toSet());

        assignments.clear();

        domain.getPredicates().values().stream().filter(predicate -> !static_predicates.contains(predicate)).forEach(predicate -> {
            if (predicate.getVariables().isEmpty()) {
                StateVariable state_variable = new StateVariable(predicate.getName());
                state_variable.addValue(new StateVariableValue(state_variable, "True"));
                state_variable.addValue(new StateVariableValue(state_variable, "False"));
                agent.addStateVariable(state_variable);
            } else {
                CartesianProductGenerator<Constant> cartesian_product = new CartesianProductGenerator<>(predicate.getVariables().stream().map(var -> var.getType().getInstances().toArray(new Constant[var.getType().getInstances().size()])).toArray(Constant[][]::new));
                for (Constant[] cs : cartesian_product) {
                    StateVariable state_variable = new StateVariable(predicate.getName() + "_" + Stream.of(cs).map(c -> c.getName()).collect(Collectors.joining("_")));
                    state_variable.addValue(new StateVariableValue(state_variable, "True"));
                    state_variable.addValue(new StateVariableValue(state_variable, "False"));
                    agent.addStateVariable(state_variable);
                }
            }
        });

        domain.getFunctions().values().stream().filter(function -> !static_functions.contains(function)).forEach(function -> {
            if (function.getVariables().isEmpty()) {
                StateVariable state_variable = new StateVariable(function.getName());
                function.getType().getInstances().forEach(instance -> {
                    state_variable.addValue(new StateVariableValue(state_variable, instance.getName()));
                });
                agent.addStateVariable(state_variable);
            } else {
                CartesianProductGenerator<Constant> cartesian_product = new CartesianProductGenerator<>(function.getVariables().stream().map(var -> var.getType().getInstances().toArray(new Constant[var.getType().getInstances().size()])).toArray(Constant[][]::new));
                for (Constant[] cs : cartesian_product) {
                    StateVariable state_variable = new StateVariable(function.getName() + "_" + Stream.of(cs).map(c -> c.getName()).collect(Collectors.joining("_")));
                    function.getType().getInstances().forEach(instance -> {
                        state_variable.addValue(new StateVariableValue(state_variable, instance.getName()));
                    });
                    agent.addStateVariable(state_variable);
                }
            }
        });

        domain.getActions().values().forEach(action -> {
            CartesianProductGenerator<Constant> cartesian_product = new CartesianProductGenerator<>(action.getVariables().stream().map(var -> var.getType().getInstances().toArray(new Constant[var.getType().getInstances().size()])).toArray(Constant[][]::new));
            for (Constant[] cs : cartesian_product) {
                assignments.clear();
                for (int i = 0; i < cs.length; i++) {
                    assignments.put(action.getVariables().get(i).getName(), cs[i]);
                }
                Action a = new Action(action.getName() + "_" + Stream.of(cs).map(c -> c.getName()).collect(Collectors.joining("_")));
                agent.addAction(a);
                env = a.getPrecondition();
                visitPrecondition(a, action.getPrecondition());
                env = a.getEffect();
                visitEffect(a, action.getEffect());
            }
        });

        domain.getDurativeActions().values().forEach(action -> {
            CartesianProductGenerator<Constant> cartesian_product = new CartesianProductGenerator<>(action.getVariables().stream().map(var -> var.getType().getInstances().toArray(new Constant[var.getType().getInstances().size()])).toArray(Constant[][]::new));
            for (Constant[] cs : cartesian_product) {
                assignments.clear();
                for (int i = 0; i < cs.length; i++) {
                    assignments.put(action.getVariables().get(i).getName(), cs[i]);
                }
                DurativeAction da = new DurativeAction(action.getName() + "_" + Stream.of(cs).map(c -> c.getName()).collect(Collectors.joining("_")));
                agent.addDurativeAction(da);
                env = da.getDuration();
                visitDuration(da, action.getDuration());
                env = da.getCondition();
                visitCondition(da, action.getCondition());
                env = da.getEffect();
                visitEffect(da, action.getEffect());
            }
        });

        ST translation = GROUP_FILE.getInstanceOf("Translation");
        translation.add("translator", this);

        return translation.render();
    }

    private String ground(Term term) {
        if (term instanceof VariableTerm) {
            return assignments.get(((VariableTerm) term).getVariable().getName()).getName();
        } else if (term instanceof ConstantTerm) {
            return ((ConstantTerm) term).getConstant().getName();
        } else if (term instanceof FunctionTerm) {
            if (static_functions.contains(((FunctionTerm) term))) {
                throw new UnsupportedOperationException(term.getClass().getName());
            } else {
                return ((FunctionTerm) term).getArguments().isEmpty() ? ((FunctionTerm) term).getFunction().getName() : ((FunctionTerm) term).getFunction().getName() + "_" + ((FunctionTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            }
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private void visitPrecondition(Action action, Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ((PredicateTerm) term).getArguments().isEmpty() ? ((PredicateTerm) term).getPredicate().getName() : ((PredicateTerm) term).getPredicate().getName() + "_" + ((PredicateTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            if (((PredicateTerm) term).isDirected()) {
                env.addEnv(agent.getStateVariable(predicate_name).getValue("True"));
            } else {
                env.addEnv(agent.getStateVariable(predicate_name).getValue("False"));
            }
        } else if (term instanceof EqTerm) {
            if (((EqTerm) term).getFirstTerm() instanceof FunctionTerm) {
                String function_name = ground(((EqTerm) term).getFirstTerm());
                env.addEnv(agent.getStateVariable(function_name).getValue(ground(((EqTerm) term).getSecondTerm())));
            } else {
                throw new UnsupportedOperationException(term.getClass().getName());
            }
        } else if (term instanceof AndTerm) {
            AND and = new AND(env);
            env.addEnv(and);
            env = and;
            ((AndTerm) term).getTerms().stream().forEach(t -> visitPrecondition(action, t));
            env = and.getEnclosingEnv();
        } else if (term instanceof OrTerm) {
            OR or = new OR(env);
            env.addEnv(or);
            env = or;
            ((OrTerm) term).getTerms().stream().forEach(t -> visitPrecondition(action, t));
            env = env.getEnclosingEnv();
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private void visitEffect(Action action, Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ((PredicateTerm) term).getArguments().isEmpty() ? ((PredicateTerm) term).getPredicate().getName() : ((PredicateTerm) term).getPredicate().getName() + "_" + ((PredicateTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            if (((PredicateTerm) term).isDirected()) {
                env.addEnv(agent.getStateVariable(predicate_name).getValue("True"));
            } else {
                env.addEnv(agent.getStateVariable(predicate_name).getValue("False"));
            }
        } else if (term instanceof AssignOpTerm) {
            String function_name = ground(((AssignOpTerm) term).getFunctionTerm());
            env.addEnv(agent.getStateVariable(function_name).getValue(ground(((AssignOpTerm) term).getValue())));
        } else if (term instanceof AndTerm) {
            AND and = new AND(env);
            env.addEnv(and);
            env = and;
            ((AndTerm) term).getTerms().stream().forEach(t -> visitEffect(action, t));
            env = and.getEnclosingEnv();
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private void visitDuration(DurativeAction durative_action, Term term) {
        throw new UnsupportedOperationException("Not supported yet..");
    }

    private void visitCondition(DurativeAction durative_action, Term term) {
        throw new UnsupportedOperationException("Not supported yet..");
    }

    private void visitEffect(DurativeAction durative_action, Term term) {
        throw new UnsupportedOperationException("Not supported yet..");
    }
}
