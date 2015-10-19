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
public interface IConstraintNetwork {

    /**
     * Creates and returns a new boolean variable.
     *
     * @return a new boolean variable.
     */
    public IBool newBool();

    /**
     * Creates and returns a new boolean variable having a {@code constant}
     * value. Allowed values for boolean variables are "true" and "false".
     *
     * @param constant the constant value of the variable.
     * @return a new boolean variable.
     */
    public IBool newBool(String constant);

    /**
     * Creates and returns a new integer variable.
     *
     * @return a new integer variable.
     */
    public IInt newInt();

    /**
     * Creates and returns a new integer variable having a {@code constant}
     * value.
     *
     * @param constant the constant value of the variable.
     * @return a new integer variable.
     */
    public IInt newInt(String constant);

    /**
     * Creates and returns a new real variable.
     *
     * @return a new real variable.
     */
    public IReal newReal();

    /**
     * Creates and returns a new real variable having a {@code constant} value.
     *
     * @param constant the constant value of the variable.
     * @return a new real variable.
     */
    public IReal newReal(String constant);

    /**
     * Casts an integer variable to a real variable.
     *
     * @param val the integer variable to cast.
     * @return the casted real variable.
     */
    public IReal toReal(IInt val);

    /**
     * Casts a real variable to an integer variable.
     *
     * @param val the real variable to cast.
     * @return the casted integer variable.
     */
    public IInt toInt(IReal val);

    /**
     * Returns a boolean variable representing whether the given real variable
     * is an integer variable.
     *
     * @param val the numeric variable to check.
     * @return a boolean variable representing whether the given real variable
     * is an integer variable.
     */
    public IBool isInt(IReal val);

    /**
     * Creates and returns a new string variable.
     *
     * @return a new string variable.
     */
    public IString newString();

    /**
     * Creates and returns a new string variable having a {@code constant}
     * value.
     *
     * @param constant the constant value of the variable.
     * @return a new string variable.
     */
    public IString newString(String constant);

    /**
     * Creates and returns a new enumerative variable having the given objects
     * as allowed variables.
     *
     * @param type the type of the allowed values of the enumerative variable.
     * @param values the allowed values of the enumerative variable.
     * @return an enumerative variable having the given objects as allowed
     * variables.
     */
    public IEnum newEnum(IType type, List<? extends IObject> values);

    /**
     * Creates and returns a new enumerative variable having the given integer
     * variable as controller variable and the given objects as allowed
     * variables.
     *
     * @param type the type of the allowed values of the enumerative variable.
     * @param var the controller variable.
     * @param values the allowed values of the enumerative variable.
     * @return an enumerative variable having the given objects as allowed
     * variables.
     */
    public IEnum newEnum(IType type, IInt var, List<? extends IObject> values);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param val the boolean variable of which the negation has to be computed.
     * @return a new boolean variable which represents the negation of the
     * {@code val} variable.
     */
    public IBool not(IBool val);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the equality of the two boolean variables.
     *
     * @param val0 a first boolean variable.
     * @param val1 a second boolean variable.
     * @return a new boolean variable which represents the equality of the two
     * boolean variables.
     */
    public IBool eq(IBool val0, IBool val1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional conjunction of the given boolean variables.
     *
     * @param val an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional conjunction of the given boolean variables.
     */
    public IBool and(IBool... val);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional disjunction of the given boolean variables.
     *
     * @param val an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional disjunction of the given boolean variables.
     */
    public IBool or(IBool... val);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the propositional exclusive disjunction of the given boolean variables.
     *
     * @param val an array of boolean variables.
     * @return a new instance of a boolean variable which represents the
     * propositional exclusive disjunction of the given boolean variables.
     */
    public IBool xor(IBool... val);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the sum of the given numerical variables.
     *
     * @param val an array of numerical variables.
     * @return a new instance of a numerical variable which represents the sum
     * of the given numerical variables.
     */
    public INumber add(INumber... val);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the division of the given numerical variables.
     *
     * @param val0 the dividend of the division.
     * @param val1 the divisor of the division.
     * @return a new instance of a numerical variable which represents the
     * division of the given numerical variables.
     */
    public INumber divide(INumber val0, INumber val1);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the product of the given numerical variables.
     *
     * @param val an array of numerical variables.
     * @return a new instance of a numerical variable which represents the
     * product of the given numerical variables.
     */
    public INumber multiply(INumber... val);

    /**
     * Creates and returns a new instance of a numerical variable which
     * represents the subtraction of the given numerical variables.
     *
     * @param val0 the minuend of the subtraction.
     * @param val1 the subtrahend of the subtraction.
     * @return a new instance of a numerical variable which represents the
     * subtraction of the given numerical variables.
     */
    public INumber subtract(INumber val0, INumber val1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the negation of the {@code val} variable.
     *
     * @param val the numeric variable of which the negation has to be computed.
     * @return a new numeric variable which represents the negation of the
     * {@code val} variable.
     */
    public INumber negate(INumber val);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 <= val1}.
     *
     * @param val0 the first term of the constraint.
     * @param val1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBool leq(INumber val0, INumber val1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 >= val1}.
     *
     * @param val0 the first term of the constraint.
     * @param val1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBool geq(INumber val0, INumber val1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 < val1}.
     *
     * @param val0 the first term of the constraint.
     * @param val1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBool lt(INumber val0, INumber val1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 > val1}.
     *
     * @param val0 the first term of the constraint.
     * @param val1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBool gt(INumber val0, INumber val1);

    /**
     * Creates and returns a new instance of a boolean variable which represents
     * the satisfaction of the constraint {@code val0 == val1}.
     *
     * @param val0 the first term of the constraint.
     * @param val1 the second term of the constraint.
     * @return a boolean variable which represents the satisfaction of the
     * constraint.
     */
    public IBool eq(INumber val0, INumber val1);

    /**
     * Asserts the facts in the current constraint network. By asserting a fact,
     * we are forcing the fact to be true.
     *
     * @param facts the facts to be asserted.
     */
    public void assertFacts(IBool... facts);

    /**
     * Checks the facts in the current constraint network and returns
     * {@code true} if no inconsistency is found.
     *
     * @param facts the facts to be checked.
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean checkFacts(IBool... facts);

    /**
     * Propagates all constraint of the constraint network returning a boolean
     * asserting whether the constraint network is consistent.
     *
     * @return {@code false} if some inconsistency has been detected.
     */
    public boolean propagate();

    /**
     * Minimizes the constraint network according to the given objective
     * function.
     *
     * @param objective_function the objective function according to which you
     * want to minimize.
     */
    public void minimize(INumber objective_function);

    /**
     * Maximizes the constraint network according to the given objective
     * function.
     *
     * @param objective_function the objective function according to which you
     * want to maximize.
     */
    public void maximize(INumber objective_function);

    /**
     * Returns a model for the given constraint network.
     *
     * @return a model for the given constraint network.
     */
    public IModel getModel();

    /**
     * Pushes the constraint network adding a new layer to the constraint
     * network containing new variables and new constraints.
     */
    public void push();

    /**
     * Pops the constraint network removing the topmost layer removing added
     * variables and retracting constraints.
     */
    public void pop();

    public void reset();
}
