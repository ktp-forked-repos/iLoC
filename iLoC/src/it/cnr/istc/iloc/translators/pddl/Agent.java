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
class Agent {

    private final String name;
    private final Map<String, StateVariable> state_variables = new LinkedHashMap<>();
    private final Map<String, Action> actions = new LinkedHashMap<>();
    private final Map<String, DurativeAction> durative_actions = new LinkedHashMap<>();

    Agent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public StateVariable getStateVariable(String name) {
        return state_variables.get(name);
    }

    public void addStateVariable(StateVariable stateVariable) {
        state_variables.put(stateVariable.getName(), stateVariable);
    }

    public Map<String, StateVariable> getStateVariables() {
        return Collections.unmodifiableMap(state_variables);
    }

    public Action getAction(String name) {
        return actions.get(name);
    }

    public void addAction(Action action) {
        actions.put(action.getName(), action);
    }

    public Map<String, Action> getActions() {
        return Collections.unmodifiableMap(actions);
    }

    public DurativeAction getDurativeAction(String name) {
        return durative_actions.get(name);
    }

    public void addDurativeAction(DurativeAction action) {
        durative_actions.put(action.getName(), action);
    }

    public Map<String, DurativeAction> getDurativeActions() {
        return Collections.unmodifiableMap(durative_actions);
    }
}
