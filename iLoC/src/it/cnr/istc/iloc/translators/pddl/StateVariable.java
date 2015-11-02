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
class StateVariable {

    private final String name;
    private final Map<String, StateVariableValue> values = new LinkedHashMap<>();

    StateVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addValue(StateVariableValue value) {
        values.put(value.getName(), value);
    }

    public void removeValue(StateVariableValue value) {
        values.remove(value.getName(), value);
    }

    public StateVariableValue getValue(String name) {
        return values.get(name);
    }

    public Map<String, StateVariableValue> getValues() {
        return Collections.unmodifiableMap(values);
    }
}
