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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(define (domain ").append(name).append(")\n");

        sb.append("(:types ");
        types.values().stream().forEach(type -> {
            sb.append(type.getName());
            if (type.getSuperclass() != null) {
                sb.append(" - ").append(type.getSuperclass().getName());
            }
        });
        sb.append(")\n");

        sb.append("(:predicates ");
        predicates.values().stream().forEach(predicate -> {
            sb.append(predicate);
        });
        sb.append(")\n");

        actions.values().stream().forEach(predicate -> {
            sb.append(predicate);
        });

        durative_actions.values().stream().forEach(predicate -> {
            sb.append(predicate);
        });

        sb.append(")\n");
        return sb.toString();
    }
}
