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

import java.util.List;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IConstraint {

    /**
     * Returns the variable domains in the scope of the constraint.
     *
     * @return the variable domains in the scope of the constraint.
     */
    public List<IVar> getArguments();

    /**
     * Performs a (most probably incomplete) propagation function removing
     * inconsistent values from variables' domains assuming that the given
     * variable's domain has changed. The given variable should be among the
     * arguments of the constraint as returned by
     * {@link IConstraint#getArguments()}.
     *
     * @param var the variable whose domain has changed.
     * @return {@code true} if no inconsistencies have been found.
     */
    public boolean propagate(IVar var);
}
