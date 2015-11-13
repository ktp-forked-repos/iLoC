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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.ISolver;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Environment implements IEnvironment {

    private final ISolver solver;
    private final IEnvironment enclosing_environment;
    private final Map<String, IObject> objects = new HashMap<>();

    public Environment(ISolver solver, IEnvironment enclosing_environment) {
        assert solver != null;
        this.solver = solver;
        this.enclosing_environment = enclosing_environment;
    }

    @Override
    public ISolver getSolver() {
        return solver;
    }

    @Override
    public IEnvironment getEnclosingEnvironment() {
        return enclosing_environment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IObject> T get(String name) {
        IObject object = objects.get(name);
        if (object != null) {
            return (T) object;
        }

        // if not here, check any enclosing environment
        object = enclosing_environment.get(name);
        if (object != null) {
            return (T) object;
        }

        // not found
        return null;
    }

    @Override
    public Map<String, IObject> getObjects() {
        return Collections.unmodifiableMap(objects);
    }

    @Override
    public void set(String name, IObject object) {
        solver.getCurrentNode().addResolver(new IResolver() {
            private boolean resolved = false;
            private IObject old_value = null;

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
                old_value = objects.put(name, object);
                resolved = true;
            }

            @Override
            public void retract() {
                assert resolved;
                if (old_value == null) {
                    objects.remove(name);
                } else {
                    objects.put(name, old_value);
                }
                resolved = false;
            }
        });
    }
}
