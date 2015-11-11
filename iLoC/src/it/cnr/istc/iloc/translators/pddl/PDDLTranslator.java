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
import it.cnr.istc.iloc.utils.CombinationGenerator;
import it.cnr.istc.iloc.utils.Dijkstra;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
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
    private final Domain domain;
    private final Problem problem;
    private Agent agent;
    private Collection<Predicate> static_predicates;
    private Collection<Function> static_functions;
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
        static_predicates = domain.getStaticPredicates();
        static_functions = domain.getStaticFunctions();

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
                env = a.getPrecondition();
                visitPrecondition(a, action.getPrecondition());
                a.getPrecondition().simplify();
                env = a.getEffect();
                visitEffect(a, action.getEffect());
                a.getEffect().simplify();
                if (a.getPrecondition().isConsistent() && a.getEffect().isConsistent()) {
                    agent.addAction(a);
                } else {
                    getTerms(a.getEffect()).forEach(term -> {
                        term.removeAction(a);
                    });
                }
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
                env = da.getDuration();
                visitDuration(da, action.getDuration());
                da.getDuration().simplify();
                env = da.getCondition();
                visitCondition(da, action.getCondition());
                da.getCondition().simplify();
                env = da.getEffect();
                visitEffect(da, action.getEffect());
                da.getEffect().simplify();
                if (da.getCondition().isConsistent()) {
                    agent.addDurativeAction(da);
                }
            }
        });

        visitGoal(problem.getGoal());

