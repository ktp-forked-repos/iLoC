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

/**
 * Represents the state of a formula. A formula can be either inactive (i.e.,
 * should be activated or unified), active (i.e., the rule associated to its
 * predicate has been applied) or unified with an already active formula.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public enum FormulaState {

    /**
     * The formula is inactive and, eventually, must be either activated or
     * unified with an already active formula.
     */
    Inactive,
    /**
     * The formula has is active, therefore the rule associated to its predicate
     * must have been applied.
     */
    Active,
    /**
     * The formula is unified with with another already active formula.
     */
    Unified;
}
