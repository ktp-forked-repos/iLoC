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
package it.cnr.istc.smt.api;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface ISMTNetwork {

    /**
     * Creates and returns a new boolean variable.
     *
     * @return a new boolean variable.
     */
    public ISMTBool newBool();

    /**
     * Creates and returns a new boolean variable having a {@code constant}
     * value. Allowed values for boolean variables are "true" and "false".
     *
     * @param constant the constant value of the variable.
     * @return a new boolean variable.
     */
    public ISMTBool newBool(String constant);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the boolean variable of which the negation has to be computed.
     * @return a new boolean variable which represents the negation of the
     * {@code val} variable.
     */
    public ISMTBool not(ISMTBool var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the equality of the two boolean variables.
     *
     * @param var0 a first boolean variable.
     * @param var1 a second boolean variable.
     * @return a new boolean variable which represents the equality of the two
     * boolean variables.
     */
    public ISMTBool eq(ISMTBool var0, ISMTBool var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional conjunction of the given boolean variables.
     *
     * @param vars an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional conjunction of the given boolean variables.
     */
    public ISMTBool and(ISMTBool... vars);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional disjunction of the given boolean variables.
     *
     * @param vars an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional disjunction of the given boolean variables.
     */
    public ISMTBool or(ISMTBool... vars);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional exclusive disjunction of the given boolean variables.
     *
     * @param var0 a first boolean variable.
     * @param var1 a second boolean variable.
     * @return a new instance of a boolean variable which represents the
     * propositional exclusive disjunction of the given boolean variables.
     */
    public ISMTBool xor(ISMTBool var0, ISMTBool var1);

    /**
     * Creates and returns a new integer variable.
     *
     * @return a new integer variable.
     */
    public ISMTInt newInt();

    /**
     * Creates and returns a new integer variable having a {@code constant}
     * value.
     *
     * @param constant the constant value of the variable.
     * @return a new integer variable.
     */
    public ISMTInt newInt(String constant);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public ISMTInt add(ISMTInt... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param var0 the dividend of the division.
     * @param var1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public ISMTInt divide(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public ISMTInt multiply(ISMTInt... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param var0 the minuend of the subtraction.
     * @param var1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public ISMTInt subtract(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public ISMTInt negate(ISMTInt var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool leq(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool geq(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool lt(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool gt(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool eq(ISMTInt var0, ISMTInt var1);

    /**
     * Creates and returns a new real variable.
     *
     * @return a new real variable.
     */
    public ISMTReal newReal();

    /**
     * Creates and returns a new real variable having a {@code constant} value.
     *
     * @param constant the constant value of the variable.
     * @return a new real variable.
     */
    public ISMTReal newReal(String constant);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public ISMTReal add(ISMTReal... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param var0 the dividend of the division.
     * @param var1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public ISMTReal divide(ISMTReal var0, ISMTReal var1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param vars an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public ISMTReal multiply(ISMTReal... vars);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param var0 the minuend of the subtraction.
     * @param var1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public ISMTReal subtract(ISMTReal var0, ISMTReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param var the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public ISMTReal negate(ISMTReal var);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool leq(ISMTReal var0, ISMTReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool geq(ISMTReal var0, ISMTReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool lt(ISMTReal var0, ISMTReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool gt(ISMTReal var0, ISMTReal var1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param var0 the first term of the constraint.
     * @param var1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public ISMTBool eq(ISMTReal var0, ISMTReal var1);

    /**
     * Casts an integer variable to a real variable.
     *
     * @param val the integer variable to cast.
     * @return the casted real variable.
     */
    public ISMTReal toReal(ISMTInt val);

    /**
     * Casts a real variable to an integer variable.
     *
     * @param val the real variable to cast.
     * @return the casted integer variable.
     */
    public ISMTInt toInt(ISMTReal val);

    /**
     * Asserts the facts in the current constraint network. By asserting a fact,
     * we are forcing the fact to be true.
     *
     * @param facts the facts to be asserted.
     */
    public void assertFacts(ISMTBool... facts);

    /**
     * Checks the facts in the current constraint network and returns
     * {@code true} if no inconsistency is found.
     *
     * @param facts the facts to be checked.
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean checkFacts(ISMTBool... facts);

    /**
     * Propagates all constraint of the constraint network returning a boolean
     * asserting whether the constraint network is consistent.
     *
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean propagate();

    /**
     * Returns a model for this given constraint network.
     *
     * @return a model for this given constraint network.
     */
    public IModel getModel();

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

    /**
     * Resets the given constraint network.
     */
    public void reset();
}
