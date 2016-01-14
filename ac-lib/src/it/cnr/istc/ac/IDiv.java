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

import it.cnr.istc.ac.api.IConstraint;
import it.cnr.istc.ac.api.IIntVar;
import it.cnr.istc.ac.api.IVar;
import it.cnr.istc.ac.api.Int;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class IDiv extends IntVar implements IConstraint {

    private final IIntVar v0, v1;
    private final List<IIntVar> arguments;

    IDiv(ACNetwork network, IIntVar v0, IIntVar v1) {
        super(network, "(/ " + (v0.isSingleton() ? v0.toString() : v0.getName()) + " " + (v1.isSingleton() ? v1.toString() : v1.getName()) + ")", Int.NEGATIVE_INFINITY, Int.POSITIVE_INFINITY);
        this.v0 = v0;
        this.v1 = v1;
        Int[] c_domain = Int.divide(v0.getLB(), v0.getUB(), v1.getLB(), v1.getUB());
        this.lb = c_domain[0];
        this.ub = c_domain[1];
        this.arguments = Arrays.asList(this, v0, v1);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        if (var == v0) {
            Int[] new_div = Int.divide(v0.getLB(), v0.getUB(), v1.getLB(), v1.getUB());
            Int[] new_v1 = Int.divide(v0.getLB(), v0.getUB(), lb, ub);
            if (!intersect(new_div[0], new_div[1]) || !v1.intersect(new_v1[0], new_v1[1])) {
                return false;
            }
        } else if (var == v1) {
            Int[] new_div = Int.divide(v0.getLB(), v0.getUB(), v1.getLB(), v1.getUB());
            Int[] new_v0 = Int.multiply(v1.getLB(), v1.getUB(), lb, ub);
            if (!intersect(new_div[0], new_div[1]) || !v0.intersect(new_v0[0], new_v0[1])) {
                return false;
            }
        } else if (var == this) {
            Int[] new_v0 = Int.multiply(v1.getLB(), v1.getUB(), lb, ub);
            Int[] new_v1 = Int.divide(v0.getLB(), v0.getUB(), lb, ub);
            if (!v0.intersect(new_v0[0], new_v0[1]) || !v1.intersect(new_v1[0], new_v1[1])) {
                return false;
            }
        } else {
            throw new AssertionError("Variable " + var + " not in constraint " + this);
        }
        return true;
    }
}
