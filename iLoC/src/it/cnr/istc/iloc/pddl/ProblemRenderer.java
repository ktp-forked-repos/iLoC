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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ProblemRenderer implements AttributeRenderer {

    private final STGroupFile file;

    ProblemRenderer(STGroupFile file) {
        this.file = file;
    }

    @Override
    public String toString(Object o, String string, Locale locale) {
        Problem problem = (Problem) o;

        StringBuilder sb = new StringBuilder();

        ST translation = file.getInstanceOf("Problem");
        translation.add("problem", (Problem) o);
        sb.append(translation.render());

        Map<String, String> known_terms = new HashMap();
        problem.getDomain().getConstants().values().forEach(constant -> {
            known_terms.put(constant.getName(), constant.getName());
        });
        problem.getObjects().values().forEach(object -> {
            known_terms.put(object.getName(), object.getName());
        });

        problem.getInitEls().forEach(init_el -> {
            sb.append(init_el.toString(file, known_terms, Collections.emptySet(), Term.Mode.InitEl)).append("\n");
        });

        sb.append(problem.getGoal().toString(file, known_terms, Collections.emptySet(), Term.Mode.Goal));

        return sb.toString().replace("?", "");
    }
}
