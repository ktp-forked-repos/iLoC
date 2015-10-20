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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Function {

    private final String name;
    private final Type type;
    private final List<Variable> variables = new ArrayList<>();

    Function(String name, Type type, Variable... variables) {
        assert type != null;
        this.name = name;
        this.type = type;
        this.variables.addAll(Arrays.asList(variables));
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name).append("\n");
        variables.stream().forEach(variable -> {
            sb.append("  ").append(variable.getName()).append(" - ").append(variable.getType().getName()).append("\n");
        });
        sb.append(")");
        return sb.toString();
    }
}