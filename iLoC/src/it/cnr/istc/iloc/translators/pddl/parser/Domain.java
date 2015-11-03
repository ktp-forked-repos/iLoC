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
package it.cnr.istc.iloc.translators.pddl.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Domain {

    private final String name;
    private final Map<String, Type> types = new LinkedHashMap<>();
    private final Map<String, Constant> constants = new LinkedHashMap<>();
    private final Map<String, Predicate> predicates = new LinkedHashMap<>();
    private final Map<String, Function> functions = new LinkedHashMap<>();
    private final Map<String, Action> actions = new LinkedHashMap<>();
    private final Map<String, DurativeAction> durative_actions = new LinkedHashMap<>();

    public Domain(String name) {
        this.name = name;
        addType(Type.OBJECT);
    }

    public String getName() {
        return name;
    }

    public Type getType(String name) {
        return types.get(name);
    }

    public Map<String, Type> getTypes() {
        return Collections.unmodifiableMap(types);
    }

    public void addType(Type type) {
        types.put(type.getName(), type);
    }

    public Constant getConstant(String name) {
        return constants.get(name);
    }

    public Map<String, Constant> getConstants() {
        return Collections.unmodifiableMap(constants);
    }

    public void addConstant(Constant constant) {
        constants.put(constant.getName(), constant);
    }

    public Predicate getPredicate(String name) {
        return predicates.get(name);
    }

    public Map<String, Predicate> getPredicates() {
        return Collections.unmodifiableMap(predicates);
    }

    public void addPredicate(Predicate predicate) {
        predicates.put(predicate.getName(), predicate);
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public Map<String, Function> getFunctions() {
        return Collections.unmodifiableMap(functions);
    }

    public void addFunction(Function function) {
        functions.put(function.getName(), function);
    }

    public Action getAction(String name) {
        return actions.get(name);
    }

    public Map<String, Action> getActions() {
        return Collections.unmodifiableMap(actions);
    }

    public void addAction(Action action) {
        actions.put(action.getName(), action);
    }

    public DurativeAction getDurativeAction(String name) {
        return durative_actions.get(name);
    }

    public Map<String, DurativeAction> getDurativeActions() {
        return Collections.unmodifiableMap(durative_actions);
    }

    public void addDurativeAction(DurativeAction durative_action) {
        durative_actions.put(durative_action.getName(), durative_action);
    }

    public Collection<Predicate> getStaticPredicates() {
        return predicates.values().stream().filter(predicate -> actions.values().stream().noneMatch(action -> containsPredicate(action.getEffect(), predicate)) && durative_actions.values().stream().noneMatch(action -> containsPredicate(action.getEffect(), predicate))).collect(Collectors.toSet());
    }

    public Collection<Function> getStaticFunctions() {
        return functions.values().stream().filter(function -> actions.values().stream().noneMatch(action -> containsFunction(action.getEffect(), function)) && durative_actions.values().stream().noneMatch(action -> containsFunction(action.getEffect(), function))).collect(Collectors.toSet());
    }

    public Collection<Invariant> getInvariants() {
        Set<Invariant> invariants = new HashSet<>();

        // We compute the initial invariant candidates..
        Collection<Predicate> staticPredicates = getStaticPredicates();
        Set<Invariant> candidates = predicates.values().stream().filter(predicate -> !staticPredicates.contains(predicate)).flatMap(predicate -> Stream.concat(Stream.of(new Invariant(new Variable[0], new Predicate[]{predicate})), predicate.getVariables().stream().map(variable -> new Invariant(new Variable[]{variable}, new Predicate[]{predicate})))).collect(Collectors.toSet());

        // We check for threats..
        while (!candidates.isEmpty()) {
            // We remove too heavy candidates..
            candidates.removeIf(invariant -> actions.values().stream().anyMatch(action -> countAddPredicates(action.getEffect(), invariant) > 1));

            Set<Invariant> refined_invariants = new HashSet<>();
            candidates.forEach(invariant -> {
                // We find a threatening action..
                Optional<Action> threatening_action = actions.values().stream().filter(action -> countAddPredicates(action.getEffect(), invariant) != countDelPredicates(action.getEffect(), invariant)).findAny();
                if (threatening_action.isPresent()) {
                    // We refine the invariant according to the found threatening action..
                    getInvariantRefinements(invariant, threatening_action.get().getEffect()).forEach(predicate -> {
                        // TODO: Invariant variables should be renamed according to the action..
                        List<Predicate> ps = new ArrayList<>(invariant.getPredicates());
                        ps.add(predicate);
                        refined_invariants.add(new Invariant(invariant.getVariables().stream().toArray(Variable[]::new), ps.stream().toArray(Predicate[]::new)));
                    });
                } else {
                    // We accept the invariant..
                    invariants.add(invariant);
                }
            });
            candidates.clear();
            candidates.addAll(refined_invariants);
        }
        return invariants;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(define (domain ").append(name).append(")\n");

        sb.append("(:types ").append(types.values().stream().map(type -> type.getName() + (type.getSuperclass() != null ? " - " + type.getSuperclass().getName() : "")).collect(Collectors.joining(" "))).append(")\n");

        if (!constants.isEmpty()) {
            sb.append("(:constants ").append(constants.values().stream().map(constant -> constant.getName() + " " + constant.getType().getName()).collect(Collectors.joining(" "))).append(")\n");
        }

        if (!predicates.isEmpty()) {
            sb.append("(:predicates ").append(predicates.values().stream().map(predicate -> predicate.toString()).collect(Collectors.joining(" "))).append(")\n");
        }

        if (!functions.isEmpty()) {
            sb.append("(:functions ").append(functions.values().stream().map(function -> function.toString()).collect(Collectors.joining(" "))).append(")\n");
        }

        actions.values().stream().forEach(action -> {
            sb.append(action).append("\n");
        });

        durative_actions.values().stream().forEach(action -> {
            sb.append(action).append("\n");
        });

        sb.append(")\n");
        return sb.toString();
    }

    private static boolean containsPredicate(Term term, Predicate predicate) {
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

    private static boolean containsFunction(Term term, Function function) {
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

    private static long countAddPredicates(Term term, Invariant invariant) {
        return getPredicateTerms(term).stream().filter(predicate_term -> invariant.getPredicates().contains(predicate_term.getPredicate()) && predicate_term.isDirected()).count();
    }

    private static long countDelPredicates(Term term, Invariant invariant) {
        return getPredicateTerms(term).stream().filter(predicate_term -> invariant.getPredicates().contains(predicate_term.getPredicate()) && !predicate_term.isDirected()).count();
    }

    private static Collection<Predicate> getInvariantRefinements(Invariant invariant, Term term) {
        return getPredicateTerms(term).stream().filter(predicate_term -> !invariant.getPredicates().contains(predicate_term.getPredicate()) && !predicate_term.isDirected()).map(predicate_term -> predicate_term.getPredicate()).collect(Collectors.toList());
    }

    private static Collection<PredicateTerm> getPredicateTerms(Term term) {
        if (term instanceof PredicateTerm) {
            return Arrays.asList(((PredicateTerm) term));
        } else if (term instanceof FunctionTerm) {
            return Collections.emptyList();
        } else if (term instanceof AndTerm) {
            return ((AndTerm) term).getTerms().stream().flatMap(t -> getPredicateTerms(t).stream()).collect(Collectors.toList());
        } else if (term instanceof AssignOpTerm) {
            return Collections.emptyList();
        } else if (term instanceof VariableTerm) {
            return Collections.emptyList();
        } else if (term instanceof ConstantTerm) {
            return Collections.emptyList();
        } else {
            throw new UnsupportedOperationException(term.getClass().getName());
        }
    }
}
