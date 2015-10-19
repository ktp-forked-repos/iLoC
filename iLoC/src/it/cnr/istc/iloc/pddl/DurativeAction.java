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
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DurativeAction {

    private final String name;
    private final List<Variable> variables = new ArrayList<>();
    private final Term duration;
    private final Term condition;
    private final Term effect;

    DurativeAction(String name, Variable[] variables, Term duration, Term condition, Term effect) {
        this.name = name;
        this.variables.addAll(Arrays.asList(variables));
        this.duration = duration;
        this.condition = condition;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    public Term getDuration() {
        return duration;
    }

    public Term getCondition() {
        return condition;
    }

    public Term getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name).append("(").append(variables.stream().map(variable -> variable.getName() + " - " + variable.getType().getName()).collect(Collectors.joining(" "))).append(")\n");
        if (duration != null) {
            sb.append("(:duration ").append(duration.toString()).append(")\n");
        }
        if (condition != null) {
            sb.append("(:condition ").append(condition.toString()).append(")\n");
        }
        if (effect != null) {
            sb.append("(:effect ").append(effect.toString()).append(")\n");
        }
        sb.append(")");
        return sb.toString();
    }
}
