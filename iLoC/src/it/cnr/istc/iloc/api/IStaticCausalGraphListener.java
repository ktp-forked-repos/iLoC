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
public interface IStaticCausalGraphListener {

    /**
     * This method is invoked when a new type has been added.
     *
     * @param type the new type that has been added.
     */
    public void typeAdded(IType type);

    /**
     * This method is invoked when an existing type has been removed.
     *
     * @param type the existing type that has been removed.
     */
    public void typeRemoved(IType type);

    /**
     * This method is invoked when a new node has been added.
     *
     * @param node the new node that has been added.
     */
    public void nodeAdded(IStaticCausalGraph.INode node);

    /**
     * This method is invoked when an existing node has been removed.
     *
     * @param node the existing node that has been removed.
     */
    public void nodeRemoved(IStaticCausalGraph.INode node);

    /**
     * This method is invoked when a static causal link has been added.
     *
     * @param edge the edge that has been added.
     */
    public void edgeAdded(IStaticCausalGraph.IEdge edge);

    /**
     *
     * This method is invoked when an existing static causal link has been
     * removed.
     *
     * @param edge the edge that has been removed.
     */
    public void edgeRemoved(IStaticCausalGraph.IEdge edge);
}
