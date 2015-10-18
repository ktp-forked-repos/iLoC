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
 * A listener for detecting changes of dynamic causal graphs. Dynamic causal
 * graphs listeners are typically used by graphical user interfaces for
 * graphically representing the state of the current dynamic causal graph.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IDynamicCausalGraphListener {

    /**
     * Notifies the listener that the formula {@code formula} has been added.
     *
     * @param formula the formula that has been added.
     */
    public void formulaAdded(IFormula formula);

    /**
     * Notifies the listener that the formula {@code formula} has been removed.
     *
     * @param formula the formula that has been removed.
     */
    public void formulaRemoved(IFormula formula);

    /**
     * Notifies the listener that the formula {@code formula} has been
     * activated.
     *
     * @param formula the formula of the dynamic causal graph which has been
     * activated.
     */
    public void formulaActivated(IFormula formula);

    /**
     * Notifies the listener that the formula {@code formula} has been unified.
     * Since unification could be dependant from the state of the constraint
     * network, it could be not possible to decide with which formula,
     * {@code formula} has been unified. For this reason, {@code formulas}
     * contains a collection of possible formulas with which formula
     * {@code formula} could have been unified. Finally, {@code constraints}
     * contains a collection of boolean variables. The index of the single
     * boolean variable whose value is {@code true} identifies the formula of
     * {@code formulas} with which formula {@code formula} has been unified.
     *
     * @param formula the formula of the dynamic causal graph has been unified.
     * @param formulas the list of possible formulas with which formula
     * {@code formula} could have been unified.
     * @param constraints the list of boolean variables identifying the with
     * which formula {@code formula} has been unified.
     */
    public void formulaUnified(IFormula formula, List<IFormula> formulas, List<IBool> constraints);

    /**
     * Notifies the listener that the formula {@code formula} has been
     * deactivated.
     *
     * @param formula the formula of the dynamic causal graph which has been
     * deactivated.
     */
    public void formulaInactivated(IFormula formula);
}
