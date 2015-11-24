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
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IRelaxedPlanningGraph {

    /**
     * Extracts the relaxed planning graph.
     */
    public void extract();

    /**
     * Propagates contraints for computing the extracted relaxed planning graph.
     *
     * @return {@code true} if propagation has succeeded.
     */
    public boolean propagate();

    /**
     * Disables the given node inside this relaxed planning graph. Disabling a
     * node will result in disabling dependant nodes.
     *
     * @param node the node to be disabled inside this relaxed planning graph.
     */
    public void disable(IStaticCausalGraph.INode node);

    /**
     * Pushes the relaxed planning graph by adding a new layer to the constraint
     * network containing new variables and new constraints.
     */
    public void push();

    /**
     * Pops the relaxed planning graph removing the topmost layer, removing
     * added variables and retracting constraints.
     */
    public void pop();

    /**
     * Returns the level of the given node inside this relaxed planning graph.
     *
     * @param node the node whose level is required.
     * @return the level of the given node inside this relaxed planning graph.
     */
    public double level(IStaticCausalGraph.INode node);
}
