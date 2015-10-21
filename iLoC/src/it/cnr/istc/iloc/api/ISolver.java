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

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface ISolver extends IScope, IEnvironment {

    /**
     * Reads a script defined in the iLoC language and performs initial
     * propagation.
     *
     * @param script the script to be read.
     * @return a model for evaluating the initial propagation or {@code null} if
     * the problem is inconsistent.
     */
    public IModel read(String script);

    /**
     * Reads a script from the given files defined in the iLoC language and
     * performs initial propagation.
     *
     * @param files an array of files containing the script to be read.
     * @return a model for evaluating the initial propagation or {@code null} if
     * the problem is inconsistent.
     * @throws IOException if an I/O exception of some sort has occurred.
     */
    public IModel read(File... files) throws IOException;

    /**
     * Returns the constraint network associated to this solver.
     *
     * @return the constraint network associated to this solver
     */
    public IConstraintNetwork getConstraintNetwork();

    /**
     * Returns the static causal graph associated to this solver.
     *
     * @return the static causal graph associated to this solver
     */
    public IStaticCausalGraph getStaticCausalGraph();

    /**
     * Returns the dynamic causal graph associated to this solver.
     *
     * @return the dynamic causal graph associated to this solver
     */
    public IDynamicCausalGraph getDynamicCausalGraph();

    /**
     * Traverse the search space till the given {@code INode} is reached. In
     * order to reach the given node, it might be required to backtrack to a
     * common ancestor node.
     * <p>
     * Notice that all the constraints of the target node are propagated.
     *
     * @param node the target node.
     * @return {@code true} if the target node is reachable and no inconsistency
     * has been detected.
     */
    public boolean goTo(INode node);

    /**
     * Solves the given reasoning problem by propagating constraints and
     * incrementally removing flaws. If the reasoning problem has a solution,
     * the resulting current {@code INode}, returned by
     * {@link ISolver#getCurrentNode()}, will be a node with fulfilled
     * constraints and an empty flaw set. In case it is not possible to find a
     * solution, the resulting current {@code INode} will be an inconsistent
     * node.
     *
     * @return {@code true} if a solution is found, {@code false} otherwise.
     */
    public boolean solve();

    /**
     * Solves the given reasoning problem by propagating constraints and
     * incrementally removing flaws while optimizing according to the given
     * optimization function. If the reasoning problem has an optimal solution,
     * the resulting current {@code INode}, returned by
     * {@link ISolver#getCurrentNode()}, will be a node with fulfilled
     * constraints, an empty flaw set and an optimal cost according to the
     * objective function. In case it is not possible to find an optimal
     * solution, the resulting current {@code INode} will be an inconsistent
     * node.
     *
     * @param objectiveFunction a numeric variable representing the objective
     * function.
     * @return {@code true} if an optimal solution is found, {@code false}
     * otherwise.
     */
    public boolean optimize(INumber objectiveFunction);

    /**
     * Solves the given flaw by generating a branch in the search tree.
     *
     * @param flaw the flaw to be solved.
     */
    public void solveFlaw(IFlaw flaw);

    /**
     * Retrieves the current search space {@code INode}.
     *
     * @return the current search space {@code INode}.
     */
    public INode getCurrentNode();

    /**
     * Returns the number of nodes of the search space.
     *
     * @return the number of nodes of the search space.
     */
    public int getSearchSpaceSize();

    /**
     * Returns the number of nodes in the fringe of the search space.
     *
     * @return the number of nodes in the fringe of the search space.
     */
    public int getFringeSize();

    public void addSolverListener(ISolverListener listener);

    public void removeSolverListener(ISolverListener listener);
}
