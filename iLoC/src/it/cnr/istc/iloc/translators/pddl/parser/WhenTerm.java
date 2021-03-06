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
package it.cnr.istc.iloc.translators.pddl.parser;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class WhenTerm implements Term {

    private final Term condition;
    private final Term effect;

    public WhenTerm(Term condition, Term effect) {
        assert condition != null;
        assert effect != null;
        this.condition = condition;
        this.effect = effect;
    }

    @Override
    public Term negate() {
        throw new AssertionError("It is not possible to call negate on a conditiona effect..");
    }

    @Override
    public String toString() {
        return "(when " + condition + " " + effect + ")";
    }
}
