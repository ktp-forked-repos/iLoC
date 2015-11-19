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
public interface INode {

    /**
     * Returns the solver this node belongs to.
     *
     * @return the solver this node belongs to.
     */
    public ISolver getSolver();

    /**
     * Returns the depth level of this node object.
     *
     * @return the depth level of this node object.
     */
    public int getLevel();

    /**
     * Returns the parent node of this node object.
     *
     * @return the parent node of this node object.
     */
    public INode getParent();

    /**
     * Returns the known cost of getting from the initial node to this node.
     * This value is a consequence of the cost of all the resolvers in all the
     * nodes from the initial node to this node.
     *
     * @return the known cost of getting from the initial node to this node.
     */
    public double getKnownCost();

    /**
     * Adds a resolver to this node object. If this {@code INode} is the current
     * node of the search space the resolved is resolved otherwise resolvers
     * will not be resolved until the {@link #resolve()} method is called on
     * this node.
     *
     * @param resolver the resolver to add to the current node.
     */
    public void addResolver(IResolver resolver);

    /**
     * Returns all the resolvers that have been added to this node object.
     *
     * @return the resolvers that have been added to this node object.
     */
    public Collection<IResolver> getResolvers();

    /**
     * Resolves the resolvers that have been added to this node object. If a
     * flaw has a single resolver, the resolver is added to the set of resolvers
     * and is resolved.
     */
    public void resolve();

    /**
     * Performs causal propagation (i.e., resolves all the flaws having a single
     * resolver). In order to avoid infinite propagation, a bound is given.
     *
     * @param bound a bound on the propagation.
     * @return {@code true} if propagation has succeeded, {@code false} if an
     * early inconsistency has been detected and {@code null} if the bound has
     * been reached.
     */
    public Boolean propagate(int bound);

    /**
     * Enqueues a flaw to this node object.
     *
     * @param flaw the flaw added to this node object.
     */
    public void enqueue(IFlaw flaw);

    /**
     * Selects the best flaw to be resolved according the cost of its resolvers.
     *
     * @return the best flaw to be resolved.
     */
    public IFlaw selectFlaw();

    /**
     * Returns a collection of all the flaws detected in the current search
     * space node.
     *
     * @return a collection of all the flaws detected in the current search
     * space node.
     */
    public Collection<IFlaw> getFlaws();

    /**
     * Checks the consistency of all the objects reachable from the current
     * search space node. This method returns a boolean representing the
     * possibility to make this node consistent. In case an unsolvable
     * inconsistency is detected, the solver will move to another search space
     * node.
     *
     * @param bound a bound on the propagation.
     * @return {@code true} if the node is consistent, {@code false} if an early
     * inconsistency has been detected and {@code null} if the bound has been
     * reached.
     */
    public Boolean checkConsistency(int bound);
}
