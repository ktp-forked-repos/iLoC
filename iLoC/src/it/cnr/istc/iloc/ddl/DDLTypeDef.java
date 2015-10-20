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

import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DDLTypeDef extends Type {

    private final IType primitive_type;
    private final INumber min;
    private final INumber max;

    DDLTypeDef(ISolver solver, IScope enclosingScope, IType primitive_type, String name, INumber min, INumber max) {
        super(solver, enclosingScope, name);
        this.primitive_type = primitive_type;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public IObject newInstance(IEnvironment enclosing_environment) {
        IConstraintNetwork network = solver.getConstraintNetwork();
        INumber var = createNumber(primitive_type, network);
        solver.getCurrentNode().addResolver(new IResolver() {

            private boolean resolved = false;

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
                network.assertFacts(
                        network.leq(min, var),
                        network.leq(var, max)
                );
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                resolved = false;
            }
        });
        return var;
    }

    private static INumber createNumber(IType primitive_type, IConstraintNetwork network) {
        INumber var = null;
        switch (primitive_type.getName()) {
            case Constants.INT:
                var = network.newInt();
                break;
            case Constants.REAL:
                var = network.newReal();
                break;
            default:
                throw new AssertionError(primitive_type.getName());
        }
        return var;
    }
}
