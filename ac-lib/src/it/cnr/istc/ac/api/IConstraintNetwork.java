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
public interface IConstraintNetwork {

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
    public IBoolVar newBool();

    /**
     * Creates and returns a new boolean variable having a {@code constant}
     * value. Allowed values for boolean variables are "true" and "false".
     *
     * @param constant the constant value of the variable.
     * @return a new boolean variable.
     */
    public IBoolVar newBool(String constant);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the boolean variable of which the negation has to be computed.
     * @return a new boolean variable which represents the negation of the
     * {@code val} variable.
     */
    public IBoolVar not(IBoolVar var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the equality of the two boolean variables.
     *
     * @param var0 a first boolean variable.
     * @param var1 a second boolean variable.
     * @return a new boolean variable which represents the equality of the two
     * boolean variables.
     */
    public IBoolVar eq(IBoolVar var0, IBoolVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional conjunction of the given boolean variables.
     *
     * @param vars an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional conjunction of the given boolean variables.
     */
    public IBoolVar and(IBoolVar... vars);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional disjunction of the given boolean variables.
     *
     * @param vars an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional disjunction of the given boolean variables.
     */
    public IBoolVar or(IBoolVar... vars);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional exclusive disjunction of the given boolean variables.
     *
     * @param var0 a first boolean variable.
     * @param var1 a second boolean variable.
     * @return a new instance of a boolean variable which represents the
     * propositional exclusive disjunction of the given boolean variables.
     */
    public IBoolVar xor(IBoolVar var0, IBoolVar var1);

    /**
     * Creates and returns a new integer variable .
     *
     * @return a new integer variable.
     */
    public IIntVar newInt();

    /**
     * Creates and returns a new integer variable having a {@code constant}.
     *
     * @param constant the constant value of the variable.
     * @return a new integer variable.
     */
    public IIntVar newInt(String constant);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public IIntVar add(IIntVar... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param var0 the dividend of the division.
     * @param var1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public IIntVar divide(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public IIntVar multiply(IIntVar... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param var0 the minuend of the subtraction.
     * @param var1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public IIntVar subtract(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public IIntVar negate(IIntVar var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar leq(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar geq(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar lt(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar gt(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar eq(IIntVar var0, IIntVar var1);

    /**
     * Creates and returns a new real variable .
     *
     * @return a new real variable.
     */
    public IRealVar newReal();

    /**
     * Creates and returns a new real variable having a {@code constant} value.
     *
     * @param constant the constant value of the variable.
     * @return a new real variable.
     */
    public IRealVar newReal(String constant);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public IRealVar add(IRealVar... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param var0 the dividend of the division.
     * @param var1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public IRealVar divide(IRealVar var0, IRealVar var1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public IRealVar multiply(IRealVar... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param var0 the minuend of the subtraction.
     * @param var1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public IRealVar subtract(IRealVar var0, IRealVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public IRealVar negate(IRealVar var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar leq(IRealVar var0, IRealVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar geq(IRealVar var0, IRealVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar lt(IRealVar var0, IRealVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar gt(IRealVar var0, IRealVar var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBoolVar eq(IRealVar var0, IRealVar var1);

    /**
     * Casts an integer variable to a real variable .
     *
     * @param var the integer variable to cast.
     * @return the casted real variable.
     */
    public IRealVar toReal(IIntVar var);

    /**
     * Casts a real variable to an integer variable .
     *
     * @param var the real variable to cast.
     * @return the casted integer variable.
     */
    public IIntVar toInt(IRealVar var);

    /**
     * Creates and returns a new enumerative variable having the given allowed
     * values.
     *
     * @param <T> the type of the allowed values.
     * @param vals the allowed values of the new enumerative variable.
     * @return a new enumerative variable.
     */
    public <T> IEnumVar<T> newEnum(Collection<T> vals);

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
    public <T> IBoolVar eq(IEnumVar<T> var0, IEnumVar<T> var1);

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
    public void assertFacts(IBoolVar... facts);

    /**
     * Checks the facts in the current constraint network and returns
     * {@code true} if no inconsistency is found.
     *
     * @param facts the facts to be checked.
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean checkFacts(IBoolVar... facts);

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
