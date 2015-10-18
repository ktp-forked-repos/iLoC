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
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IField {

    /**
     * Returns {@code true} if this field is a synthetic field; returns
     * {@code false} otherwise.
     *
     * @return true if and only if this field is a synthetic field.
     */
    public boolean isSynthetic();

    /**
     * Returns the name of the field represented by this {@code IField} object.
     *
     * @return the name of the field represented by this {@code IField} object.
     */
    public String getName();

    /**
     * Returns the runtime class of this {@code IObject}.
     *
     * @return The {@code IType} object that represents the runtime class of
     * this object.
     */
    public IType getType();
}
