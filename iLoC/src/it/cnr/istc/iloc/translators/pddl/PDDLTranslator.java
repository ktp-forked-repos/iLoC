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
import it.cnr.istc.iloc.translators.pddl.parser.NumberTerm;
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
        GROUP_FILE.registerRenderer(StateVariableValue.class, new StateVariableValueRenderer(GROUP_FILE));
    }

    public static String translate(Domain domain, Problem problem) {
        return new PDDLTranslator(domain, problem).translate();
    }

    public static String translate(File pddl_domain, File pddl_problem) throws IOException {
        PDDLInstance instance = Parser.parse(pddl_domain, pddl_problem);
        return new PDDLTranslator(instance.getDomain(), instance.getProblem()).translate();
    }

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
        if (term instanceof PredicateTerm) {
            return false;
        } else if (term instanceof FunctionTerm) {
            return ((FunctionTerm) term).getFunction() == function;
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
    private final Domain domain;
    private final Problem problem;
    private Agent agent;
    private Set<Predicate> static_predicates;
    private Set<Function> static_functions;
    private final Map<String, String> static_assignments = new HashMap<>();
    private final Map<String, Constant> assignments = new HashMap<>();
    private Env env;
    private And init = new And(null);
    private Env goal = new And(null);

    private PDDLTranslator(Domain domain, Problem problem) {
        this.domain = domain;
        this.problem = problem;
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

    public And getInit() {
        return init;
    }

    public Env getGoal() {
        return goal;
    }

    public String translate() {
        agent = new Agent(domain.getName() + "Agent");

        // Check for static predicates and functions..
        static_predicates = domain.getPredicates().values().stream().filter(predicate -> domain.getActions().values().stream().noneMatch(action -> containsPredicate(action.getEffect(), predicate)) && domain.getDurativeActions().values().stream().noneMatch(action -> containsPredicate(action.getEffect(), predicate))).collect(Collectors.toSet());
        static_functions = domain.getFunctions().values().stream().filter(function -> domain.getActions().values().stream().noneMatch(action -> containsFunction(action.getEffect(), function)) && domain.getDurativeActions().values().stream().noneMatch(action -> containsFunction(action.getEffect(), function))).collect(Collectors.toSet());

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

        // we set the initial state for instantiating static predicates..
        assignments.clear();
        assignments.putAll(domain.getConstants());
        assignments.putAll(problem.getObjects());
        problem.getInitEls().forEach(init_el -> {
            visitInitEl(init_el);
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

        visitGoal(problem.getGoal());

        ST translation = GROUP_FILE.getInstanceOf("Translation");
        translation.add("translator", this);

        return translation.render();
    }

    private String ground(Term term) {
        if (term instanceof VariableTerm) {
            return assignments.get(((VariableTerm) term).getVariable().getName()).getName();
        } else if (term instanceof ConstantTerm) {
            return ((ConstantTerm) term).getConstant().getName();
        } else if (term instanceof PredicateTerm) {
            String predicate_name = ((PredicateTerm) term).getArguments().isEmpty() ? ((PredicateTerm) term).getPredicate().getName() : ((PredicateTerm) term).getPredicate().getName() + "_" + ((PredicateTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            if (static_predicates.contains(((PredicateTerm) term).getPredicate())) {
                // We have a static predicate..
                if (static_assignments.containsKey(predicate_name)) {
                    // The static predicate is true in the initial state..
                    return "True";
                } else {
                    // The static predicate is false in the initial state..
                    return "False";
                }
            } else {
                return predicate_name;
            }
        } else if (term instanceof FunctionTerm) {
            String function_name = ((FunctionTerm) term).getArguments().isEmpty() ? ((FunctionTerm) term).getFunction().getName() : ((FunctionTerm) term).getFunction().getName() + "_" + ((FunctionTerm) term).getArguments().stream().map(a -> ground(a)).collect(Collectors.joining("_"));
            if (static_functions.contains(((FunctionTerm) term).getFunction())) {
                // We have a static function..
                assert static_assignments.containsKey(function_name);
                return static_assignments.get(function_name);
            } else {
                return function_name;
            }
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private void visitPrecondition(Action action, Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ground(term);
            if (static_predicates.contains(((PredicateTerm) term).getPredicate())) {
                if (predicate_name.equals("False")) {
                    // We have a precondition which is never true..
                    // We can simplify the disjunction or even remove the avtion..
                    throw new UnsupportedOperationException("Not supported yet..");
                }
            } else {
                if (((PredicateTerm) term).isDirected()) {
                    env.addEnv(new Precondition(action, agent.getStateVariable(predicate_name).getValue("True")));
                } else {
                    env.addEnv(new Precondition(action, agent.getStateVariable(predicate_name).getValue("False")));
                }
            }
        } else if (term instanceof EqTerm) {
            if (((EqTerm) term).getFirstTerm() instanceof FunctionTerm) {
                String function_name = ground(((EqTerm) term).getFirstTerm());
                env.addEnv(new Precondition(action, agent.getStateVariable(function_name).getValue(ground(((EqTerm) term).getSecondTerm()))));
            } else {
                throw new UnsupportedOperationException(term.getClass().getName());
            }
        } else if (term instanceof AndTerm) {
            And and = new And(env);
            env.addEnv(and);
            env = and;
            ((AndTerm) term).getTerms().stream().forEach(t -> visitPrecondition(action, t));
            env = and.getEnclosingEnv();
        } else if (term instanceof OrTerm) {
            Or or = new Or(env);
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
            String predicate_name = ground(term);
            if (((PredicateTerm) term).isDirected()) {
                StateVariableValue value = agent.getStateVariable(predicate_name).getValue("True");
                value.addAction(action);
                env.addEnv(new Effect(action, value));
            } else {
                StateVariableValue value = agent.getStateVariable(predicate_name).getValue("False");
                value.addAction(action);
                env.addEnv(new Effect(action, value));
            }
        } else if (term instanceof AssignOpTerm) {
            String function_name = ground(((AssignOpTerm) term).getFunctionTerm());
            StateVariableValue value = agent.getStateVariable(function_name).getValue(ground(((AssignOpTerm) term).getValue()));
            value.addAction(action);
            env.addEnv(new Effect(action, value));
        } else if (term instanceof AndTerm) {
            And and = new And(env);
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

    private void visitInitEl(Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ((PredicateTerm) term).getArguments().isEmpty() ? ((PredicateTerm) term).getPredicate().getName() : ((PredicateTerm) term).getPredicate().getName() + "_" + ((PredicateTerm) term).getArguments().stream().map(a -> ((ConstantTerm) a).getConstant().getName()).collect(Collectors.joining("_"));
            if (static_predicates.contains(((PredicateTerm) term).getPredicate())) {
                // We are instantiating a static predicate..
                if (((PredicateTerm) term).isDirected()) {
                    static_assignments.put(predicate_name, "True");
                } else {
                    static_assignments.put(predicate_name, "False");
                }
            } else {
                if (((PredicateTerm) term).isDirected()) {
                    init.addEnv(new InitEl(agent.getStateVariable(predicate_name).getValue("True")));
                } else {
                    init.addEnv(new InitEl(agent.getStateVariable(predicate_name).getValue("False")));
                }
            }
        } else if (term instanceof EqTerm) {
            if (((EqTerm) term).getFirstTerm() instanceof FunctionTerm) {
                String function_name = ((FunctionTerm) term).getArguments().isEmpty() ? ((FunctionTerm) term).getFunction().getName() : ((FunctionTerm) term).getFunction().getName() + "_" + ((FunctionTerm) term).getArguments().stream().map(a -> ((ConstantTerm) a).getConstant().getName()).collect(Collectors.joining("_"));
                if (static_functions.contains(((FunctionTerm) ((EqTerm) term).getFirstTerm()).getFunction())) {
                    // We are instantiating a static function..
                    if (((EqTerm) term).getSecondTerm() instanceof ConstantTerm) {
                        static_assignments.put(function_name, ((ConstantTerm) ((EqTerm) term).getSecondTerm()).getConstant().getName());
                    } else {
                        static_assignments.put(function_name, ((NumberTerm) ((EqTerm) term).getSecondTerm()).getValue().toString());
                    }
                } else {
                    if (((EqTerm) term).getSecondTerm() instanceof ConstantTerm) {
                        init.addEnv(new InitEl(agent.getStateVariable(function_name).getValue(((ConstantTerm) ((EqTerm) term).getSecondTerm()).getConstant().getName())));
                    } else {
                        init.addEnv(new InitEl(agent.getStateVariable(function_name).getValue(((NumberTerm) ((EqTerm) term).getSecondTerm()).getValue().toString())));
                    }
                }
            } else {
                throw new UnsupportedOperationException(term.getClass().getName());
            }
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private void visitGoal(Term term) {
        if (term instanceof PredicateTerm) {
            String predicate_name = ground(term);
            if (((PredicateTerm) term).isDirected()) {
                goal.addEnv(new Goal(agent.getStateVariable(predicate_name).getValue("True")));
            } else {
                goal.addEnv(new Goal(agent.getStateVariable(predicate_name).getValue("False")));
            }
        } else if (term instanceof EqTerm) {
            if (((EqTerm) term).getFirstTerm() instanceof FunctionTerm) {
                String function_name = ground(((EqTerm) term).getFirstTerm());
                goal.addEnv(new Goal(agent.getStateVariable(function_name).getValue(ground(((EqTerm) term).getSecondTerm()))));
            } else {
                throw new UnsupportedOperationException(term.getClass().getName());
            }
        } else if (term instanceof AndTerm) {
            And and = new And(goal);
            goal.addEnv(and);
            goal = and;
            ((AndTerm) term).getTerms().stream().forEach(t -> visitGoal(t));
            goal = and.getEnclosingEnv();
        } else if (term instanceof OrTerm) {
            Or or = new Or(goal);
            goal.addEnv(or);
            goal = or;
            ((OrTerm) term).getTerms().stream().forEach(t -> visitGoal(t));
            goal = goal.getEnclosingEnv();
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }
}
