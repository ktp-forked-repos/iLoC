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

import it.cnr.istc.ac.api.IACEnum;
import it.cnr.istc.ac.api.IConstraint;
import it.cnr.istc.ac.api.IVar;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class EBiE<T0, T1> extends BoolVar implements IConstraint {

    private final IACEnum<T0> v0;
    private final IACEnum<T1> v1;
    private final Map<T0, T1> v0_v1_map;
    private final Map<T1, T0> v1_v0_map;
    private final List<IVar> arguments;

    EBiE(ACNetwork network, IACEnum<T0> v0, IACEnum<T1> v1) {
        super(network, "(bi " + (v0.isSingleton() ? v0.toString() : v0.getName()) + " " + (v1.isSingleton() ? v1.toString() : v1.getName()) + ")", Arrays.asList(Boolean.TRUE, Boolean.FALSE));
        assert v0.getAllowedValues().size() == v1.getAllowedValues().size();
        this.v0 = v0;
        this.v1 = v1;
        this.v0_v1_map = new IdentityHashMap<>(v0.getAllowedValues().size());
        this.v1_v0_map = new IdentityHashMap<>(v1.getAllowedValues().size());
        Iterator<T0> v0_it = v0.getAllowedValues().iterator();
        Iterator<T1> v1_it = v1.getAllowedValues().iterator();
        while (v0_it.hasNext() && v1_it.hasNext()) {
            T0 v0_v = v0_it.next();
            T1 v1_v = v1_it.next();
            v0_v1_map.put(v0_v, v1_v);
            v1_v0_map.put(v1_v, v0_v);
        }
        this.arguments = Arrays.asList(this, v0, v1);
    }

    @Override
    public List<IVar> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public boolean propagate(IVar var) {
        Set<Boolean> c_allowed_values = getAllowedValues();
        if (isSingleton()) {
            Collection<T1> v0_vals = v0.getAllowedValues().stream().map(v -> v0_v1_map.get(v)).collect(Collectors.toList());
            Collection<T0> v1_vals = v1.getAllowedValues().stream().map(v -> v1_v0_map.get(v)).collect(Collectors.toList());
            if (c_allowed_values.contains(Boolean.TRUE)) {
                // The constraint must be satisfied..
                if (!v0.intersect(v1_vals)) {
                    return false;
                }
                if (!v1.intersect(v0_vals)) {
                    return false;
                }
            } else {
                // The constraint must be not satisfied..
                assert c_allowed_values.contains(Boolean.FALSE);
                if (!v0.complement(v1_vals)) {
                    return false;
                }
                if (!v1.complement(v0_vals)) {
                    return false;
                }
            }
        } else if (v0.isSingleton() && v1.isSingleton()) {
            if (v0.getAllowedValues().stream().anyMatch(v -> v1.getAllowedValues().contains(v0_v1_map.get(v)))) {
                // The constraint is already satisfied..
                if (!remove(Boolean.FALSE)) {
                    return false;
                }
            } else {
                // The constraint cannot be satisfied..
                assert v0.getAllowedValues().stream().noneMatch(v -> v1.getAllowedValues().contains(v0_v1_map.get(v)));
                if (!remove(Boolean.TRUE)) {
                    return false;
                }
            }
        }
        return true;
    }
}
