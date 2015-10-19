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
package it.cnr.istc.iloc.pddl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Type {

    static final Type OBJECT = new Type("Object");
    static final Type NUMBER = new Type("number");
    private final String name;
    private Type superclass;
    private final Collection<Constant> instances = new ArrayList<>();

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Type getSuperclass() {
        return superclass;
    }

    public void setSuperclass(Type superclass) {
        this.superclass = superclass;
    }

    public Constant newInstance(String name) {
        Constant instance = new Constant(name, this);
        addInstance(instance);
        return instance;
    }

    public void addInstance(Constant instance) {
        instances.add(instance);
        if (superclass != null) {
            superclass.addInstance(instance);
        }
    }

    public Collection<Constant> getInstances() {
        return Collections.unmodifiableCollection(instances);
    }

    @Override
    public String toString() {
        return name + (superclass != null ? " extends " + superclass.name : "");
    }
}
