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
package it.cnr.istc.ac.api;

import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 * @param <T> the type of the allowed values.
 */
public interface IACEnum<T> extends IVar {

    /**
     * Returns the set of current allowed values.
     *
     * @return the set of current allowed values.
     */
    public Set<T> getAllowedValues();

    /**
     * Updates the current domain by removing all the values which are not
     * allowed in the given domain returning {@code true} if the resulting
     * domain is not empty.
     *
     * @param vals the intersection values.
     * @return {@code true} if the resulting domain is not empty.
     */
    public boolean intersect(Collection<T> vals);

    /**
     * Updates the current domain by removing all the values which are allowed
     * in the given domain returning {@code true} if the resulting domain is not
     * empty.
     *
     * @param vals the complement values.
     * @return {@code true} if the resulting domain is not empty.
     */
    public boolean complement(Collection<T> vals);

    /**
     * Removes the given value and returns {@code true} if the resulting domain
     * is not empty.
     *
     * @param value the value to be removed from this boolean domain.
     * @return {@code true} if the resulting domain is not empty.
     */
    public boolean remove(T value);

    /**
     * Checks if this domain is intersecting with the given domain.
     *
     * @param var the variable with which intersection is checked.
     * @return {@code true} if this domain is intersecting with the given
     * domain.
     */
    public default boolean isIntersecting(IACEnum<T> var) {
        Set<T> allowed_values = var.getAllowedValues();
        return this.getAllowedValues().stream().anyMatch(v -> allowed_values.contains(v));
    }

    @Override
    public default boolean isSingleton() {
        return getAllowedValues().size() == 1;
    }

    @Override
    public default boolean isEmpty() {
        return getAllowedValues().isEmpty();
    }
}
