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
import it.cnr.istc.iloc.api.IType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PropositionalAgentType extends Type {

    public static final String TYPE_NAME = "PropositionalAgent";
    private final Properties properties;
    private final boolean lazy_scheduling;

    public PropositionalAgentType(ISolver solver, Properties properties) {
        super(solver, solver, TYPE_NAME);
        this.properties = properties;
        this.lazy_scheduling = Boolean.parseBoolean(properties.getProperty("PropositionalAgentLazyScheduling", "true"));
    }

    @Override
    public void predicateDefined(IPredicate predicate) {
        try {
            predicate.defineField(new Field(Constants.START, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.END, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.DURATION, enclosingScope.getType(Constants.NUMBER)));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PropositionalAgentType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void factCreated(IFormula fact) {
        solver.getCurrentNode().enqueue(new PropositionalAction(fact));
    }

    @Override
    public void goalCreated(IFormula goal) {
        solver.getCurrentNode().enqueue(new PropositionalAction(goal));
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
                                network.lt(f.get(Constants.START), start),
                                network.lt(start, f.get(Constants.START)),
                                network.not(formula.getScope().eq(f.getScope()))
                        )
                );
                vars.add(
                        network.or(
                                network.lt(f.get(Constants.END), end),
                                network.lt(end, f.get(Constants.END)),
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

            Map<IType, List<IObject>> type_instances = instances.stream().collect(Collectors.groupingBy(instance -> instance.getType()));
            type_instances.keySet().stream().map(type -> type.getFormulas(model)).forEach(formulas -> {
                formulas.keySet().forEach(instance -> {
                    List<IFormula> c_formulas = new ArrayList<>(formulas.get(instance));
                    for (int i = 0; i < c_formulas.size(); i++) {
                        for (int j = i + 1; j < c_formulas.size(); j++) {
                            IFormula f_i = c_formulas.get(i);
                            IFormula f_j = c_formulas.get(j);
                            if (model.evaluate(network.eq((INumber) f_i.get(Constants.START), (INumber) f_j.get(Constants.START)))) {
                                constraints.add(network.or(
                                        network.not(f_i.get(Constants.START).eq(f_j.get(Constants.START))),
                                        network.not(f_i.getScope().eq(f_j.getScope()))
                                ));
                            }
                            if (model.evaluate(network.eq((INumber) f_i.get(Constants.END), (INumber) f_j.get(Constants.END)))) {
                                constraints.add(network.or(
                                        network.not(f_i.get(Constants.END).eq(f_j.get(Constants.END))),
                                        network.not(f_i.getScope().eq(f_j.getScope()))
                                ));
                            }
                        }
                    }
                });
            });

            return constraints;
        }
    }
}
