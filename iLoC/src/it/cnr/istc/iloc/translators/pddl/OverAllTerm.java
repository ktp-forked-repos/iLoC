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

import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class OverAllTerm implements Term {

    private final Term term;

    public OverAllTerm(Term term) {
        this.term = term;
    }

    @Override
    public Term negate() {
        throw new AssertionError("It is not possible to call negate on an over all term..");
    }

    @Override
    public Term ground(Domain domain, Map<String, Term> known_terms) {
        return new OverAllTerm(term.ground(domain, known_terms));
    }

    @Override
    public String toString() {
        return "(over all " + term + ')';
    }
}
