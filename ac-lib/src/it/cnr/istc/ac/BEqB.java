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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class BEqB extends BoolVar implements IConstraint {

    private final IBoolVar v0;
    private final IBoolVar v1;
    private final List<IBoolVar> arguments;

    BEqB(ACNetwork network, IBoolVar v0, IBoolVar v1) {
        super(network, "(== " + (v0.isSingleton() ? v0.toString() : v0.getName()) + " " + (v1.isSingleton() ? v1.toString() : v1.getName()) + ")", Arrays.asList(Boolean.TRUE, Boolean.FALSE));
        this.v0 = v0;
        this.v1 = v1;
        this.arguments = Arrays.asList(this, v0, v1);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        Set<Boolean> c_allowed_values = getAllowedValues();
        if (isSingleton()) {
            Collection<Boolean> v0_vals = new ArrayList<>(v0.getAllowedValues());
            Collection<Boolean> v1_vals = new ArrayList<>(v1.getAllowedValues());
            if (c_allowed_values.contains(Boolean.TRUE)) {
                // The constraint must be satisfied..
                if (!v0.intersect(v1_vals)) {
                    return false;
                }
                if (!v1.intersect(v0_vals)) {
                    return false;
                }
            } else {
                // The constraint must be not satisfied..
                assert c_allowed_values.contains(Boolean.FALSE);
                if (!v0.complement(v1_vals)) {
                    return false;
                }
                if (!v1.complement(v0_vals)) {
                    return false;
                }
            }
        } else if (v0.isSingleton() && v1.isSingleton()) {
            if (v0.isIntersecting(v1)) {
                // The constraint is already satisfied..
                if (!remove(Boolean.FALSE)) {
                    return false;
                }
            } else {
                // The constraint cannot be satisfied..
                assert !v0.isIntersecting(v1);
                if (!remove(Boolean.TRUE)) {
                    return false;
                }
            }
        }
        return true;
    }
}
