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

import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IGoal;
import it.cnr.istc.iloc.api.IRelaxedPlanningGraph;
import it.cnr.istc.iloc.api.IResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class Goal implements IGoal {

    private boolean skip_check = false;
    private final IFormula formula;
    private final IRelaxedPlanningGraph rpg;
    private final IResolver activationResolver;
    private final IResolver unificationResolver;

    Goal(IFormula formula) {
        this.formula = formula;
        this.rpg = formula.getSolver().getRelaxedPlanningGraph();
        this.activationResolver = new ActivationResolver();
        this.unificationResolver = new UnificationResolver();
    }

    @Override
    public IFormula getFormula() {
        return formula;
    }

    @Override
    public double getEstimatedCost() {
        return rpg.level(formula.getSolver().getStaticCausalGraph().getNode(formula.getType()));
    }

    @Override
    public Collection<IResolver> getResolvers() {
        List<IFormula> unifiable = formula.getType().getInstances().stream().map(f -> (IFormula) f).filter(f -> f.getFormulaState() == FormulaState.Active).collect(Collectors.toList());
        if (!unifiable.isEmpty()) {
            if (skip_check) {
                return Arrays.asList(unificationResolver, activationResolver);
            } else if (formula.getSolver().getConstraintNetwork().checkFacts(formula.getSolver().getConstraintNetwork().or(unifiable.stream().map(f -> formula.eq(f)).toArray(IBool[]::new)))) {
                skip_check = true;
                return Arrays.asList(unificationResolver, activationResolver);
            } else {
                return Arrays.asList(activationResolver);
            }
        } else {
            return Arrays.asList(activationResolver);
        }
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

    private class UnificationResolver implements IResolver {

        private boolean resolved = false;

        @Override
        public double getKnownCost() {
            return 0;
        }

        @Override
        public void resolve() {
            assert !resolved;
            assert formula.getType().getInstances().stream().map(f -> (IFormula) f).anyMatch(f -> f.getFormulaState() == FormulaState.Active);
            IConstraintNetwork network = formula.getType().getSolver().getConstraintNetwork();

            List<IBool> constraints = new ArrayList<>();
            List<IFormula> formulas = new ArrayList<>();
            formula.getType().getInstances().stream().map(f -> (IFormula) f).filter(f -> f.getFormulaState() == FormulaState.Active).forEach(f -> {
                formulas.add(f);
                constraints.add(formula.eq(f));
            });

            formula.setUnifiedState(formulas, constraints);

            if (constraints.size() == 1) {
                network.assertFacts(constraints.get(0));
            } else {
                network.assertFacts(network.or(constraints.toArray(new IBool[constraints.size()])));
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
