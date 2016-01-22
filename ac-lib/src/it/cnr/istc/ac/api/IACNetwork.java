/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.ac.api;

import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IACNetwork {

    /**
     * Returns the number of variables.
     *
     * @return the number of variables.
     */
    public int getNbVars();

    /**
     * Returns the number of constraints.
     *
     * @return the number of constraints.
     */
    public int getNbConstraints();

    /**
     * Creates and returns a new boolean variable .
     *
     * @return a new boolean variable.
     */
    public IACBool newBool();

    /**
     * Creates and returns a new boolean variable having a {@code constant}
     * value. Allowed values for boolean variables are "true" and "false".
     *
     * @param constant the constant value of the variable.
     * @return a new boolean variable.
     */
    public IACBool newBool(String constant);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the boolean variable of which the negation has to be computed.
     * @return a new boolean variable which represents the negation of the
     * {@code val} variable.
     */
    public IACBool not(IACBool var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the equality of the two boolean variables.
     *
     * @param var0 a first boolean variable.
     * @param var1 a second boolean variable.
     * @return a new boolean variable which represents the equality of the two
     * boolean variables.
     */
    public IACBool eq(IACBool var0, IACBool var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional conjunction of the given boolean variables.
     *
     * @param vars an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional conjunction of the given boolean variables.
     */
    public IACBool and(IACBool... vars);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional disjunction of the given boolean variables.
     *
     * @param vars an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional disjunction of the given boolean variables.
     */
    public IACBool or(IACBool... vars);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional exclusive disjunction of the given boolean variables.
     *
     * @param var0 a first boolean variable.
     * @param var1 a second boolean variable.
     * @return a new instance of a boolean variable which represents the
     * propositional exclusive disjunction of the given boolean variables.
     */
    public IACBool xor(IACBool var0, IACBool var1);

    /**
     * Creates and returns a new integer variable .
     *
     * @return a new integer variable.
     */
    public IACInt newInt();

    /**
     * Creates and returns a new integer variable having a {@code constant}.
     *
     * @param constant the constant value of the variable.
     * @return a new integer variable.
     */
    public IACInt newInt(String constant);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public IACInt add(IACInt... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param var0 the dividend of the division.
     * @param var1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public IACInt divide(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public IACInt multiply(IACInt... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param var0 the minuend of the subtraction.
     * @param var1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public IACInt subtract(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public IACInt negate(IACInt var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool leq(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool geq(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool lt(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool gt(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool eq(IACInt var0, IACInt var1);

    /**
     * Creates and returns a new real variable .
     *
     * @return a new real variable.
     */
    public IACReal newReal();

    /**
     * Creates and returns a new real variable having a {@code constant} value.
     *
     * @param constant the constant value of the variable.
     * @return a new real variable.
     */
    public IACReal newReal(String constant);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public IACReal add(IACReal... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param var0 the dividend of the division.
     * @param var1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public IACReal divide(IACReal var0, IACReal var1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public IACReal multiply(IACReal... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param var0 the minuend of the subtraction.
     * @param var1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public IACReal subtract(IACReal var0, IACReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public IACReal negate(IACReal var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool leq(IACReal var0, IACReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool geq(IACReal var0, IACReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool lt(IACReal var0, IACReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool gt(IACReal var0, IACReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IACBool eq(IACReal var0, IACReal var1);

    /**
     * Casts an integer variable to a real variable .
     *
     * @param var the integer variable to cast.
     * @return the casted real variable.
     */
    public IACReal toReal(IACInt var);

    /**
     * Casts a real variable to an integer variable .
     *
     * @param var the real variable to cast.
     * @return the casted integer variable.
     */
    public IACInt toInt(IACReal var);

    /**
     * Creates and returns a new enumerative variable having the given allowed
     * values.
     *
     * @param <T> the type of the allowed values.
     * @param vals the allowed values of the new enumerative variable.
     * @return a new enumerative variable.
     */
    public <T> IACEnum<T> newEnum(Collection<T> vals);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the equality of the two enumerative variables.
     *
     * @param <T> the type of the enumerative variables' allowed values.
     * @param var0 a first enumerative variable.
     * @param var1 a second enumerative variable.
     * @return a new boolean variable which represents the equality of the two
     * enumerative variables.
     */
    public <T> IACBool eq(IACEnum<T> var0, IACEnum<T> var1);

    /**
     * Creates a bijection, or one-to-one correspondence, between the elements
     * of two sets. Every element of one set is paired with exactly one element
     * of the other set, according to their order.
     *
     * @param <T0> the type of the first enumerative variables' allowed values.
     * @param <T1> the type of the second enumerative variables' allowed values.
     * @param var0 a first enumerative variable.
     * @param var1 a second enumerative variable.
     * @return a new boolean variable which represents the bijection between the
     * two enumerative variables.
     */
    public <T0, T1> IACBool bijection(IACEnum<T0> var0, IACEnum<T1> var1);

    /**
     * Adds the given variable to the propagation queue.
     *
     * @param var the variable to be added to the propagation queue.
     */
    public void enqueue(IVar var);

    /**
     * Asserts the facts in the current constraint network. By asserting a fact,
     * we are forcing the fact to be true.
     *
     * @param facts the facts to be asserted.
     */
    public void assertFacts(IACBool... facts);

    /**
     * Checks the facts in the current constraint network and returns
     * {@code true} if no inconsistency is found.
     *
     * @param facts the facts to be checked.
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean checkFacts(IACBool... facts);

    /**
     * Propagates all constraint of the constraint network returning a boolean
     * asserting whether the constraint network is consistent.
     *
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean propagate();

    /**
     * Pushes the constraint network adding a new layer to the constraint
     * network containing new variables and new constraints.
     */
    public void push();

    /**
     * Pops the constraint network removing the topmost layer, removing added
     * variables and retracting constraints.
     */
    public void pop();
}
