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

import it.cnr.istc.ac.api.IACBool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class BoolVar extends Var implements IACBool {

    protected final Set<Boolean> allowed_values;

    BoolVar(ACNetwork network, String name) {
        this(network, name, Arrays.asList(Boolean.TRUE, Boolean.FALSE));
    }

    BoolVar(ACNetwork network, String name, Collection<Boolean> allowed_values) {
        super(network, name);
        assert allowed_values.stream().allMatch(c -> c.equals(Boolean.TRUE) || c.equals(Boolean.FALSE));
        this.allowed_values = new HashSet<>(allowed_values);
    }

    @Override
    public Set<Boolean> getAllowedValues() {
        return Collections.unmodifiableSet(allowed_values);
    }

    @Override
    public boolean intersect(Collection<Boolean> vals) {
        Set<Boolean> to_remove = allowed_values.stream().filter(v -> !vals.contains(v)).collect(Collectors.toSet());
        if (!to_remove.isEmpty()) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain(n_updates, allowed_values));
            }
            allowed_values.removeAll(to_remove);
            network.enqueue(this);
            n_updates++;
            if (n_updates > network.getNbConstraints()) {
                return false;
            }
        }
        return !allowed_values.isEmpty();
    }

    @Override
    public boolean complement(Collection<Boolean> vals) {
        Set<Boolean> to_remove = allowed_values.stream().filter(v -> vals.contains(v)).collect(Collectors.toSet());
        if (!to_remove.isEmpty()) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain(n_updates, allowed_values));
            }
            allowed_values.removeAll(to_remove);
            network.enqueue(this);
            n_updates++;
            if (n_updates > network.getNbConstraints()) {
                return false;
            }
        }
        return !allowed_values.isEmpty();
    }

    @Override
    public boolean remove(Boolean value) {
        if (allowed_values.contains(value)) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain(n_updates, allowed_values));
            }
            allowed_values.remove(value);
            network.enqueue(this);
            n_updates++;
            if (n_updates > network.getNbConstraints()) {
                return false;
            }
        }
        return !allowed_values.isEmpty();
    }

    @Override
    void restoreDomain() {
        Domain old_domain = (Domain) network.stack.peekFirst().domains.get(this);
        this.n_updates = old_domain.n_updates;
        this.allowed_values.clear();
        this.allowed_values.addAll(old_domain.allowed_values);
    }

    @Override
    public String toString() {
        return isSingleton() ? allowed_values.iterator().next().toString() : name + " in {" + allowed_values.stream().map(b -> b.toString()).collect(Collectors.joining(", ")) + "}";
    }

    private static class Domain {

        private final int n_updates;
        protected final Collection<Boolean> allowed_values;

        Domain(int n_updates, Collection<Boolean> allowed_values) {
            this.n_updates = n_updates;
            this.allowed_values = new ArrayList<>(allowed_values);
        }
    }
}
