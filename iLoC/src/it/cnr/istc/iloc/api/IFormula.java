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
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IFormula extends IObject {

    /**
     * Returns the cause for this formula to exist. The returned formula, if
     * any, is the formula which unified with the rule that generated this
     * formula. The returned formula will be null if this formula is an initial
     * fact or goal.
     *
     * @return the cause for this formula to exist or {@code null} if this
     * formula is an initial fact or goal.
     */
    public IFormula getCause();

    /**
     * Returns the scope of this formula. The scope is an enumerative variable
     * whose allowed values are the objects on which the formula applies.
     *
     * @return an enumerative variable representing the scope of this formula.
     */
    public default IEnum getScope() {
        return get(Constants.SCOPE);
    }

    @Override
    public IPredicate getType();

    /**
     * Returns the current state of the formula.
     *
     * @return the current state of the formula.
     */
    public FormulaState getFormulaState();

    /**
     * Sets the state of this formula as active.
     */
    public void setActiveState();

    /**
     * Sets the state of this formula as active. Since unification could be
     * dependant from the state of the constraint network, it could be not
     * possible to decide with which formula, this formula has been unified. For
     * this reason, {@code formulas} contains a collection of possible formulas
     * with which formula this formula could have been unified. Finally,
     * {@code constraints} contains a collection of boolean variables. The index
     * of the single boolean variable whose value is {@code true} identifies the
     * formula of {@code formulas} with which this formula has been unified.
     *
     * @param formulas the list of possible formulas with which formula
     * {@code formula} could have been unified.
     * @param constraints the list of boolean variables identifying the with
     * which formula {@code formula} has been unified.
     */
    public void setUnifiedState(List<IFormula> formulas, List<IBool> constraints);

    /**
     * Sets the state of this formula as inactive.
     */
    public void setInactiveState();
}
