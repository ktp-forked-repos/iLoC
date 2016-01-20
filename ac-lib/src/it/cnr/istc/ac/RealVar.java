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
import it.cnr.istc.ac.api.Real;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RealVar extends Var implements IACReal {

    protected Real lb, ub;

    RealVar(ACNetwork network, String name, Real lb, Real ub) {
        super(network, name);
        this.lb = lb;
        this.ub = ub;
    }

    @Override
    public Real getLB() {
        return lb;
    }

    @Override
    public Real getUB() {
        return ub;
    }

    @Override
    public boolean intersect(Real lb, Real ub) {
        if (isIntersecting(lb, ub)) {
            boolean updated = false;
            Real old_lb = this.lb;
            Real old_ub = this.ub;
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
    public boolean complement(Real lb, Real ub) {
        boolean updated = false;
        Real old_lb = this.lb;
        Real old_ub = this.ub;
        if (!lb.isInifinity() && this.lb.eq(lb)) {
            this.lb = ub;
            updated = true;
        }
        if (!ub.isInifinity() && this.ub.eq(ub)) {
            this.ub = lb;
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
        private final Real lb, ub;

        Domain(int n_updates, Real lb, Real ub) {
            this.n_updates = n_updates;
            this.lb = lb;
            this.ub = ub;
        }
    }
}
