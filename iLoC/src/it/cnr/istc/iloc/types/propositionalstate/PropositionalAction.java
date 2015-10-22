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
package it.cnr.istc.iloc.types.propositionalstate;

import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IGoal;
import it.cnr.istc.iloc.api.IResolver;
import java.util.Arrays;
import java.util.Collection;

/**
 * We use a specific class for managind propositional actions since such actions
 * can neve unify.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class PropositionalAction implements IGoal {

    private final IFormula formula;
    private final IResolver activationResolver;

    PropositionalAction(IFormula formula) {
        this.formula = formula;
        this.activationResolver = new ActivationResolver();
    }

    @Override
    public IFormula getFormula() {
        return formula;
    }

    @Override
    public double getEstimatedCost() {
        return formula.getSolver().getStaticCausalGraph().getMinReachableNodes(formula.getSolver().getStaticCausalGraph().getNode(formula.getType())).size();
    }

    @Override
    public Collection<IResolver> getResolvers() {
        return Arrays.asList(activationResolver);
    }

    @Override
    public String toString() {
        return "goal - " + formula;
    }

    private class ActivationResolver implements IResolver {

        private boolean resolved = false;
        private boolean expanded = false;

        @Override
        public double getKnownCost() {
            return Double.MIN_NORMAL;
        }

        @Override
        public void resolve() {
            assert !resolved;
            formula.setActiveState();
            if (!expanded) {
                formula.getType().applyRule(formula);
                expanded = true;
            }
            resolved = true;
        }

        @Override
        public boolean isResolved() {
            return resolved;
        }

        @Override
        public void retract() {
            assert resolved;
            formula.setInactiveState();
            resolved = false;
        }
    }
}
