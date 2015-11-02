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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StateVariableValueRenderer implements AttributeRenderer {

    private final STGroupFile file;

    StateVariableValueRenderer(STGroupFile file) {
        this.file = file;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        StateVariableValue value = (StateVariableValue) o;
        int size = value.getSize();
        assert size > 0;
        if (size == 1) {
            if (!value.getActions().isEmpty()) {
                Action action = value.getActions().stream().findAny().get();
                return "goal " + action.getName().toLowerCase() + " = new agent." + action.getName() + "(at:start);";
            } else if (!value.getAtStartDurativeActions().isEmpty()) {
                Action action = value.getActions().stream().findAny().get();
                return "goal " + action.getName().toLowerCase() + " = new agent." + action.getName() + "(start:start);";
            } else if (!value.getAtEndDurativeActions().isEmpty()) {
                Action action = value.getActions().stream().findAny().get();
                return "goal " + action.getName().toLowerCase() + " = new agent." + action.getName() + "(end:start);";
            } else {
                throw new AssertionError();
            }
        } else {
            Collection<String> or = new ArrayList<>();
            value.getActions().forEach(action -> {
                or.add("    goal " + action.getName().toLowerCase() + " = new agent." + action.getName() + "(at:start);");
            });
            value.getAtStartDurativeActions().forEach(at_start_action -> {
                or.add("    goal " + at_start_action.getName().toLowerCase() + " = new agent." + at_start_action.getName() + "(start:start);");
            });
            value.getAtEndDurativeActions().forEach(at_end_action -> {
                or.add("    goal " + at_end_action.getName().toLowerCase() + " = new agent." + at_end_action.getName() + "(end:start);");
            });
            return "{\n" + or.stream().collect(Collectors.joining("\n} or {\n")) + "\n}";
        }
    }
}
