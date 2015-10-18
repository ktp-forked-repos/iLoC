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

import java.util.List;

/**
 * Represents a dynamic causal graph for the current problem. In general, a
 * dynamic causal graph is a directed graph whose nodes are formulas and arcs
 * are causal links between formulas.
 * <p>
 * Dynamic causal graphs depend both on the implicit structure of the domain and
 * on the current state of the solver.
 * <p>
 * Dynamic causal graphs can be exploited for the development of dynamic
 * heuristics.
 *
 * @author Riccardo De Benedictis
 */
public interface IDynamicCausalGraph {

    /**
     * Adds a formula to the current dynamic causal graph.
     * {@code formula.getCause()} returns the cause of the formula, that is, the
     * formula from which {@code formula} is generated. As a consequence, a
     * causal link from {@code formula.getCause()} and {@code formula} will be
     * added.
     *
     * @param formula the new formula for the current dynamic causal graph.
     */
    public void addFormula(IFormula formula);

    /**
     * Removes a formula from the current dynamic causal graph.
     *
     * @param formula the formula of the current dynamic causal graph to be
     * removed.
     */
    public void removeFormula(IFormula formula);

    /**
     * Notifies the dynamic causal graph that the formula {@code formula} has
     * been activated.
     *
     * @param formula the formula of the dynamic causal graph which has been
     * activated.
     */
    public void formulaActivated(IFormula formula);

    /**
     * Notifies the dynamic causal graph that the formula {@code formula} has
     * been unified. Since unification could be dependant from the state of the
     * constraint network, it could be not possible to decide with which
     * formula, {@code formula} has been unified. For this reason,
     * {@code formulas} contains a collection of possible formulas with which
     * formula {@code formula} could have been unified. Finally,
     * {@code constraints} contains a collection of boolean variables. The index
     * of the single boolean variable whose value is {@code true} identifies the
     * formula of {@code formulas} with which formula {@code formula} has been
     * unified.
     *
     * @param formula the formula of the dynamic causal graph has been unified.
     * @param formulas the list of possible formulas with which formula
     * {@code formula} could have been unified.
     * @param constraints the list of boolean variables identifying the with
     * which formula {@code formula} has been unified.
     */
    public void formulaUnified(IFormula formula, List<IFormula> formulas, List<IBool> constraints);

    /**
     * Notifies the dynamic causal graph that the formula {@code formula} has
     * been inactivated.
     *
     * @param formula the formula of the dynamic causal graph which has been
     * inactivated.
     */
    public void formulaInactivated(IFormula formula);

    /**
     * Adds a listener for detecting changes to this dynamic causal graph.
     *
     * @param listener the listener to be added.
     */
    public void addCausalGraphListener(IDynamicCausalGraphListener listener);

    /**
     * Removes a listener from this dynamic causal graph.
     *
     * @param listener the listener to be removed.
     */
    public void removeCausalGraphListener(IDynamicCausalGraphListener listener);
}
