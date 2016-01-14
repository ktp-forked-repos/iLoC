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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IIntVar extends IVar {

    /**
     * Returns the minimum value in a domain.
     *
     * @return the smallest value present in the domain.
     */
    public Int getLB();

    /**
     * Returns the maximum value in a domain.
     *
     * @return the largest value present in the domain.
     */
    public Int getUB();

    /**
     * Updates the domain of this variable by removing all the values which are
     * not allowed in the given domain returning {@code true} if the resulting
     * domain is not empty.
     *
     * @param lb the lower bound with which intersection is performed.
     * @param ub the upper bound with which intersection is performed.
     * @return {@code true} if the resulting domain is not empty.
     */
    public boolean intersect(Int lb, Int ub);

    /**
     * Updates the domain of this variable by removing all the values which are
     * allowed in the given domain returning {@code true} if the resulting
     * domain is not empty.
     *
     * @param lb the lower bound with which complement is performed.
     * @param ub the upper bound with which complement is performed.
     * @return {@code true} if the resulting domain is not empty.
     */
    public boolean complement(Int lb, Int ub);

    /**
     * Checks if the domain of this variable is intersecting with the domain of
     * the given variable.
     *
     * @param var the variable with which intersection is checked.
     * @return {@code true} if this domain is intersecting with the given
     * domain.
     */
    public default boolean isIntersecting(IIntVar var) {
        return getUB().geq(var.getLB()) && getLB().leq(var.getUB());
    }

    /**
     * Checks if the domain of this variable is intersecting with the given
     * domain.
     *
     * @param lb the lower bound with which intersection is checked.
     * @param ub the upper bound with which intersection is checked.
     * @return {@code true} if this domain is intersecting with the given
     * domain.
     */
    public default boolean isIntersecting(Int lb, Int ub) {
        return getUB().geq(lb) && getLB().leq(ub);
    }

    @Override
    public default boolean isSingleton() {
        return getLB().eq(getUB());
    }

    @Override
    public default boolean isEmpty() {
        return getLB().gt(getUB());
    }
}
