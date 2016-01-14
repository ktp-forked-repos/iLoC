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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class BNot extends BoolVar implements IConstraint {

    private final IBoolVar v;
    private final List<IBoolVar> arguments;

    BNot(ACNetwork network, IBoolVar v) {
        super(network, "(! " + (v.isSingleton() ? v.toString() : v.getName()) + ")");
        assert !v.isEmpty();
        if (v.isSingleton()) {
            boolean complement = complement(v.getAllowedValues());
            assert complement;
        }
        this.v = v;
        this.arguments = Arrays.asList(this, v);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        assert var.isSingleton();
        if (var == v) {
            return complement(((IBoolVar) var).getAllowedValues());
        } else if (var == this) {
            return v.complement(allowed_values);
        } else {
            throw new AssertionError("variable " + var + " is not in " + arguments.toString());
        }
    }
}