//        clear();
        Map<Object, Double> table = new LinkedHashMap<>();
        StateVariableValue[] values = agent.getStateVariables().values().stream().flatMap(sv -> sv.getValues().values().stream()).filter(value -> value.getName().equals("True")).toArray(StateVariableValue[]::new);
        List<StateVariableValue> init_terms = getTerms(init);
        for (StateVariableValue value : values) {
            if (init_terms.contains(value)) {
                table.put(value, 0d);
            }
        }
        for (StateVariableValue[] vs : new CombinationGenerator<>(2, values)) {
            if (init_terms.contains(vs[0]) && init_terms.contains(vs[1])) {
                table.put(new HashSet<>(Arrays.asList(vs[0], vs[1])), 0d);
            }
        }

        //TODO: Compute costs starting from what is known..
        double h_2 = computeH2Cost(new HashSet<>(getTerms(goal)), table);
        table.forEach((value, cost) -> System.out.println(value + " = " + cost));

        agent.getStateVariables().values().stream().flatMap(sv -> sv.getValues().values().stream()).filter(value -> !table.containsKey(value) || table.get(value) == Double.POSITIVE_INFINITY).forEach(unreachable -> {
            Collection<Action> a_to_remove = new ArrayList<>(unreachable.getActions());
            a_to_remove.forEach(a -> unreachable.removeAction(a));
            Collection<DurativeAction> da_at_start_to_remove = new ArrayList<>(unreachable.getAtStartDurativeActions());
            da_at_start_to_remove.forEach(a -> unreachable.removeAtStartDurativeAction(a));
            Collection<DurativeAction> da_at_end_to_remove = new ArrayList<>(unreachable.getAtEndDurativeActions());
            da_at_end_to_remove.forEach(a -> unreachable.addAtEndDurativeAction(a));
        });

        clear();

        ST translation = GROUP_FILE.getInstanceOf("Translation");
        translation.add("translator", this);

        return translation.render();
    }

    private void clear() {
        while (true) {
            // We collect values which are not effects of any action..
            List<StateVariableValue> v_to_remove = agent.getStateVariables().values().stream().flatMap(sv -> sv.getValues().values().stream().filter(v -> v.isLeaf())).collect(Collectors.toList());
            if (v_to_remove.isEmpty()) {
                break;
            }

            v_to_remove.forEach(v -> {
                // We remove these values from their state variables..
                v.getStateVariable().removeValue(v);
                if (v.getStateVariable().getValues().isEmpty()) {
                    // If the state variable has no allowed values, we can remove it definitely..
                    agent.removeStateVariable(v.getStateVariable());
                }
            });

            List<Action> a_to_remove = new ArrayList<>();
            agent.getActions().values().forEach(action -> {
                // We remove these values from the action precondition..
                v_to_remove.forEach(v -> {
                    removeValue(action.getPrecondition(), v);
                });
                // .. we perform some simplification ..
                action.getPrecondition().simplify();
                if (!action.getPrecondition().isConsistent()) {
                    // .. and if the action becomes impossible, we remove the action..
                    a_to_remove.add(action);
                }
            });
            a_to_remove.forEach(a -> {
                agent.removeAction(a);
                // We remove the removable actions from the values, possible creating new impossible values..
                getTerms(a.getEffect()).forEach(t -> t.removeAction(a));
            });

            List<DurativeAction> da_to_remove = new ArrayList<>();
            agent.getDurativeActions().values().forEach(durative_action -> {
                // We remove these values from the action condition..
                v_to_remove.forEach(v -> {
                    removeValue(durative_action.getCondition(), v);
                });
                // .. we perform some simplification ..
                durative_action.getCondition().simplify();
                if (!durative_action.getCondition().isConsistent()) {
                    // .. and if the action becomes impossible, we remove the action..
                    da_to_remove.add(durative_action);
                }
            });
            da_to_remove.forEach(da -> {
                agent.removeDurativeAction(da);
                // We remove the removable durative actions from the values, possible creating new impossible values..
                getTerms(da.getEffect()).forEach(t -> {
                    t.removeAtStartDurativeAction(da);
                    t.removeAtEndDurativeAction(da);
                });
            });
        }
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
                if (((PredicateTerm) term).isDirected() && predicate_name.equals("False")) {
                    // We have a precondition which is never true..
                    // We can simplify the disjunction or even remove the action..
                    env.setConsistent(false);
                } else if (!((PredicateTerm) term).isDirected() && predicate_name.equals("True")) {
                    // We have a precondition which is never true..
                    // We can simplify the disjunction or even remove the action..
                    env.setConsistent(false);
                }
            } else if (((PredicateTerm) term).isDirected()) {
                env.addEnv(new Precondition(action, agent.getStateVariable(predicate_name).getValue("True")));
            } else {
                env.addEnv(new Precondition(action, agent.getStateVariable(predicate_name).getValue("False")));
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
            ((AndTerm) term).getTerms().forEach(t -> visitPrecondition(action, t));
            env = and.getEnclosingEnv();
        } else if (term instanceof OrTerm) {
            Or or = new Or(env);
            env.addEnv(or);
            env = or;
            ((OrTerm) term).getTerms().forEach(t -> visitPrecondition(action, t));
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
                if (agent.getStateVariable(predicate_name).getValue("False").getActions().contains(action)) {
                    env.setConsistent(false);
                }
            } else {
                StateVariableValue value = agent.getStateVariable(predicate_name).getValue("False");
                value.addAction(action);
                env.addEnv(new Effect(action, value));
                if (agent.getStateVariable(predicate_name).getValue("True").getActions().contains(action)) {
                    env.setConsistent(false);
                }
            }
        } else if (term instanceof AssignOpTerm) {
            String function_name = ground(((AssignOpTerm) term).getFunctionTerm());
            StateVariableValue value = agent.getStateVariable(function_name).getValue(ground(((AssignOpTerm) term).getValue()));
            value.addAction(action);
            env.addEnv(new Effect(action, value));
            if (agent.getStateVariable(function_name).getValues().values().stream().filter(v -> v != value).anyMatch(v -> v.getActions().contains(action))) {
                env.setConsistent(false);
            }
        } else if (term instanceof AndTerm) {
            And and = new And(env);
            env.addEnv(and);
            env = and;
            ((AndTerm) term).getTerms().forEach(t -> visitEffect(action, t));
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
            } else if (((PredicateTerm) term).isDirected()) {
                init.addEnv(new InitEl(agent.getStateVariable(predicate_name).getValue("True")));
            } else {
                init.addEnv(new InitEl(agent.getStateVariable(predicate_name).getValue("False")));
            }
        } else if (term instanceof EqTerm) {
            if (((EqTerm) term).getFirstTerm() instanceof FunctionTerm) {
                String function_name = ((FunctionTerm) ((EqTerm) term).getFirstTerm()).getArguments().isEmpty() ? ((FunctionTerm) ((EqTerm) term).getFirstTerm()).getFunction().getName() : ((FunctionTerm) ((EqTerm) term).getFirstTerm()).getFunction().getName() + "_" + ((FunctionTerm) ((EqTerm) term).getFirstTerm()).getArguments().stream().map(a -> ((ConstantTerm) a).getConstant().getName()).collect(Collectors.joining("_"));
                if (static_functions.contains(((FunctionTerm) ((EqTerm) term).getFirstTerm()).getFunction())) {
                    // We are instantiating a static function..
                    if (((EqTerm) term).getSecondTerm() instanceof ConstantTerm) {
                        // We are instantiating a static multi-valued function..
                        static_assignments.put(function_name, ((ConstantTerm) ((EqTerm) term).getSecondTerm()).getConstant().getName());
                    } else {
                        // We are instantiating a static numeric function..
                        static_assignments.put(function_name, ((NumberTerm) ((EqTerm) term).getSecondTerm()).getValue().toString());
                    }
                } else if (((EqTerm) term).getSecondTerm() instanceof ConstantTerm) {
                    init.addEnv(new InitEl(agent.getStateVariable(function_name).getValue(((ConstantTerm) ((EqTerm) term).getSecondTerm()).getConstant().getName())));
                } else {
                    init.addEnv(new InitEl(agent.getStateVariable(function_name).getValue(((NumberTerm) ((EqTerm) term).getSecondTerm()).getValue().toString())));
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
            ((AndTerm) term).getTerms().forEach(t -> visitGoal(t));
            goal = and.getEnclosingEnv();
        } else if (term instanceof OrTerm) {
            Or or = new Or(goal);
            goal.addEnv(or);
            goal = or;
            ((OrTerm) term).getTerms().forEach(t -> visitGoal(t));
            goal = goal.getEnclosingEnv();
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }

    private void removeValue(Env env, StateVariableValue value) {
        if (env instanceof Precondition) {
            if (((Precondition) env).getValue() == value) {
                env.setConsistent(false);
            }
        } else if (env instanceof And) {
            ((And) env).getEnvs().forEach(e -> removeValue(e, value));
        } else if (env instanceof Or) {
            ((Or) env).getEnvs().forEach(e -> removeValue(e, value));
        } else {
            throw new UnsupportedOperationException("Not supported yet..");
        }
    }

    private Dijkstra<StateVariableValue> computeH1Costs() {
        Dijkstra<StateVariableValue> dijkstra = new Dijkstra<>();

        StateVariableValue[] values = agent.getStateVariables().values().stream().flatMap(sv -> sv.getValues().values().stream()).filter(value -> value.getName().equals("True")).toArray(StateVariableValue[]::new);
        List<StateVariableValue> init_terms = getTerms(init);
        for (StateVariableValue value : values) {
            dijkstra.addVertex(value, init_terms.contains(value) ? 0 : Double.POSITIVE_INFINITY);
        }

        for (StateVariableValue value : values) {
            value.getActions().forEach(action -> {
                List<StateVariableValue> terms = getTerms(action.getPrecondition());
                terms.forEach(term -> dijkstra.addEdge(term, value, 1));
            });
        }

        init_terms.stream().forEach(init_term -> {
            dijkstra.dijkstra(init_term);
        });

        return dijkstra;
    }

    private Dijkstra<Object> computeH2Costs() {
        Dijkstra<Object> dijkstra = new Dijkstra<>();

        StateVariableValue[] values = agent.getStateVariables().values().stream().flatMap(sv -> sv.getValues().values().stream()).filter(value -> value.getName().equals("True")).toArray(StateVariableValue[]::new);
        List<StateVariableValue> init_terms = getTerms(init);
        for (StateVariableValue value : values) {
            dijkstra.addVertex(value, init_terms.contains(value) ? 0 : Double.POSITIVE_INFINITY);
        }
        for (StateVariableValue[] vs : new CombinationGenerator<>(2, values)) {
            HashSet<StateVariableValue> pair = new HashSet<>(Arrays.asList(vs[0], vs[1]));
            dijkstra.addVertex(pair, (init_terms.contains(vs[0]) && init_terms.contains(vs[1])) ? 0 : Double.POSITIVE_INFINITY);
        }

        for (StateVariableValue value : values) {
            value.getActions().forEach(action -> {
                List<StateVariableValue> terms = getTerms(action.getPrecondition());
                if (terms.size() == 1) {
                    dijkstra.addEdge(terms.get(0), value, 1);
                } else {
                    for (StateVariableValue[] vs : new CombinationGenerator<>(2, terms.stream().toArray(StateVariableValue[]::new))) {
                        dijkstra.addEdge(new HashSet<>(Arrays.asList(vs[0], vs[1])), value, 1);
                    }
                }
            });
        }
        for (StateVariableValue[] vs : new CombinationGenerator<>(2, values)) {
            HashSet<StateVariableValue> pair = new HashSet<>(Arrays.asList(vs[0], vs[1]));
            dijkstra.addEdge(vs[0], pair, 0);
            dijkstra.addEdge(vs[1], pair, 0);
//            dijkstra.addEdge(pair, vs[0], 0);
//            dijkstra.addEdge(pair, vs[1], 0);
            // The actions that generate both the values..
            vs[0].getActions().stream().filter(t -> vs[1].getActions().contains(t)).forEach(action -> {
                List<StateVariableValue> terms = getTerms(action.getPrecondition());
                if (terms.size() == 1) {
                    dijkstra.addEdge(terms.get(0), pair, 1);
                } else {
                    for (StateVariableValue[] c_vs : new CombinationGenerator<>(2, terms.stream().toArray(StateVariableValue[]::new))) {
                        dijkstra.addEdge(new HashSet<>(Arrays.asList(c_vs[0], c_vs[1])), pair, 1);
                    }
                }
            });
            // The actions that generate the first value but not the second..
            vs[0].getActions().stream().filter(t -> !vs[1].getStateVariable().getValues().values().stream().filter(v -> v != vs[1]).anyMatch(v -> v.getActions().contains(t))).forEach(action -> {
                List<StateVariableValue> preconditions = getTerms(action.getPrecondition()).stream().filter(term -> term != vs[1]).collect(Collectors.toList());
                if (preconditions.isEmpty()) {
                    dijkstra.addEdge(vs[1], pair, 1);
                } else {
                    preconditions.forEach(term -> {
                        dijkstra.addEdge(new HashSet<>(Arrays.asList(term, vs[1])), pair, 1);
                    });
                }
            });
            // The actions that generate the second value but not the first..
            vs[1].getActions().stream().filter(t -> !vs[0].getStateVariable().getValues().values().stream().filter(v -> v != vs[0]).anyMatch(v -> v.getActions().contains(t))).forEach(action -> {
                List<StateVariableValue> preconditions = getTerms(action.getPrecondition()).stream().filter(term -> term != vs[0]).collect(Collectors.toList());
                if (preconditions.isEmpty()) {
                    dijkstra.addEdge(vs[0], pair, 1);
                } else {
                    preconditions.forEach(term -> {
                        dijkstra.addEdge(new HashSet<>(Arrays.asList(term, vs[0])), pair, 1);
                    });
                }
            });
        }

        init_terms.stream().forEach(init_term -> {
            dijkstra.dijkstra(init_term);
        });

        return dijkstra;
    }

    private static double computeH2Cost(Set<StateVariableValue> values, Map<Object, Double> table) {
        assert !values.isEmpty();
        double v = 0;
        StateVariableValue[] c_values = values.stream().toArray(StateVariableValue[]::new);
        for (int i = 0; i < c_values.length; i++) {
            v = Math.max(v, computeH2Cost(c_values[i], table));
            for (int j = i + 1; j < c_values.length; j++) {
                v = Double.max(v, computeH2Cost(c_values[i], c_values[j], table));
            }
        }
        return v;
    }

    private static double computeH2Cost(StateVariableValue value, Map<Object, Double> table) {
        if (table.containsKey(value)) {
            return table.get(value);
        } else {
            OptionalDouble min = value.getActions().stream().mapToDouble(action -> computeH2Cost(new HashSet<>(getTerms(action.getPrecondition())), table)).min();
            double c_cost = 1 + min.orElse(Double.POSITIVE_INFINITY);
            table.put(value, c_cost);
            System.out.println(value + " = " + c_cost);
            return c_cost;
        }
    }

    private static double computeH2Cost(StateVariableValue v0, StateVariableValue v1, Map<Object, Double> table) {
        HashSet<StateVariableValue> pair = new HashSet<>(Arrays.asList(v0, v1));
        if (table.containsKey(pair)) {
            return table.get(pair);
        } else {
            // The actions that generate both the values..
            double v1_v2 = 1 + v0.getActions().stream().filter(t -> v1.getActions().contains(t)).mapToDouble(action -> computeH2Cost(new HashSet<>(getTerms(action.getPrecondition())), table)).min().orElse(Double.POSITIVE_INFINITY);
            // The actions that generate the first value but not the second..
            double v1_not_v2 = 1 + v0.getActions().stream().filter(t -> !v1.getStateVariable().getValues().values().stream().filter(value -> value != v1).anyMatch(value -> value.getActions().contains(t))).mapToDouble(action -> computeH2Cost(Stream.concat(getTerms(action.getPrecondition()).stream(), Stream.of(v1)).collect(Collectors.toSet()), table)).min().orElse(Double.POSITIVE_INFINITY);
            // The actions that generate the second value but not the first..
            double not_v1_v2 = 1 + v1.getActions().stream().filter(t -> !v0.getStateVariable().getValues().values().stream().filter(value -> value != v0).anyMatch(value -> value.getActions().contains(t))).mapToDouble(action -> computeH2Cost(Stream.concat(getTerms(action.getPrecondition()).stream(), Stream.of(v0)).collect(Collectors.toSet()), table)).min().orElse(Double.POSITIVE_INFINITY);

            double c_cost = Double.POSITIVE_INFINITY;
            c_cost = Double.min(c_cost, v1_v2);
            c_cost = Double.min(c_cost, v1_not_v2);
            c_cost = Double.min(c_cost, not_v1_v2);
            table.put(pair, c_cost);
            System.out.println(pair + " = " + c_cost);
            return c_cost;
        }
    }

    private static List<StateVariableValue> getTerms(Env env) {
        if (env instanceof Precondition) {
            return Arrays.asList(((Precondition) env).getValue());
        } else if (env instanceof Effect) {
            return Arrays.asList(((Effect) env).getValue());
        } else if (env instanceof InitEl) {
            return Arrays.asList(((InitEl) env).getValue());
        } else if (env instanceof Goal) {
            return Arrays.asList(((Goal) env).getValue());
        } else if (env instanceof And) {
            return ((And) env).getEnvs().stream().flatMap(e -> getTerms(e).stream()).collect(Collectors.toList());
        } else {
            throw new UnsupportedOperationException("Not supported yet.." + env.getClass().getName());
        }
    }
}
