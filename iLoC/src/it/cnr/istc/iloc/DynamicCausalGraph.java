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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IDynamicCausalGraph;
import it.cnr.istc.iloc.api.IDynamicCausalGraphListener;
import it.cnr.istc.iloc.api.IFormula;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class DynamicCausalGraph implements IDynamicCausalGraph {

    private final Collection<IDynamicCausalGraphListener> listeners = new ArrayList<>();
    private final Set<IFormula> formulas = new HashSet<>();
    private final Map<IFormula, Unification> unifications = new IdentityHashMap<>();

    @Override
    public void addFormula(IFormula formula) {
        formulas.add(formula);
        listeners.forEach(listener -> {
            listener.formulaAdded(formula);
        });
    }

    @Override
    public void removeFormula(IFormula formula) {
        formulas.remove(formula);
        listeners.forEach(listener -> {
            listener.formulaRemoved(formula);
        });
    }

    @Override
    public void formulaActivated(IFormula formula) {
        listeners.forEach(listener -> {
            listener.formulaActivated(formula);
        });
    }

    @Override
    public void formulaUnified(IFormula formula, List<IFormula> formulas, List<IBool> constraints) {
        unifications.put(formula, new Unification(formulas, constraints));
        listeners.forEach(listener -> {
            listener.formulaUnified(formula, formulas, constraints);
        });
    }

    @Override
    public void formulaInactivated(IFormula formula) {
        unifications.remove(formula);
        listeners.forEach(listener -> {
            listener.formulaInactivated(formula);
        });
    }

    @Override
    public void addCausalGraphListener(IDynamicCausalGraphListener listener) {
        listeners.add(listener);
        formulas.forEach(formula -> {
            listener.formulaAdded(formula);
        });
        formulas.forEach(formula -> {
            switch (formula.getFormulaState()) {
                case Inactive:
                    listener.formulaInactivated(formula);
                    break;
                case Active:
                    listener.formulaActivated(formula);
                    break;
                case Unified:
                    listener.formulaUnified(formula, unifications.get(formula).with, unifications.get(formula).terms);
                    break;
                default:
                    throw new AssertionError(formula.getFormulaState().name());
            }
        });
    }

    @Override
    public void removeCausalGraphListener(IDynamicCausalGraphListener listener) {
        listeners.remove(listener);
    }

    private static class Unification {

        private final List<IFormula> with;
        private final List<IBool> terms;

        Unification(List<IFormula> with, List<IBool> terms) {
            this.with = with;
            this.terms = terms;
        }
    }
}
