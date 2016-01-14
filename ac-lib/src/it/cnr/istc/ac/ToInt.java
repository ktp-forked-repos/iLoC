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
import it.cnr.istc.ac.api.Int;
import it.cnr.istc.ac.api.Real;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class ToInt extends IntVar implements IConstraint {

    private final IRealVar v;
    private final List<IVar> arguments;

    ToInt(ACNetwork network, IRealVar v) {
        super(network, "(to_int " + (v.isSingleton() ? v.toString() : v.getName()) + ")", Int.NEGATIVE_INFINITY, Int.POSITIVE_INFINITY);
        this.v = v;
        if (v.getLB().isNegativeInifinity()) {
            // nothing to do..
        } else if (v.getLB().isPositiveInifinity()) {
            lb = Int.POSITIVE_INFINITY;
        } else {
            lb = new Int(v.getLB().setScale(0, RoundingMode.DOWN).toString());
        }
        if (v.getUB().isNegativeInifinity()) {
            ub = Int.NEGATIVE_INFINITY;
        } else if (v.getUB().isPositiveInifinity()) {
            // nothing to do..
        } else {
            ub = new Int(v.getUB().setScale(0, RoundingMode.UP).toString());
        }
        this.arguments = Arrays.asList(this, v);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        if (var == v) {
            Int c_lb = Int.NEGATIVE_INFINITY;
            if (v.getLB().isNegativeInifinity()) {
                // nothing to do..
            } else if (v.getLB().isPositiveInifinity()) {
                c_lb = Int.POSITIVE_INFINITY;
            } else {
                c_lb = new Int(v.getLB().setScale(0, RoundingMode.DOWN).toString());
            }
            Int c_ub = Int.POSITIVE_INFINITY;
            if (v.getUB().isNegativeInifinity()) {
                c_ub = Int.NEGATIVE_INFINITY;
            } else if (v.getUB().isPositiveInifinity()) {
                // nothing to do..
            } else {
                c_ub = new Int(v.getUB().setScale(0, RoundingMode.UP).toString());
            }
            if (!intersect(c_lb, c_ub)) {
                return false;
            }
        } else {
            assert var == this;
            Real c_lb = Real.NEGATIVE_INFINITY;
            if (lb.isNegativeInifinity()) {
                // nothing to do..
            } else if (lb.isPositiveInifinity()) {
                c_lb = Real.POSITIVE_INFINITY;
            } else {
                c_lb = new Real(lb.toString());
            }
            Real c_ub = Real.POSITIVE_INFINITY;
            if (ub.isNegativeInifinity()) {
                c_ub = Real.NEGATIVE_INFINITY;
            } else if (ub.isPositiveInifinity()) {
                // nothing to do..
            } else {
                c_ub = new Real(ub.toString());
            }
            if (!v.intersect(c_lb, c_ub)) {
                return false;
            }
        }
        return true;
    }
}
