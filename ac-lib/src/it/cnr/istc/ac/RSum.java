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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RSum extends RealVar implements IConstraint {

    private final Collection<IRealVar> vars;
    private final List<IRealVar> arguments;

    RSum(ACNetwork network, Collection<IRealVar> vars) {
        super(network, "(+ " + vars.stream().map(var -> var.isSingleton() ? var.toString() : var.getName()).collect(Collectors.joining(" ")) + ")", Real.NEGATIVE_INFINITY, Real.POSITIVE_INFINITY);
        this.vars = vars;
        Real c_lb = Real.ZERO;
        Real c_ub = Real.ZERO;
        for (IRealVar v : vars) {
            c_lb = c_lb.add(v.getLB());
            c_ub = c_ub.add(v.getUB());
        }
        this.lb = c_lb;
        this.ub = c_ub;
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
        Real c_lb = Real.ZERO;
        Real c_ub = Real.ZERO;
        if (var != this) {
            // we update the current bounds..
            for (IRealVar v : vars) {
                c_lb = c_lb.add(v.getLB());
                c_ub = c_ub.add(v.getUB());
            }
            if (!intersect(c_lb, c_ub)) {
                return false;
            }
        }
        for (IRealVar v0 : vars) {
            if (var != v0) {
                // we update other variables bounds..
                c_lb = lb;
                c_ub = ub;
                for (IRealVar v1 : vars) {
                    if (v0 != v1) {
                        c_lb = c_lb.subtract(v1.getUB());
                        c_ub = c_ub.subtract(v1.getLB());
                    }
                }
                if (!v0.intersect(c_lb, c_ub)) {
                    return false;
                }
            }
        }
        return true;
    }
}
