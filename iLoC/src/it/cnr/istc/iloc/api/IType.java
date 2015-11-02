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

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IType extends IScope {

    /**
     * Returns the name of the entity (class, interface, primitive type, or
     * void) represented by this {@code IType} object, as a {@code String}.
     *
     * @return the name of the class or interface represented by this object.
     */
    public String getName();

    /**
     * Determines if the class or interface represented by this {@code IType}
     * object is either the same as, or is a superclass or superinterface of,
     * the class or interface represented by the specified {@code IType}
     * parameter. It returns {@code true} if so; otherwise it returns
     * {@code false}.
     *
     * @param type the {@code IType} object to be checked.
     * @return the {@code boolean} value indicating whether objects of the type
     * {@code cls} can be assigned to objects of this class.
     */
    public boolean isAssignableFrom(IType type);

    /**
     * Determines if the specified {@code IType} object represents a primitive
     * type.
     *
     * @return true if and only if this class represents a primitive type
     */
    public boolean isPrimitive();

    /**
     * Returns the {@code IType} representing the superclass of the entity
     * (class, primitive type, predicate type or void) represented by this
     * {@code IType}.
     *
     * @return the superclass of the class represented by this object.
     */
    public IType getSuperclass();

    /**
     * Sets the {@code IType} representing the superclass of the entity (class,
     * primitive type, predicate type or void) represented by this
     * {@code IType}.
     *
     * @param superclass the superclass of the class represented by this object.
     */
    public void setSuperclass(IType superclass);

    /**
     * Returns a {@code IConstructor} object that reflects the specified public
     * constructor of the class represented by this {@code IType} object. The
     * {@code parameterTypes} parameter is an array of {@code IType} objects
     * that identify the constructor's formal parameter types, in declared
     * order.
     *
     * @param parameterTypes the parameter array.
     * @return the {@code IConstructor} object of the public constructor that
     * matches the specified {@code parameterTypes}.
     * @throws NoSuchMethodException if a matching method is not found.
     */
    public IConstructor getConstructor(IType... parameterTypes) throws NoSuchMethodException;

    /**
     * Returns a collection containing {@code IConstructor} objects reflecting
     * all the public constructors of the class represented by this
     * {@code IType} object.
     *
     * @return the collection of {@code IConstructor} objects representing the
     * public constructors of this class.
     */
    public Collection<IConstructor> getConstructors();

    /**
     * Defines a constructor for this {@code IType} object.
     *
     * @param constructor the defined constructor for this {@code IType} object.
     */
    public void defineConstructor(IConstructor constructor);

    /**
     * Checks the consistency of all the instances of this {@code IType} object
     * according to the given model returning {@code true} if no inconsistency
     * is found.
     *
     * @param model the model according to which the consistency should be
     * checked.
     * @return a collection of constraints needed for removing the
     * inconsistencies.
     */
    public List<IBool> checkConsistency(IModel model);

    /**
     * Returns a list containing all the instances of this {@code IType}. This
     * method could return different instances depending on the current node of
     * the search space.
     *
     * @return a list containing all the instances of this {@code IType}.
     */
    public List<IObject> getInstances();

    /**
     * Creates an existential of this {@code IType} object. The existential is
     * an object representing one of the possible instances of this
     * {@code IType}.
     *
     * @return an existential of this {@code IType} object.
     */
    public default IEnum newExistential() {
        return getSolver().getConstraintNetwork().newEnum(this, getInstances());
    }

    /**
     * Creates a new instance of the class represented by this {@code IType}
     * object.
     *
     * @param enclosing_environment the enclosing environment for the new
     * object.
     * @return a newly allocated instance of the class represented by this
     * object.
     */
    public IObject newInstance(IEnvironment enclosing_environment);

    /**
     * Utility method for retrieving all the active formulas currently
     * associated, according to the given model, to all the instances of this
     * {@code IType} object.
     *
     * @param model the model according to which the formulas are associated to
     * instances.
     * @return a {@code Map} having instances as key and a {@code Collection} of
     * formulas as values.
     */
    public default Map<IObject, Collection<IFormula>> getFormulas(IModel model) {
        final Collection<IObject> instances = getInstances();
        final Map<IObject, Collection<IFormula>> formulas = new IdentityHashMap<>(instances.size());
        instances.forEach(instance -> {
            formulas.put(instance, new ArrayList<>());
        });
        getPredicates().values().stream().flatMap(predicate -> predicate.getInstances().stream()).map(formula -> (IFormula) formula).filter(formula -> formula.getFormulaState() == FormulaState.Active).forEach(formula -> {
            formulas.get(formula.get(model, Constants.SCOPE)).add(formula);
        });
        return formulas;
    }
}
