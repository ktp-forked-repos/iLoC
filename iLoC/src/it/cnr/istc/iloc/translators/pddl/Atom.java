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

import it.cnr.istc.iloc.translators.pddl.parser.Predicate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Atom {

    private final Predicate predicate;
    private final List<Parameter> variables;

    Atom(Predicate predicate, Parameter... variables) {
        assert Stream.of(variables).noneMatch(Objects::isNull);
        this.predicate = predicate;
        this.variables = Arrays.asList(variables);
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public List<Parameter> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.predicate);
        hash = 73 * hash + Objects.hashCode(this.variables);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Atom other = (Atom) obj;
        return Objects.equals(this.predicate, other.predicate) && Objects.equals(this.variables, other.variables);
    }

    @Override
    public String toString() {
        return "(" + predicate.getName() + " " + variables.stream().map(variable -> variable.getName() + " - " + variable.getType().getName()).collect(Collectors.joining(" ")) + ")";
    }
}
