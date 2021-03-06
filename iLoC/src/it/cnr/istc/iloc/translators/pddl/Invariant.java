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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Invariant {

    private final Map<String, Parameter> parameters = new LinkedHashMap<>();
    private final Set<Atom> atoms = new LinkedHashSet<>();

    Invariant() {
    }

    void addParameter(Parameter parameter) {
        parameters.put(parameter.getName(), parameter);
    }

    public Map<String, Parameter> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    void addAtom(Atom atom) {
        atoms.add(atom);
    }

    public Collection<Atom> getAtoms() {
        return Collections.unmodifiableCollection(atoms);
    }

    public Collection<Parameter> getCountedVariables() {
        return atoms.stream().flatMap(atom -> atom.getVariables().stream().filter(var -> !parameters.containsValue(var))).collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.parameters);
        hash = 97 * hash + Objects.hashCode(this.atoms);
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
        final Invariant other = (Invariant) obj;
        return Objects.equals(this.parameters, other.parameters) && Objects.equals(this.atoms, other.atoms);
    }

    @Override
    public String toString() {
        return parameters.values().stream().map(v -> v.getName()).collect(Collectors.joining(", ")) + (parameters.isEmpty() ? "" : " ") + atoms.stream().map(p -> p.toString()).collect(Collectors.joining(" + "));
    }
}
