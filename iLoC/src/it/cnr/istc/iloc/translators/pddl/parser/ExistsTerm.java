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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ExistsTerm implements Term {

    private final List<Variable> variables;
    private final Term formula;

    public ExistsTerm(Variable[] variables, Term formula) {
        assert Stream.of(variables).noneMatch(variable -> variable == null);
        assert formula != null;
        this.variables = Arrays.asList(variables);
        this.formula = formula;
    }

    @Override
    public Term negate() {
        return new ForAllTerm(variables.stream().toArray(Variable[]::new), formula.negate());
    }

    @Override
    public String toString() {
        return "(exists " + variables.stream().map(variable -> variable.getName() + " - " + variable.getType().getName()).collect(Collectors.joining(" ")) + " " + formula + ")";
    }
}
