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
public interface IFlaw {

    /**
     * Returns a heuristic estimate of the cost to get from the current node to
     * any goal node. For the algorithm to find the actual shortest path, the
     * heuristic function must be admissible, meaning that it never
     * overestimates the actual cost to get to the nearest goal node.
     *
     * @return a heuristic estimate of the cost to get from the current node to
     * any goal node.
     */
    public double getEstimatedCost();

    /**
     * Returns the collection of resolvers for solving the flaw.
     *
     * @return the collection of resolvers for solving the flaw.
     */
    public Collection<IResolver> getResolvers();
}
