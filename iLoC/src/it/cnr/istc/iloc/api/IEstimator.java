/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.iloc.api;

/**
 * Represents an heuristic estimator for estimating the cost for solving a flaw.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IEstimator {

    /**
     * Recomputes all the currently estimated costs.
     */
    public void recomputeCosts();

    /**
     * Returns an estimated cost for the given node according to this heuristic
     * estimator.
     *
     * @param node the node to be estimated.
     * @return a value indicating an estimated cost for solving the flaw
     * associated to the given node.
     */
    public double estimate(IStaticCausalGraph.INode node);
}
