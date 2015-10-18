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
package it.cnr.istc.iloc.api;

import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IDisjunction extends IScope {

    /**
     * Adds a disjunct to the ginev disjunction.
     *
     * @param disjunct the disjunct to be added to the given disjunction.
     */
    public void addDisjunct(IDisjunct disjunct);

    /**
     * Returns a collection containing all the disjuncts of the given
     * disjunction.
     *
     * @return a collection containing all the disjuncts of the given
     * disjunction.
     */
    public Collection<IDisjunct> getDisjuncts();
}
