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

import it.cnr.istc.ac.api.IEnumVar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EnumVar<T> extends Var implements IEnumVar<T> {

    private final Set<T> allowed_values;

    EnumVar(ACNetwork network, String name, Collection<T> allowed_values) {
        super(network, name);
        this.allowed_values = new LinkedHashSet<>(allowed_values);
    }

    @Override
    public Set<T> getAllowedValues() {
        return Collections.unmodifiableSet(allowed_values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean intersect(Collection<T> vals) {
        Set<T> to_remove = allowed_values.stream().filter(v -> !vals.contains(v)).collect(Collectors.toSet());
        if (!to_remove.isEmpty()) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain<>(n_updates, allowed_values));
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
    @SuppressWarnings("unchecked")
    public boolean complement(Collection<T> vals) {
        Set<T> to_remove = allowed_values.stream().filter(v -> vals.contains(v)).collect(Collectors.toSet());
        if (!to_remove.isEmpty()) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain<>(n_updates, allowed_values));
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
    @SuppressWarnings("unchecked")
    public boolean remove(T value) {
        if (allowed_values.contains(value)) {
            if (!network.stack.peekFirst().domains.containsKey(this)) {
                network.stack.peekFirst().domains.put(this, new Domain<>(n_updates, allowed_values));
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
    @SuppressWarnings("unchecked")
    void restoreDomain() {
        Domain<T> old_domain = (Domain<T>) network.stack.peekFirst().domains.get(this);
        this.n_updates = old_domain.n_updates;
        this.allowed_values.clear();
        this.allowed_values.addAll(old_domain.allowed_values);
    }

    @Override
    public String toString() {
        return isSingleton() ? allowed_values.iterator().next().toString() : name + " in {" + allowed_values.stream().map(v -> v.toString()).collect(Collectors.joining(", ")) + "}";
    }

    private static class Domain<T> {

        private final int n_updates;
        protected final Collection<T> allowed_values;

        Domain(int n_updates, Collection<T> allowed_values) {
            this.n_updates = n_updates;
            this.allowed_values = new ArrayList<>(allowed_values);
        }
    }
}
