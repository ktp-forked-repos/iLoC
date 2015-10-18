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

import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IPredicate extends IType {

    /**
     * Creates a new fact formula having as predicate this predicate. The new
     * fact formula will have the given cause, if any, and the given
     * assignments, if any.
     *
     * @param cause the cause of the fact, if any, or {@code null}.
     * @param assignments the assignments for the fact, if any, or an empty
     * {@code Map}.
     * @return the new fact formula having as predicate this predicate.
     */
    public IFormula newFact(IFormula cause, Map<String, IObject> assignments);

    /**
     * Creates a new goal formula having as predicate this predicate. The new
     * goal formula will have the given cause, if any, and the given
     * assignments, if any.
     *
     * @param cause the cause of the goal, if any, or {@code null}.
     * @param assignments the assignments for the goal, if any, or an empty
     * {@code Map}.
     * @return the new goal formula having as predicate this predicate.
     */
    public IFormula newGoal(IFormula cause, Map<String, IObject> assignments);

    /**
     * Applies the rule having this predicate as head after the unification of
     * the given formula with the head of the rule.
     *
     * @param formula the formula which unifies with the head of the rule.
     */
    public void applyRule(IFormula formula);
}
