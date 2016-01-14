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
class INot extends IntVar implements IConstraint {

    private final IIntVar v;
    private final List<IIntVar> arguments;

    INot(ACNetwork network, IIntVar v) {
        super(network, "(- " + (v.isSingleton() ? v.toString() : v.getName()) + ")", Int.NEGATIVE_INFINITY, Int.POSITIVE_INFINITY);
        this.v = v;
        this.lb = Int.negate(v.getUB());
        this.ub = Int.negate(v.getLB());
        this.arguments = Arrays.asList(this, v);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        if (var == this) {
            return v.intersect(Int.negate(ub), Int.negate(lb));
        } else {
            return intersect(Int.negate(v.getUB()), Int.negate(v.getLB()));
        }
    }
}
