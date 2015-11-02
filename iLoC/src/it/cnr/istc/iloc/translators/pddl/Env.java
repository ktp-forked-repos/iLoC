/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.iloc.translators.pddl;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
interface Env {

    /**
     * Returns the enclosing environment of this environment.
     *
     * @return the enclosing environment of this environment.
     */
    Env getEnclosingEnv();

    /**
     * Adds an environment to this environment. This method is typically used
     * for adding environments to conjunctions and disjunctions.
     *
     * @param env the environment to add to this environment.
     */
    void addEnv(Env env);

    /**
     * Checks whether this environment is consistent or not.
     *
     * @return {@code true} if this environment is consistent.
     */
    boolean isConsistent();

    /**
     * Sets the consistency of this environment. This methid is typically called
     * when a static predicate is found.
     *
     * @param consistent the consistency of the environment.
     */
    void setConsistent(boolean consistent);

    /**
     * Simplifies this environment, removing all the enclosed environments which
     * are inconsistent.
     */
    void simplify();
}
