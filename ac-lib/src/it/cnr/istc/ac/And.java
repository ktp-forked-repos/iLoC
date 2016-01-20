/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.ac;

import it.cnr.istc.ac.api.IACBool;
import it.cnr.istc.ac.api.IConstraint;
import it.cnr.istc.ac.api.IVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class And extends BoolVar implements IConstraint {

    private final Collection<IACBool> vars;
    private final List<IACBool> arguments;

    And(ACNetwork network, Collection<IACBool> vars) {
        super(network, "(and " + vars.stream().map(var -> var.isSingleton() ? var.toString() : var.getName()).collect(Collectors.joining(" ")) + ")", Arrays.asList(Boolean.TRUE, Boolean.FALSE));
        this.vars = vars;
        this.arguments = new ArrayList<>(vars.size() + 1);
        this.arguments.add(this);
        this.arguments.addAll(vars);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        Set<Boolean> c_allowed_values = getAllowedValues();
        if (isSingleton()) {
            if (c_allowed_values.contains(Boolean.TRUE)) {
                // The constraint must be satisfied..
                if (vars.stream().anyMatch(v -> !v.remove(Boolean.FALSE))) {
                    return false;
                }
            } else {
                // The constraint must be not satisfied..
                assert c_allowed_values.contains(Boolean.FALSE);
                Set<IACBool> false_or_free_vars = vars.stream().filter(v -> v.getAllowedValues().contains(Boolean.FALSE)).collect(Collectors.toSet());
                if (false_or_free_vars.size() == 1 && !false_or_free_vars.iterator().next().remove(Boolean.TRUE)) {
                    return false;
                }
            }
        } else if (vars.stream().allMatch(v -> v.isSingleton())) {
            if (vars.stream().allMatch(v -> v.getAllowedValues().contains(Boolean.TRUE))) {
                // The constraint is already satisfied..
                if (!remove(Boolean.FALSE)) {
                    return false;
                }
            } else {
                // The constraint cannot be satisfied..
                assert vars.stream().anyMatch(v -> v.getAllowedValues().contains(Boolean.TRUE));
                if (!remove(Boolean.TRUE)) {
                    return false;
                }
            }
        }
        return true;
    }
}
