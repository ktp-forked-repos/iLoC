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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class FunctionTerm implements Term {

    private final Function function;
    private final List<Term> arguments;

    FunctionTerm(Function function, List<Term> arguments) {
        assert function != null;
        assert arguments.stream().noneMatch(Objects::isNull);
        this.function = function;
        this.arguments = arguments;
    }

    public Function getFunction() {
        return function;
    }

    public List<Term> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public Term negate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Term ground(Domain domain, Map<String, Term> known_terms) {
        return new FunctionTerm(domain.getFunction(function.getName() + "_" + arguments.stream().map(term -> term.ground(domain, known_terms).toString()).collect(Collectors.joining("_")) + "_"), Collections.emptyList());
    }

    @Override
    public List<Term> containsPredicate(boolean directed, Predicate predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<Term> containsFunction(Function function) {
        if (this.function == function) {
            return Arrays.asList(this);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String toString(STGroupFile file, Map<String, String> known_terms, Set<Term> to_skip, Mode mode) {
        assert !to_skip.contains(this);
        StringBuilder sb = new StringBuilder();
        String value = known_terms.remove("value");
        List<String> assignments = new ArrayList<>(arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) instanceof VariableTerm) {
                if (known_terms.containsKey(((VariableTerm) arguments.get(i)).getName())) {
                    assignments.add(function.getVariables().get(i).getName() + ":" + known_terms.get(((VariableTerm) arguments.get(i)).getName()));
                } else {
                    known_terms.put(((VariableTerm) arguments.get(i)).getName(), Utils.lowercase(function.getName()) + "." + ((VariableTerm) arguments.get(i)).getName());
                }
            } else if (arguments.get(i) instanceof FunctionTerm) {
                sb.append(arguments.get(i).toString(file, known_terms, to_skip, mode)).append("\n");
                assignments.add(function.getVariables().get(i).getName() + ":" + Utils.lowercase(((FunctionTerm) arguments.get(i)).getFunction().getName()) + ".value");
            } else {
                assignments.add(function.getVariables().get(i).getName() + ":" + ((ConstantTerm) arguments.get(i)).getName());
            }
        }
        if (value != null) {
            assignments.add("value:" + value);
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

        translation.add("name", Utils.lowercase(function.getName()));
        translation.add("predicate_name", Utils.capitalize(function.getName()));
        translation.add("assignments", assignments);

        sb.append(translation.render());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "(" + function.getName() + " " + arguments.stream().map(argument -> argument.toString()).collect(Collectors.joining(" ")) + ")";
    }
}
