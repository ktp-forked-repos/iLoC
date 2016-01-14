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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IVar {

    /**
     * Returns the name of this variable.
     *
     * @return the name of this variable.
     */
    public String getName();

    /**
     * Returns {@code true} if this domain has only one element.
     *
     * @return {@code true} if this domain contains only one element.
     */
    public boolean isSingleton();

    /**
     * Returns {@code true} if this domain is empty.
     *
     * @return {@code true} if this domain is empty.
     */
    public boolean isEmpty();
}
