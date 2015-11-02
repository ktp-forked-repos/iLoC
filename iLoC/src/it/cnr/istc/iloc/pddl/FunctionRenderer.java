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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class FunctionRenderer implements AttributeRenderer {

    private final STGroupFile file;
    private final Domain domain;

    FunctionRenderer(STGroupFile file, Domain domain) {
        this.file = file;
        this.domain = domain;
    }

    @Override
    public String toString(Object o, String format, Locale locale) {
        Function function = (Function) o;
        List<String> disjuncts = new ArrayList<>();

        Map<Action, List<Term>> actions = new HashMap<>();
        domain.getActions().values().stream().filter(action -> action.getEffect() != null).forEach(action -> {
            List<Term> terms = action.getEffect().containsFunction(function);
            if (!terms.isEmpty()) {
                actions.put(action, terms);
            }
        });
        actions.entrySet().forEach(entrySet -> {
            Action action = entrySet.getKey();
            entrySet.getValue().forEach(t -> {
                StringBuilder sb = new StringBuilder();
                AssignOpTerm term = (AssignOpTerm) t;
                assert term.getFunctionTerm().getFunction() == function;

                Map<String, String> known_terms = new HashMap<>();
                known_terms.put("action_name", Utils.lowercase(action.getName()));
                for (int i = 0; i < function.getVariables().size(); i++) {
                    if (term.getFunctionTerm().getArguments().get(i) instanceof VariableTerm) {
                        known_terms.put(((VariableTerm) term.getFunctionTerm().getArguments().get(i)).getName(), Utils.lowercase(function.getVariables().get(i).getName()));
                    } else if (term.getFunctionTerm().getArguments().get(i) instanceof FunctionTerm) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    } else {
                        known_terms.put(((ConstantTerm) term.getFunctionTerm().getArguments().get(i)).getName(), Utils.lowercase(function.getVariables().get(i).getName()));
                    }
                }
                if (term.getValue() instanceof VariableTerm) {
                    known_terms.put(((VariableTerm) term.getValue()).getName(), "value");
                } else if (term.getValue() instanceof FunctionTerm) {
                    throw new UnsupportedOperationException("Not supported yet.");
                } else if (term.getValue() instanceof ConstantTerm) {
                    sb.append("value == ").append(((ConstantTerm) term.getValue()).getName()).append(";\n");
                } else if (term.getValue() instanceof NumberTerm) {
                    sb.append("value == ").append(((NumberTerm) term.getValue()).getValue()).append(";\n");
                }

                List<String> action_assignments = new ArrayList<>(action.getVariables().size());
                for (int i = 0; i < action.getVariables().size(); i++) {
                    if (known_terms.containsKey(action.getVariables().get(i).getName())) {
                        action_assignments.add(action.getVariables().get(i).getName() + ":" + known_terms.get(action.getVariables().get(i).getName()));
                    } else {
                        known_terms.put((action.getVariables().get(i)).getName(), Utils.lowercase(action.getName()) + "." + action.getVariables().get(i).getName());
                    }
                }

                ST action_template = file.getInstanceOf("Action");
                action_template.add("name", Utils.lowercase(action.getName()));
                action_template.add("action_name", Utils.capitalize(action.getName()));
                action_template.add("assignments", action_assignments);
                sb.append(action_template.render()).append("\n");

                sb.append(action.getEffect().toString(file, known_terms, new HashSet<>(Arrays.asList(term)), Term.Mode.Effect));

                disjuncts.add(sb.toString());
            });
        });

        Map<DurativeAction, List<Term>> durative_actions = new HashMap<>();
        domain.getDurativeActions().values().stream().filter(action -> action.getEffect() != null).forEach(action -> {
            List<Term> terms = action.getEffect().containsFunction(function);
            if (!terms.isEmpty()) {
                durative_actions.put(action, terms);
            }
        });
        durative_actions.entrySet().forEach(entrySet -> {
            DurativeAction action = entrySet.getKey();
            entrySet.getValue().forEach(t -> {
                StringBuilder sb = new StringBuilder();
                FunctionTerm term = (FunctionTerm) t;
                assert term.getFunction() == function;

                Map<String, String> known_terms = new HashMap<>();
                known_terms.put("action_name", Utils.lowercase(action.getName()));
                for (int i = 0; i < function.getVariables().size(); i++) {
                    if (term.getArguments().get(i) instanceof VariableTerm) {
                        known_terms.put(((VariableTerm) term.getArguments().get(i)).getName(), Utils.lowercase(function.getVariables().get(i).getName()));
                    } else if (term.getArguments().get(i) instanceof FunctionTerm) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    } else {
                        known_terms.put(((ConstantTerm) term.getArguments().get(i)).getName(), Utils.lowercase(function.getVariables().get(i).getName()));
                    }
                }

                List<String> action_assignments = new ArrayList<>(action.getVariables().size());
                for (int i = 0; i < action.getVariables().size(); i++) {
                    if (known_terms.containsKey(action.getVariables().get(i).getName())) {
                        action_assignments.add(action.getVariables().get(i).getName() + ":" + known_terms.get(action.getVariables().get(i).getName()));
                    } else {
                        known_terms.put((action.getVariables().get(i)).getName(), Utils.lowercase(action.getName()) + "." + action.getVariables().get(i).getName());
                    }
                }

                ST action_template = file.getInstanceOf("DurativeAction");
                action_template.add("name", Utils.lowercase(action.getName()));
                action_template.add("action_name", Utils.capitalize(action.getName()));
                action_template.add("assignments", action_assignments);
                sb.append(action_template.render()).append("\n");

                sb.append(action.getEffect().toString(file, known_terms, new HashSet<>(Arrays.asList(term)), Term.Mode.Effect));

                disjuncts.add(sb.toString());
            });
        });

        if (disjuncts.isEmpty()) {
            // TODO: It is a static function!! It should be handled as a static function..
            return "";
        } else if (disjuncts.size() == 1) {
            return disjuncts.get(0);
        } else {
            ST disjunction = file.getInstanceOf("Disjunction");
            disjunction.add("disjuncts", disjuncts);
            return disjunction.render();
        }
    }
}
