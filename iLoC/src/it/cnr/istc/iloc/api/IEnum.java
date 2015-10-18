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
 * A special kind of variable for representing enumerative variables.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IEnum extends IObject {

    /**
     * Returns the underlying integer variable used for selecting the value of
     * this enumerative variable.
     *
     * @return the underlying integer variable used for selecting the value of
     * this enumerative variable.
     */
    public IInt getVar();

    /**
     * Returns an array of enums representing all the allowed values of this
     * enumerative variable.
     *
     * @return an array of enums representing all the allowed values of this
     * enumerative variable.
     */
    public IObject[] getEnums();
}
