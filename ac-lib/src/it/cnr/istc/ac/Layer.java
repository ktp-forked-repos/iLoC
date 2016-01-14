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
import it.cnr.istc.ac.api.IVar;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Layer {

    final Layer parent;
    /**
     * Maintains information about watched constraints by given variable.
     */
    final Map<IVar, Collection<IConstraint>> watched_constraints;
    /**
     * The set of variables inside the network.
     */
    final Set<IVar> vars;
    /**
     * The set of constraints inside the network.
     */
    final Set<IConstraint> constraints;
    /**
     * Maintains information about modified domains.
     */
    final Map<Var, Object> domains;

    Layer() {
        this.parent = null;
        this.watched_constraints = new IdentityHashMap<>();
        this.vars = new LinkedHashSet<>();
        this.constraints = new LinkedHashSet<>();
        this.domains = new IdentityHashMap<>();
    }

    Layer(Layer parent) {
        this.parent = parent;
        this.watched_constraints = new IdentityHashMap<>(parent.watched_constraints);
        this.vars = new LinkedHashSet<>(parent.vars);
        this.constraints = new LinkedHashSet<>(parent.constraints);
        this.domains = new IdentityHashMap<>();
    }
}
