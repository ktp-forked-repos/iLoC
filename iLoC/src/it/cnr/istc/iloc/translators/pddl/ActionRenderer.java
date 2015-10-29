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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ActionRenderer implements AttributeRenderer {

    private final STGroupFile file;
    private final Grounder grounder;

    ActionRenderer(STGroupFile file, Grounder grounder) {
        this.file = file;
        this.grounder = grounder;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        GroundAction action = (GroundAction) o;
        StringBuilder sb = new StringBuilder();
        sb.append("predicate ").append(action.getName()).append("() {\n");
        Map<Class<? extends GD>, List<GD>> gds = action.getPrecondition().getGDs().stream().collect(Collectors.groupingBy(gd -> gd.getClass()));
        gds.entrySet().stream().forEach(entry -> {
            if (entry.getKey() == AND.class) {
            } else if (entry.getKey() == AND.class) {
            } else if (entry.getKey() == AND.class) {
            } else {
                throw new UnsupportedOperationException(entry.getKey().getName());
            }
        });
        action.getPrecondition().getGDs().stream().filter(gd -> gd instanceof StateVariableValue).map(gd -> (StateVariableValue) gd).forEach(value -> {
            String goal_name = Utils.lowercase(value.getStateVariable().getName()) + "_" + Utils.lowercase(value.getName());
            sb.append("    goal ").append(goal_name).append(" = new ").append(Utils.lowercase(value.getStateVariable().getName())).append(".").append(value.getName()).append("(");
            if (Utils.containsStateVariable(action.getPrecondition(), value.getStateVariable())) {
                sb.append("end:start);\n");
            } else {
                sb.append(");\n");
                sb.append("    ").append(goal_name).append(".start <= at - 1;\n");
                sb.append("    ").append(goal_name).append(".end >= at;\n");
            }
        });
        sb.append("}\n");
        return sb.toString();
    }
}
