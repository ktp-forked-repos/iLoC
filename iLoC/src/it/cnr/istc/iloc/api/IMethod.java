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
 * Instances of the class {@code IMethod} provides information about, and access
 * to, a single method on a class.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IMethod extends IScope {

    /**
     * Returns the name of the method represented by this {@code IMethod}
     * object, as a {@code String}.
     *
     * @return the name of the method.
     */
    public String getName();

    /**
     * Returns an array of {@code IField} objects that represent all the
     * parameters to the method represented by this object. Returns an empty
     * array if the method has no parameters.
     * <p>
     * Parameters are ordered according to method definition.
     *
     * @return an array of {@code IField} objects representing all the
     * parameters to the method represented by this object.
     */
    public IField[] getParameters();

    /**
     * Returns an optional {@code IType} object that represents the formal
     * return type of the method represented by this {@code IMethod} object.
     *
     * @return the return type for the method this object represents.
     */
    public IType getReturnType();

    /**
     * Invokes the underlying method represented by this {@code IMethod} object,
     * on the specified object with the specified parameters.
     *
     * @param object the object the underlying method is invoked from.
     * @param expressions the arguments used for the method call.
     * @return the result of dispatching the method represented by this object
     * on {@code object} with parameters {@code expressions}.
     */
    public IObject invoke(IObject object, IObject... expressions);
}
