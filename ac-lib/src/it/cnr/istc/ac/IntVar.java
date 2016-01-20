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

import it.cnr.istc.ac.api.IACInt;
import it.cnr.istc.ac.api.Int;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class IntVar extends Var implements IACInt {

    protected Int lb, ub;

    IntVar(ACNetwork network, String name, Int lb, Int ub) {
        super(network, name);
        this.lb = lb;
        this.ub = ub;
    }

    @Override
    public Int getLB() {
        return lb;
    }

    @Override
    public Int getUB() {
        return ub;
    }

    @Override
    public boolean intersect(Int lb, Int ub) {
        if (isIntersecting(lb, ub)) {
            boolean updated = false;
            Int old_lb = this.lb;
            Int old_ub = this.ub;
            if (this.lb.lt(lb)) {
                this.lb = lb;
                updated = true;
            }
            if (this.ub.gt(ub)) {
                this.ub = ub;
                updated = true;
            }
            if (updated) {
                if (!network.stack.peekFirst().domains.containsKey(this)) {
                    network.stack.peekFirst().domains.put(this, new Domain(n_updates, old_lb, old_ub));
                }
                network.enqueue(this);
                n_updates++;
                return !isEmpty() && n_updates <= network.getNbConstraints();
            } else {
                assert !isEmpty();
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean complement(Int lb, Int ub) {
        boolean updated = false;
        Int old_lb = this.lb;
        Int old_ub = this.ub;
        if (!lb.isInifinity() && this.lb.eq(lb)) {
            this.lb = ub.add(Int.ONE);
            updated = true;
        }
        if (!ub.isInifinity() && this.ub.eq(ub)) {
            this.ub = lb.subtract(Int.ONE);
            updated = true;
        }
        if (updated) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain(n_updates, old_lb, old_ub));
            }
            network.enqueue(this);
            n_updates++;
            return !isEmpty() && n_updates <= network.getNbConstraints();
        } else {
            assert !isEmpty();
            return true;
        }
    }

    @Override
    void restoreDomain() {
        Domain old_domain = (Domain) network.stack.peekFirst().domains.get(this);
        this.n_updates = old_domain.n_updates;
        this.lb = old_domain.lb;
        this.ub = old_domain.ub;
    }

    @Override
    public String toString() {
        if (isSingleton()) {
            return lb.toString();
        } else {
            return name + " in [" + lb + ", " + ub + "]";
        }
    }

    private static class Domain {

        private final int n_updates;
        private final Int lb, ub;

        Domain(int n_updates, Int lb, Int ub) {
            this.n_updates = n_updates;
            this.lb = lb;
            this.ub = ub;
        }
    }
}
