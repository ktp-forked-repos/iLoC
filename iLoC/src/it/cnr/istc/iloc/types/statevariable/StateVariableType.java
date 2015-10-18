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
package it.cnr.istc.iloc.types.statevariable;

import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.utils.CombinationGenerator;
import it.cnr.istc.iloc.utils.MathUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class StateVariableType extends Type {

    public static final String TYPE_NAME = "StateVariable";
    private final Properties properties;
    private final boolean lazy_scheduling;

    public StateVariableType(ISolver solver, Properties properties) {
        super(solver, solver, TYPE_NAME);
        this.properties = properties;
        this.lazy_scheduling = Boolean.parseBoolean(properties.getProperty("StateVariableLazyScheduling", "false"));
    }

    @Override
    public void predicateDefined(IPredicate predicate) {
        try {
            predicate.defineField(new Field(Constants.START, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.END, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.DURATION, enclosingScope.getType(Constants.NUMBER)));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StateVariableType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Let's add temporal constraints to the formula whenever the formula is
     * activated:
     * <p>
     * duration = end - start;
     * <p>
     * start >= origin;
     * <p>
     * end {@literal <=} horizon;
     * <p>
     * duration {@literal >=} 0;
     *
     * @param formula the formula that has been created.
     */
    @Override
    public void formulaActivated(IFormula formula) {
        IConstraintNetwork network = solver.getConstraintNetwork();
        final INumber start = formula.get(Constants.START);
        final INumber end = formula.get(Constants.END);
        final INumber duration = formula.get(Constants.DURATION);
        final INumber origin = solver.get(Constants.ORIGIN);
        final INumber horizon = solver.get(Constants.HORIZON);
        network.assertFacts(
                network.eq(duration, network.subtract(end, start)),
                network.geq(start, origin),
                network.leq(end, horizon),
                network.geq(duration, network.newReal("0"))
        );

        if (!lazy_scheduling) {
            List<IBool> vars = new ArrayList<>();
            formula.getType().getEnclosingScope().getPredicates().values().stream().flatMap(predicate -> predicate.getInstances().stream().map(f -> (IFormula) f).filter(f -> f != formula && f.getFormulaState() == FormulaState.Active)).forEach(f -> {
                vars.add(
                        network.or(
                                network.leq(f.get(Constants.END), start),
                                network.leq(end, f.get(Constants.START)),
                                network.not(formula.getScope().eq(f.getScope()))
                        )
                );
            });

            if (!vars.isEmpty()) {
                network.assertFacts(vars.toArray(new IBool[vars.size()]));
            }
        }
    }

    @Override
    public List<IBool> checkConsistency(IModel model) {
        if (!lazy_scheduling) {
            return Collections.emptyList();
        } else {
            List<IBool> constraints = new ArrayList<>();
            IConstraintNetwork network = solver.getConstraintNetwork();
            final Map<IObject, Collection<IFormula>> formulas = getFormulas(model);

            instances.stream().map(instance -> new StateVariableTimeline(solver, model, instance, formulas.get(instance))).forEach(timeline -> {
                timeline.values.stream().filter(value -> value.formulas.length > 1).forEach(peak -> {
                    // we have found a peak
                    List<IBool> or = new ArrayList<>(MathUtils.combinations(1, peak.formulas.length));
                    for (IFormula[] c_fs : new CombinationGenerator<>(2, peak.formulas)) {
                        or.add(network.leq(c_fs[0].get(Constants.END), c_fs[1].get(Constants.START)));
                        or.add(network.leq(c_fs[1].get(Constants.END), c_fs[0].get(Constants.START)));
                        or.add(network.not(c_fs[0].getScope().eq(c_fs[1].getScope())));
                    }
                    constraints.add(network.or(or.toArray(new IBool[or.size()])));
                });
            });

            return constraints;
        }
    }
}
