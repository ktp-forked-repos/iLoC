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

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DurativeActionRenderer implements AttributeRenderer {

    private final STGroupFile file;

    DurativeActionRenderer(STGroupFile file) {
        this.file = file;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        DurativeAction action = (DurativeAction) o;
        Map<String, String> known_terms = action.getVariables().stream().map(variable -> variable.getName()).collect(Collectors.toMap(variable -> variable, variable -> variable));
        StringBuilder sb = new StringBuilder();
        if (action.getDuration() != null) {
            sb.append("        ").append(action.getDuration().toString(file, known_terms, Collections.emptySet(), Term.Mode.OverAllCondition)).append(";\n");
        }
        if (action.getCondition() != null) {
            sb.append(action.getCondition().toString(file, known_terms, Collections.emptySet(), Term.Mode.Condition));
        }
        return sb.toString();
    }
}
