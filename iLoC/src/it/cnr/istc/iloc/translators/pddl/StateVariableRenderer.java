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

import java.util.Locale;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StateVariableRenderer implements AttributeRenderer {

    private final STGroupFile file;
    private final Grounder grounder;

    StateVariableRenderer(STGroupFile file, Grounder grounder) {
        this.file = file;
        this.grounder = grounder;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        StateVariable variable = (StateVariable) o;
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(variable.getName()).append(" extends StateVariable {\n");
        variable.getValues().stream().forEach(value -> {
            sb.append("\n");
            sb.append("    predicate ").append(value.getName()).append("() {\n");
            sb.append("    }\n");
        });
        sb.append("}\n");
        return sb.toString();
    }
}
