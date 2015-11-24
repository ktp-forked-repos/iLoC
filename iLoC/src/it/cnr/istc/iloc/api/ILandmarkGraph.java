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

import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface ILandmarkGraph {

    /**
     * Extract the landmarks from the given problem.
     */
    public void extractLandmarks();

    /**
     * Returns the computed (disjunctive) landmarks.
     *
     * @return a set containing the computed (disjunctive) landmarks.
     */
    public Set<ILandmark> getLandmarks();

    /**
     * Gets the landmark fringe. This set represents all those landmarks which
     * are not dependent from other landmarks.
     *
     * @return a set of landmarks which are not dependent from other landmarks.
     */
    public Set<ILandmark> getLandmarksFringe();

    /**
     * Recomputes the estimated costs of the heuristic estimator.
     */
    public void recomputeEstimatedCosts();

    /**
     * Returns an estimated cost for the given node according to this landmark
     * graph.
     *
     * @param node the node to be estimated.
     * @return a value indicating an estimated cost for solving the flaw
     * associated to the given causal node.
     */
    public double estimate(IStaticCausalGraph.INode node);

    /**
     * Represents a (disjunctive) landmark.
     */
    public interface ILandmark {

        /**
         * Returns the static causal graph nodes which constitute this
         * disjunctive landmark.
         *
         * @return a set of static causal graph nodes representing the
         * disjunctive landmark.
         */
        public Set<IStaticCausalGraph.INode> getNodes();
    }
}
