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

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IScope {

    /**
     * Returns the solver enclosing this scope.
     *
     * @return the solver enclosing this scope.
     */
    public ISolver getSolver();

    /**
     * Returns the immediately enclosing scope of the underlying scope. If the
     * underlying scope is a top level scope this method returns {@code null}.
     *
     * @return the immediately enclosing scope of the underlying scope.
     */
    public IScope getEnclosingScope();

    /**
     * Returns an {@code IField} object that reflects the specified public
     * member field of this {@code IScope} object. The {@code name} parameter is
     * a {@code String} specifying the simple name of the desired field.
     *
     * @param name the field name.
     * @return the {@code IField} object of this class specified by
     * {@code name}.
     * @throws NoSuchFieldException if a field with the specified name is not
     * found.
     */
    public IField getField(String name) throws NoSuchFieldException;

    /**
     * Returns a {@code Map} containing all the fields defined in the current
     * scope. The map has the field name as key.
     *
     * @return a {@code Map} containing all the fields defined in the current
     * scope.
     */
    public Map<String, IField> getFields();

    /**
     * Defines a field in the current scope.
     *
     * @param field the field to define in the current scope.
     */
    public void defineField(IField field);

    /**
     * Returns a {@code IMethod} object that reflects the specified public
     * member method of the class or interface represented by this
     * {@code IScope} object. The {@code name} parameter is a {@code String}
     * specifying the simple name of the desired method. The
     * {@code parameterTypes} parameter is an array of {@code IType} objects
     * that identify the method's formal parameter types, in declared order. If
     * {@code parameterTypes} is {@code null}, it is treated as if it were an
     * empty array.
     *
     * @param name the name of the method.
     * @param parameterTypes the list of parameters.
     * @return the {@code IMethod} object that matches the specified
     * {@code name} and {@code parameterTypes}.
     * @throws NoSuchMethodException if a matching method is not found.
     */
    public IMethod getMethod(String name, IType... parameterTypes) throws NoSuchMethodException;

    /**
     * Returns a collection containing {@code IMethod} objects reflecting all
     * the public methods of the class or interface represented by this {@code
     * IScope} object.
     *
     * @return the collection of {@code IMethod} objects representing the public
     * methods of this object.
     */
    public Collection<IMethod> getMethods();

    /**
     * Defines a method for this {@code IScope} object.
     *
     * @param method the defined method for this {@code IScope} object.
     */
    public void defineMethod(IMethod method);

    /**
     * Returns a predicate whose scope is within this {@code IType} object
     * scope. If a predicate is not found within this scope, the enclosing scope
     * is checked.
     *
     * @param name the name of the predicate to find.
     * @return a predicate having the given name.
     * @throws ClassNotFoundException if a predicate having the given name is
     * not found.
     */
    public IPredicate getPredicate(String name) throws ClassNotFoundException;

    /**
     * Returns all the predicates enclosed by this {@code IType} object mapped
     * by their names.
     *
     * @return a map containing all the predicates enclosed by this type having
     * type names as indexes.
     */
    public Map<String, IPredicate> getPredicates();

    /**
     * Defines a new predicate having this {@code IType} object as scope.
     *
     * @param predicate the predicate having this {@code IType} object as scope.
     */
    public void definePredicate(IPredicate predicate);

    /**
     * Notifies subclasses that a new predicate has been defined. This method
     * could be useful for adding further implicit parameters to the predicate
     * according to the specific subclass implementation.
     *
     * @param predicate the just defined predicate.
     */
    public default void predicateDefined(IPredicate predicate) {
    }

    /**
     * Returns a type whose scope is within this {@code IScope} object scope. If
     * a type is not found within this scope, the enclosing scope is checked.
     *
     * @param name the name of the type to find.
     * @return a type having the given name.
     * @throws ClassNotFoundException if a type having the given name is not
     * found.
     */
    public IType getType(String name) throws ClassNotFoundException;

    /**
     * Returns all the types enclosed by this {@code IType} object mapped by
     * their names.
     *
     * @return a map containing all the types enclosed by this type having type
     * names as indexes.
     */
    public Map<String, IType> getTypes();

    /**
     * Defines a new type as a subtype of this {@code IType} object.
     *
     * @param type the type defined as a subtype of this {@code IType} object.
     */
    public void defineType(IType type);

    /**
     * Notifies subclasses that a new formula has been created. Subclasses might
     * override this method for adding further constraints to the formula.
     *
     * @param formula the just created formula.
     */
    public default void formulaCreated(IFormula formula) {
    }

    /**
     * Notifies subclasses that a new fact has been created.
     *
     * @param fact the just created fact.
     */
    public default void factCreated(IFormula fact) {
        formulaCreated(fact);
    }

    /**
     * Notifies subclasses that a new goal has been created.
     *
     * @param goal the just created goal.
     */
    public default void goalCreated(IFormula goal) {
        formulaCreated(goal);
    }

    /**
     * Notifies subclasses that a new formula has been activated.
     *
     * @param formula the just activated formula.
     */
    public default void formulaActivated(IFormula formula) {
    }

    /**
     * Notifies subclasses that a new formula has been unified. Since
     * unification could be dependant from the state of the constraint network,
     * it could be not possible to decide with which formula, {@code formula}
     * has been unified. For this reason, {@code formulas} contains a collection
     * of possible formulas with which formula {@code formula} could have been
     * unified. Finally, {@code constraints} contains a collection of boolean
     * variables. The index of the single boolean variable whose value is
     * {@code true} identifies the formula of {@code formulas} with which
     * formula {@code formula} has been unified.
     *
     * @param formula the just unified formula.
     * @param formulas the list of possible formulas with which formula
     * {@code formula} could have been unified.
     * @param constraints the list of boolean variables identifying the with
     * which formula {@code formula} has been unified.
     */
    public default void formulaUnified(IFormula formula, List<IFormula> formulas, List<IBool> constraints) {
    }

    /**
     * Notifies subclasses that a new formula has been inactivated.
     *
     * @param formula the just inactivated formula.
     */
    public default void formulaInactivated(IFormula formula) {
    }
}
