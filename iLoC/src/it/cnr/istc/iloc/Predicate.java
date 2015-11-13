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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public abstract class Predicate extends Type implements IPredicate {

    public Predicate(ISolver solver, IScope enclosingScope, String name, IField... fields) {
        super(solver, enclosingScope, name);
        if (enclosingScope != solver) {
            defineField(new Field(Constants.SCOPE, (IType) enclosingScope));
        }
        for (IField field : fields) {
            defineField(field);
        }
    }

    @Override
    public IFormula newFact(IFormula cause, Map<String, IObject> assignments) {
        IFormula fact = new Formula(solver, cause, this, assignments);
        solver.getCurrentNode().addResolver(new IResolver() {
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return 0;
            }

            @Override
            public void resolve() {
                assert !resolved;
                instances.add(fact);
                solver.getDynamicCausalGraph().addFormula(fact);
                resolved = true;
            }

            @Override
            public boolean isResolved() {
                return resolved;
            }

            @Override
            public void retract() {
                assert resolved;
                instances.remove(fact);
                solver.getDynamicCausalGraph().removeFormula(fact);
                resolved = false;
            }
        });
        enclosingScope.factCreated(fact);
        return fact;
    }

    @Override
    public IFormula newGoal(IFormula cause, Map<String, IObject> assignments) {
        IFormula goal = new Formula(solver, cause, this, assignments);
        solver.getCurrentNode().addResolver(new IResolver() {
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return 0;
            }

            @Override
            public void resolve() {
                assert !resolved;
                instances.add(goal);
                solver.getDynamicCausalGraph().addFormula(goal);
                resolved = true;
            }

            @Override
            public boolean isResolved() {
                return resolved;
            }

            @Override
            public void retract() {
                assert resolved;
                instances.remove(goal);
                solver.getDynamicCausalGraph().removeFormula(goal);
                resolved = false;
            }
        });
        enclosingScope.goalCreated(goal);
        return goal;
    }

    @Override
    public String toString() {
        return getName() + "(" + getFields().values().stream().filter(field -> !field.isSynthetic()).map(f -> f.getType().getName() + " " + f.getName()).collect(Collectors.joining(", ")) + ")";
    }
}
