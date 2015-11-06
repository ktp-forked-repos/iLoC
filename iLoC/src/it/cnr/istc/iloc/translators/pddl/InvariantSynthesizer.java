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
import it.cnr.istc.iloc.translators.pddl.parser.ConstantTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Domain;
import it.cnr.istc.iloc.translators.pddl.parser.FunctionTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Predicate;
import it.cnr.istc.iloc.translators.pddl.parser.PredicateTerm;
import it.cnr.istc.iloc.translators.pddl.parser.Term;
import it.cnr.istc.iloc.translators.pddl.parser.Variable;
import it.cnr.istc.iloc.translators.pddl.parser.VariableTerm;
import it.cnr.istc.iloc.utils.CombinationGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class InvariantSynthesizer {

    public static Collection<Invariant> getInvariants(Domain domain) {
        return new InvariantSynthesizer(domain).synthesizeInvariants();
    }

    /**
     * Computes the number of add predicates in the given term considered by the
     * invariant.
     *
     * @param term the term to be checked.
     * @param invariant the invariant considered for counting.
     * @return the number of add predicates in the given term considered by the
     * environment.
     */
    private static long countAddPredicates(Term term, Invariant invariant) {
        long count = 0;
        Collection<PredicateTerm> terms = getPredicateTerms(term);
        for (Atom atom : invariant.getAtoms()) {
            Optional<PredicateTerm> predicate = terms.stream().filter(t -> t.getPredicate() == atom.getPredicate() && t.isDirected()).findAny();
            if (predicate.isPresent()) {
                // We change the names of the variables..
                for (int i = 0; i < atom.getPredicate().getVariables().size(); i++) {
                    atom.getVariables().get(i).setName(((VariableTerm) predicate.get().getArguments().get(i)).getVariable().getName());
                }
                count++;
            }
        }
        return count;
    }

    /**
     * Computes the number of del predicates in the given term not considered by
     * the invariant.
     *
     * @param term the term to be checked.
     * @param invariant the invariant considered for counting.
     * @return the number of add predicates in the given term not considered by
     * the environment.
     */
    private static long countDelPredicates(Term term, Invariant invariant) {
        Collection<PredicateTerm> terms = getPredicateTerms(term);
        return invariant.getAtoms().stream().mapToLong(atom -> terms.stream().filter(t -> t.getPredicate() == atom.getPredicate() && !t.isDirected()).count()).sum();
    }

    /**
     * Returns {@code true} if the given invariant is threatened by the given
     * action.
     *
     * @param action the action which might threat the invariant.
     * @param invariant the invariant which might be threathened by the action.
     * @return {@code true} if the given invariant is threatened by the given
     * action.
     */
    private boolean isThreatened(it.cnr.istc.iloc.translators.pddl.parser.Action action, Invariant invariant) {
        return countAddPredicates(action.getEffect(), invariant) > countDelPredicates(action.getEffect(), invariant);
    }

    private static Collection<Invariant> refine(Invariant invariant, Term term) {
        Collection<Invariant> refinements = new ArrayList<>();
        Collection<PredicateTerm> terms = getPredicateTerms(term);
        for (PredicateTerm t : terms) {
            if (t.isDirected()) {
                // Invariants can be refined only by negated predicates..
                continue;
            }
            // We check if predicate represented by "t" is already covered by the invariant..
            boolean covered = false;
            for (Atom atom : invariant.getAtoms()) {
                if (t.getPredicate() == atom.getPredicate()) {
                    // We belive it is covered, since "t" represents a predicate of the environment, but we want to check also the parameters..
                    covered = true;
                    for (int i = 0; i < atom.getVariables().size(); i++) {
                        if (!((VariableTerm) t.getArguments().get(i)).getVariable().getName().equals(atom.getVariables().get(i).getName())) {
                            covered = false;
                            break;
                        }
                    }
                }
            }
            if (!covered) {
                // Since the predicate is not covered, we refine the environment..
                Invariant refined = copy(invariant);
                refined.addAtom(new Atom(t.getPredicate(), t.getPredicate().getVariables().stream().map(var -> refined.getParameters().containsKey(var.getName()) ? refined.getParameters().get(var.getName()) : new Parameter(var.getName(), var.getType())).toArray(Parameter[]::new)));
                refinements.add(refined);
            }
        }
        return refinements;
    }

    private static Invariant copy(Invariant invariant) {
        Invariant inv = new Invariant();
        invariant.getParameters().values().forEach(parameter -> {
            Parameter par = new Parameter(parameter.getName(), parameter.getType());
            inv.addParameter(par);
        });
        invariant.getAtoms().forEach(atom -> {
            inv.addAtom(new Atom(atom.getPredicate(), atom.getVariables().stream().map(var -> inv.getParameters().containsKey(var.getName()) ? inv.getParameters().get(var.getName()) : new Parameter(var.getName(), var.getType())).toArray(Parameter[]::new)));
        });
        return inv;
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
    private final Domain domain;

    private InvariantSynthesizer(Domain domain) {
        this.domain = domain;
    }

    private Collection<Invariant> synthesizeInvariants() {
        Set<Invariant> invariants = new HashSet<>();

        // We compute the initial invariant candidates..
        Collection<Predicate> staticPredicates = domain.getStaticPredicates();
        Collection<Invariant> candidates = new ArrayList<>();
        domain.getPredicates().values().stream().filter(predicate -> !staticPredicates.contains(predicate)).forEach(predicate -> {
            if (predicate.getVariables().size() <= 1) {
                // We are interested in candidates with at most one counted variable..
                Invariant inv = new Invariant();
                inv.addAtom(new Atom(predicate, predicate.getVariables().stream().map(var -> new Parameter(var.getName(), var.getType())).toArray(Parameter[]::new)));
                candidates.add(inv);
            }
            for (int i = 1; i <= predicate.getVariables().size(); i++) {
                if (predicate.getVariables().size() - i <= 1) {
                    // We are interested in candidates with at most one counted variable..
                    for (Variable[] vs : new CombinationGenerator<>(i, predicate.getVariables().stream().toArray(Variable[]::new))) {
                        Invariant inv = new Invariant();
                        Map<String, Parameter> pars = new HashMap<>();
                        for (Variable var : vs) {
                            Parameter par = new Parameter(var.getName(), var.getType());
                            pars.put(par.getName(), par);
                            inv.addParameter(par);
                        }
                        inv.addAtom(new Atom(predicate, predicate.getVariables().stream().map(var -> pars.containsKey(var.getName()) ? pars.get(var.getName()) : new Parameter(var.getName(), var.getType())).toArray(Parameter[]::new)));
                        candidates.add(inv);
                    }
                }
            }
        });

        // We check for threats..
        while (!candidates.isEmpty()) {
            // We remove too heavy candidates..
            candidates.removeIf(invariant -> domain.getActions().values().stream().anyMatch(action -> countAddPredicates(action.getEffect(), invariant) > 1));

            Set<Invariant> refined_invariants = new HashSet<>();
            candidates.forEach(invariant -> {
                // We find a threatening action..
                Optional<it.cnr.istc.iloc.translators.pddl.parser.Action> threatening_action = domain.getActions().values().stream().filter(action -> isThreatened(action, invariant)).findAny();
                if (threatening_action.isPresent()) {
                    // We refine the invariant according to the found threatening action..
                    refined_invariants.addAll(refine(invariant, threatening_action.get().getEffect()));
                } else {
                    // Since the invariant is not threatened by any schematic operator, we can accept it..
                    invariants.add(invariant);
                }
            });
            candidates.clear();
            candidates.addAll(refined_invariants);
        }
        return invariants;
    }
}
