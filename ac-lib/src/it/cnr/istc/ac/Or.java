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

import it.cnr.istc.ac.api.IBoolVar;
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
class Or extends BoolVar implements IConstraint {

    private final Collection<IBoolVar> vars;
    private final List<IBoolVar> arguments;

    Or(ACNetwork network, Collection<IBoolVar> vars) {
        super(network, "(or " + vars.stream().map(var -> var.isSingleton() ? var.toString() : var.getName()).collect(Collectors.joining(" ")) + ")", Arrays.asList(Boolean.TRUE, Boolean.FALSE));
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
                Set<IBoolVar> true_or_free_vars = vars.stream().filter(v -> v.getAllowedValues().contains(Boolean.TRUE)).collect(Collectors.toSet());
                if (true_or_free_vars.size() == 1 && !true_or_free_vars.iterator().next().remove(Boolean.FALSE)) {
                    return false;
                }
            } else {
                // The constraint must be not satisfied..
                assert c_allowed_values.contains(Boolean.FALSE);
                if (vars.stream().anyMatch(v -> !v.remove(Boolean.TRUE))) {
                    return false;
                }
            }
        } else if (vars.stream().allMatch(v -> v.isSingleton())) {
            if (vars.stream().anyMatch(v -> v.getAllowedValues().contains(Boolean.TRUE))) {
                // The constraint is already satisfied..
                if (!remove(Boolean.FALSE)) {
                    return false;
                }
            } else {
                // The constraint cannot be satisfied..
                assert vars.stream().allMatch(v -> v.getAllowedValues().contains(Boolean.FALSE));
                if (!remove(Boolean.TRUE)) {
                    return false;
                }
            }
        }
        return true;
    }
}
