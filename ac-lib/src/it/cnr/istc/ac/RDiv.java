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

import it.cnr.istc.ac.api.IACReal;
import it.cnr.istc.ac.api.IConstraint;
import it.cnr.istc.ac.api.IVar;
import it.cnr.istc.ac.api.Real;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RDiv extends RealVar implements IConstraint {

    private final IACReal v0, v1;
    private final List<IACReal> arguments;

    RDiv(ACNetwork network, IACReal v0, IACReal v1) {
        super(network, "(/ " + (v0.isSingleton() ? v0.toString() : v0.getName()) + " " + (v1.isSingleton() ? v1.toString() : v1.getName()) + ")", Real.NEGATIVE_INFINITY, Real.POSITIVE_INFINITY);
        this.v0 = v0;
        this.v1 = v1;
        Real[] c_domain = Real.divide(v0.getLB(), v0.getUB(), v1.getLB(), v1.getUB());
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
            Real[] new_div = Real.divide(v0.getLB(), v0.getUB(), v1.getLB(), v1.getUB());
            Real[] new_v1 = Real.divide(v0.getLB(), v0.getUB(), lb, ub);
            if (!intersect(new_div[0], new_div[1]) || !v1.intersect(new_v1[0], new_v1[1])) {
                return false;
            }
        } else if (var == v1) {
            Real[] new_div = Real.divide(v0.getLB(), v0.getUB(), v1.getLB(), v1.getUB());
            Real[] new_v0 = Real.multiply(v1.getLB(), v1.getUB(), lb, ub);
            if (!intersect(new_div[0], new_div[1]) || !v0.intersect(new_v0[0], new_v0[1])) {
                return false;
            }
        } else if (var == this) {
            Real[] new_v0 = Real.multiply(v1.getLB(), v1.getUB(), lb, ub);
            Real[] new_v1 = Real.divide(v0.getLB(), v0.getUB(), lb, ub);
            if (!v0.intersect(new_v0[0], new_v0[1]) || !v1.intersect(new_v1[0], new_v1[1])) {
                return false;
            }
        } else {
            throw new AssertionError("Variable " + var + " not in constraint " + this);
        }
        return true;
    }
}
