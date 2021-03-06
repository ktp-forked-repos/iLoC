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
import java.util.Arrays;
import java.util.Collections;
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
class PredicateTerm implements Term {

    private final Term enclosingTerm;
    private final boolean directed;
    private Predicate predicate;
    private final List<Term> arguments = new ArrayList<>();

    PredicateTerm(Term enclosingTerm, boolean directed) {
        this.enclosingTerm = enclosingTerm;
        this.directed = directed;
    }

    public boolean isDirected() {
        return directed;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        assert predicate != null;
        this.predicate = predicate;
    }

    public void addArgument(Term argument) {
        assert argument != null;
        arguments.add(argument);
    }

    public List<Term> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public Term getEnclosingTerm() {
        return enclosingTerm;
    }

    @Override
    public Term negate(Term enclosingTerm) {
        PredicateTerm predicate_term = new PredicateTerm(enclosingTerm, !directed);
        predicate_term.setPredicate(predicate);
        arguments.forEach(argument -> predicate_term.addArgument(argument));
        return predicate_term;
    }

    @Override
    public Term ground(Domain domain, Term enclosingTerm, Map<String, Term> known_terms) {
        PredicateTerm predicate_term = new PredicateTerm(enclosingTerm, directed);
        String predicate_name = predicate.getVariables().isEmpty() ? predicate.getName() : predicate.getName() + "_" + arguments.stream().map(term -> term.ground(domain, predicate_term, known_terms).toString()).collect(Collectors.joining("_")) + "_";
        predicate_term.setPredicate(domain.getPredicate(predicate_name));
        return predicate_term;
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        if (this.directed == directed && this.predicate == predicate) {
            return Arrays.asList(this);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Term> containsFunction(Function function) {
        return Collections.emptyList();
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        assert !to_skip.contains(this);
        StringBuilder sb = new StringBuilder();
        List<String> assignments = new ArrayList<>(arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) instanceof VariableTerm) {
                if (known_terms.containsKey(((VariableTerm) arguments.get(i)).getName())) {
                    assignments.add(predicate.getVariables().get(i).getName() + ":" + known_terms.get(((VariableTerm) arguments.get(i)).getName()));
                } else {
                    known_terms.put(((VariableTerm) arguments.get(i)).getName(), Utils.lowercase(predicate.getName()) + "." + ((VariableTerm) arguments.get(i)).getName());
                }
            } else if (arguments.get(i) instanceof FunctionTerm) {
                sb.append(arguments.get(i).toString(file, known_terms, to_skip, mode)).append("\n");
                assignments.add(predicate.getVariables().get(i).getName() + ":" + Utils.lowercase(((FunctionTerm) arguments.get(i)).getFunction().getName()) + ".value");
            } else {
                assignments.add(predicate.getVariables().get(i).getName() + ":" + ((ConstantTerm) arguments.get(i)).getName());
            }
        }

        ST translation = null;
        switch (mode) {
            case Condition:
                translation = file.getInstanceOf("PredicatePrecondition");
                break;
            case Effect:
                assert known_terms.containsKey("action_name");
                translation = file.getInstanceOf("PredicateEffect");
                translation.add("action_name", known_terms.get("action_name"));
                break;
            case AtStartCondition:
                translation = file.getInstanceOf("PredicateAtStartCondition");
                break;
            case OverAllCondition:
                translation = file.getInstanceOf("PredicateOverAllCondition");
                break;
            case AtEndCondition:
                translation = file.getInstanceOf("PredicateAtEndCondition");
                break;
            case AtStartEffect:
                assert known_terms.containsKey("action_name");
                translation = file.getInstanceOf("PredicateAtStartEffect");
                translation.add("action_name", known_terms.get("action_name"));
                break;
            case AtEndEffect:
                assert known_terms.containsKey("action_name");
                translation = file.getInstanceOf("PredicateAtEndEffect");
                translation.add("action_name", known_terms.get("action_name"));
                break;
            case InitEl:
                translation = file.getInstanceOf("PredicateInit");
                break;
            case Goal:
                translation = file.getInstanceOf("PredicateGoal");
                break;
            default:
                throw new AssertionError(mode.name());
        }
        assert translation != null;

        translation.add("name", Utils.lowercase(predicate.getName()) + (directed ? "_true" : "_false"));
        translation.add("predicate_name", Utils.capitalize(predicate.getName()) + (directed ? "True" : "False"));
        translation.add("assignments", assignments);

        sb.append(translation.render());
        return sb.toString();
    }

    @Override
    public String toString() {
        if (directed) {
            return "(" + predicate.getName() + " " + arguments.stream().map(argument -> argument.toString()).collect(Collectors.joining(" ")) + ")";
        } else {
            return "(not (" + predicate.getName() + " " + arguments.stream().map(argument -> argument.toString()).collect(Collectors.joining(" ")) + "))";
        }
    }
}
