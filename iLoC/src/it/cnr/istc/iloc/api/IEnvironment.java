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

import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IEnvironment {

    /**
     * Returns the solver enclosing this environment.
     *
     * @return the solver enclosing this environment.
     */
    public ISolver getSolver();

    /**
     * Returns the immediately enclosing environment of the underlying
     * environment. If the underlying environment is a top level environment
     * this method returns {@code null}.
     *
     * @return the immediately enclosing environment of the underlying
     * environment.
     */
    public IEnvironment getEnclosingEnvironment();

    /**
     * Returns the object of this environment, or any enclosing environment,
     * associated to the given name.
     *
     * @param <T>
     * @param name the name of the object to be retrieved.
     * @return the object associated to the given name.
     */
    public <T extends IObject> T get(String name);

    /**
     * Returns all the objects enclosed by this {@code IEnvironment} object
     * mapped by their names.
     *
     * @return a map containing all the objects enclosed by this environment
     * having type names as indexes.
     */
    Map<String, IObject> getObjects();

    /**
     * Associates a name to the given object for this environment.
     *
     * @param name the name associated to the given object for this environment.
     * @param object the object whose name, for this environment, is the given
     * name.
     */
    public void set(String name, IObject object);

    /**
     * Utility method for retrieving the object of this environment, or any
     * enclosing environment, associated to the given name. In case the object
     * is an enumerative variable, the variable is evaluated according to the
     * given model.
     *
     * @param <T>
     * @param model the model used for evaluate enumerative variables.
     * @param name the name of the object to be retrieved.
     * @return the object associated to the given name.
     */
    public default <T extends IObject> T get(IModel model, String name) {
        T object = get(name);
        while (object instanceof IEnum) {
            object = model.evaluate((IEnum) object);
        }
        return object;
    }
}
