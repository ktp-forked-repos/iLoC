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
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.ISolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PropositionalStateType extends Type {

    public static final String TYPE_NAME = "PropositionalState";
    private final Properties properties;
    private final boolean lazy_scheduling;

    public PropositionalStateType(ISolver solver, Properties properties) {
        super(solver, solver, TYPE_NAME);
        this.properties = properties;
        this.lazy_scheduling = Boolean.parseBoolean(properties.getProperty("PropositionalStateLazyScheduling", "false"));
    }

    @Override
    public void predicateDefined(IPredicate predicate) {
        try {
            predicate.defineField(new Field(Constants.START, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.END, enclosingScope.getType(Constants.NUMBER)));
            predicate.defineField(new Field(Constants.DURATION, enclosingScope.getType(Constants.NUMBER)));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PropositionalStateType.class.getName()).log(Level.SEVERE, null, ex);
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
            try {
                if (formula.getType().getName().endsWith("True")) {
                    /**
                     * Activated formula is a positive polarity formula
                     * therefore it must be scheduled only with negative
                     * polarity formulas having the same predicate and the same
                     * parameters.
                     */
                    IPredicate predicate = formula.getType().getEnclosingScope().getPredicate(formula.getType().getName().substring(0, formula.getType().getName().length() - 4) + "False");
                    predicate.getInstances().stream().map(f -> (IFormula) f).filter(f -> f != formula && f.getFormulaState() == FormulaState.Active).forEach(f -> {
                        List<IBool> or = new ArrayList<>();
                        // Either they have different parameters ..
                        formula.getType().getFields().values().stream().filter(field -> !field.isSynthetic() && !(field.getName().equals(Constants.START) || field.getName().equals(Constants.END) || field.getName().equals(Constants.DURATION))).forEach(field -> {
                            or.add(network.not(formula.get(field.getName()).eq(f.get(field.getName()))));
                        });
                        // .. or they are ordered
                        or.add(network.leq(f.get(Constants.END), start));
                        or.add(network.leq(end, f.get(Constants.START)));
                        network.assertFacts(network.or(or.toArray(new IBool[or.size()])));
                    });
                } else if (formula.getType().getName().endsWith("False")) {
                    /**
                     * Activated formula is a negative polarity formula
                     * therefore it must be scheduled only with positive
                     * polarity formulas having the same predicate and the same
                     * parameters.
                     */
                    IPredicate predicate = formula.getType().getEnclosingScope().getPredicate(formula.getType().getName().substring(0, formula.getType().getName().length() - 5) + "True");
                    predicate.getInstances().stream().map(f -> (IFormula) f).filter(f -> f != formula && f.getFormulaState() == FormulaState.Active).forEach(f -> {
                        List<IBool> or = new ArrayList<>();
                        // Either they have different parameters ..
                        formula.getType().getFields().values().stream().filter(field -> !field.isSynthetic() && !(field.getName().equals(Constants.START) || field.getName().equals(Constants.END) || field.getName().equals(Constants.DURATION))).forEach(field -> {
                            or.add(network.not(formula.get(field.getName()).eq(f.get(field.getName()))));
                        });
                        // .. or they are ordered
                        or.add(network.leq(f.get(Constants.END), start));
                        or.add(network.leq(end, f.get(Constants.START)));
                        network.assertFacts(network.or(or.toArray(new IBool[or.size()])));
                    });
                } else {
                    /**
                     * Activated formula is a multivalued function formula
                     * therefore it must be scheduled only with multivalued
                     * function formulas having the same predicate, the same
                     * parameters and a different value.
                     */
                    IPredicate predicate = formula.getType();
                    predicate.getInstances().stream().map(f -> (IFormula) f).filter(f -> f != formula && f.getFormulaState() == FormulaState.Active).forEach(f -> {
                        List<IBool> or = new ArrayList<>();
                        // Either they have different parameters ..
                        formula.getType().getFields().values().stream().filter(field -> !field.isSynthetic() && !(field.getName().equals(Constants.START) || field.getName().equals(Constants.END) || field.getName().equals(Constants.DURATION) || field.getName().equals("value"))).forEach(field -> {
                            or.add(network.not(formula.get(field.getName()).eq(f.get(field.getName()))));
                        });
                        // .. or they are have the same value ..
                        or.add(formula.get("value").eq(f.get("value")));
                        // .. or they are ordered
                        or.add(network.leq(f.get(Constants.END), start));
                        or.add(network.leq(end, f.get(Constants.START)));
                        network.assertFacts(network.or(or.toArray(new IBool[or.size()])));
                    });
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PropositionalStateType.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
