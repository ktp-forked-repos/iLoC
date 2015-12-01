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
 * Instances of the class {@code IConstructor} provide information about, and access to,
 * a single constructor for a class.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IConstructor extends IScope {

    /**
     * Returns an array of {@code IField} objects that represent all the
     * parameters to the constructor represented by this object. Returns an
     * empty array if the constructor has no parameters.
     * <p>
     * Parameters are ordered according to constructor definition.
     *
     * @return an array of {@code IField} objects representing all the
     * parameters to the constructor represented by this object.
     */
    public IField[] getParameters();

    /**
     * Uses the constructor represented by this {@code IConstructor} object to
     * create and initialize a new instance of the constructor's declaring
     * class, with the specified initialization parameters.
     *
     * @param environment the enclosing environment for the new object.
     * @param expressions array of objects to be passed as arguments to the
     * constructor call.
     * @return a new object created by calling the constructor this object
     * represents.
     */
    public IObject newInstance(IEnvironment environment, IObject... expressions);

    /**
     * Instantiates all the instantiable member variables of the given instance
     * type.
     *
     * @param instance the instance whose instantiable member variables will be
     * instantiated.
     */
    public void init(IObject instance);

    /**
     * Instantiates all the uninstantiated member variables of the given
     * instance type. Primitive types will be automatically instantiated.
     * Complex types will be instantiated as enumerative variables.
     *
     * @param instance the instance whose uninstantiated member variables will
     * be instantiated.
     */
    public void completeInit(IObject instance);

    /**
     * Invokes the underlying constructor represented by this
     * {@code IConstructor} object, on the specified object with the specified
     * parameters.
     *
     * @param instance the instance the underlying method is invoked from.
     * @param expressions the arguments used for the method call.
     */
    public void invoke(IObject instance, IObject... expressions);
}
