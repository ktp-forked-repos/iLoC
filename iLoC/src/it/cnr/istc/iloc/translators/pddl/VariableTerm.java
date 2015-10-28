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
public class VariableTerm implements Term {

    private final Variable variable;

    public VariableTerm(Variable variable) {
        assert variable != null;
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public Term negate() {
        throw new AssertionError("It is not possible to call negate on a variable..");
    }

    @Override
    public String toString() {
        return variable.getName();
    }
}
