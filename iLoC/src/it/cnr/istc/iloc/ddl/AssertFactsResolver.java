/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.iloc.ddl;

import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IResolver;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class AssertFactsResolver implements IResolver {

    private final IConstraintNetwork network;
    private final IBool[] facts;
    private boolean resolved = false;

    AssertFactsResolver(IConstraintNetwork network, IBool... facts) {
        this.network = network;
        this.facts = facts;
    }

    @Override
    public double getKnownCost() {
        return 0;
    }

    @Override
    public boolean isResolved() {
        return resolved;
    }

    @Override
    public void resolve() {
        assert !resolved;
        network.assertFacts(facts);
        resolved = true;
    }

    @Override
    public void retract() {
        assert resolved;
        resolved = false;
    }
}
