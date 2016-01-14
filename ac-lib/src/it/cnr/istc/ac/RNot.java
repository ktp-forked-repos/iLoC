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
import it.cnr.istc.ac.api.IRealVar;
import it.cnr.istc.ac.api.IVar;
import it.cnr.istc.ac.api.Real;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RNot extends RealVar implements IConstraint {

    private final IRealVar v;
    private final List<IRealVar> arguments;

    RNot(ACNetwork network, IRealVar v) {
        super(network, "(- " + (v.isSingleton() ? v.toString() : v.getName()) + ")", Real.NEGATIVE_INFINITY, Real.POSITIVE_INFINITY);
        this.v = v;
        this.lb = Real.negate(v.getUB());
        this.ub = Real.negate(v.getLB());
        this.arguments = Arrays.asList(this, v);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        if (var == this) {
            return v.intersect(Real.negate(ub), Real.negate(lb));
        } else {
            return intersect(Real.negate(v.getUB()), Real.negate(v.getLB()));
        }
    }
}
