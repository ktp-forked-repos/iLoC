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
package it.cnr.istc.iloc.types.battery;

import it.cnr.istc.iloc.Constructor;
import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.Predicate;
import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IDisjunctionFlaw;
import it.cnr.istc.iloc.api.IFact;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IGoal;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IPreferenceFlaw;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import it.cnr.istc.iloc.api.IType;
import java.util.ArrayList;
import java.util.Collection;
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
public class BatteryType extends Type {

    public static final String TYPE_NAME = "Battery";
    public static final String CAPACITY = "capacity";
    public static final String INITIAL_AMOUNT = "initial_amount";
    public static final String FINAL_AMOUNT = "final_amount";
    public static final String CHARGE_PREDICATE_NAME = "Charge";
    public static final String CONSUME_PREDICATE_NAME = "Consume";
    public static final String AMOUNT = "amount";
    public static final String C_AMOUNT = "c_amount";
    private final Properties properties;
    private final boolean lazy_scheduling;
    private boolean closed_batteries = false;
    private ChargeBatteryPredicate charge_predicate;
    private ConsumeBatteryPredicate consume_predicate;

    public BatteryType(ISolver solver, Properties properties) {
        super(solver, solver, TYPE_NAME);
        this.properties = properties;
        this.lazy_scheduling = Boolean.parseBoolean(properties.getProperty("ConsumableResourceLazyScheduling", "true"));

        try {
            final IType numeric_type = solver.getType(Constants.NUMBER);

            // Let's add the default fields to this type ..
            defineField(new Field(CAPACITY, numeric_type));
            defineField(new Field(INITIAL_AMOUNT, numeric_type));
            defineField(new Field(FINAL_AMOUNT, numeric_type));

            // .. the default constructors ..
            defineConstructor(new BatteryConstructor0(solver, this, new Field(CAPACITY, numeric_type)));
            defineConstructor(new BatteryConstructor1(solver, this, new Field(CAPACITY, numeric_type), new Field(INITIAL_AMOUNT, numeric_type), new Field(FINAL_AMOUNT, numeric_type)));

            // .. and the default predicates
            charge_predicate = new ChargeBatteryPredicate(solver, this, new Field(Constants.START, numeric_type), new Field(Constants.END, numeric_type), new Field(Constants.DURATION, numeric_type), new Field(AMOUNT, numeric_type), new Field(C_AMOUNT, numeric_type));
            definePredicate(charge_predicate);

            consume_predicate = new ConsumeBatteryPredicate(solver, this, new Field(Constants.START, numeric_type), new Field(Constants.END, numeric_type), new Field(Constants.DURATION, numeric_type), new Field(AMOUNT, numeric_type));
            definePredicate(consume_predicate);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BatteryType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void factCreated(IFormula fact) {
        fact.getType().applyRule(fact);
    }

    @Override
    public void goalCreated(IFormula goal) {
        goal.getType().applyRule(goal);
    }

    @Override
    public void formulaUnified(IFormula formula, List<IFormula> with, List<IBool> terms) {
        throw new AssertionError("Formulas having a Battery type scope cannot unify..");
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
     * <p>
     * We also constraint the amount to be greater than or equal to 0:
     * <p>
     * amount {@literal >=} 0;
     * <p>
     * If the formula is a charge formula, we also add a constraint that forces
     * the c_amount to be less or equal to the amount:
     * <p>
     * c_amount {@literal <=} amount;
     * <p>
     * c_amount {@literal >=} 0;
     *
     * @param formula the formula that has been created.
     */
    @Override
    public void formulaActivated(IFormula formula) {
        IConstraintNetwork network = solver.getConstraintNetwork();
        final INumber start = formula.get(Constants.START);
        final INumber end = formula.get(Constants.END);
        final INumber duration = formula.get(Constants.DURATION);
        final INumber amount = formula.get(AMOUNT);
        final INumber origin = solver.get(Constants.ORIGIN);
        final INumber horizon = solver.get(Constants.HORIZON);
        if (formula.getType().getName().equals(CHARGE_PREDICATE_NAME)) {
            final INumber c_amount = formula.get(C_AMOUNT);
            network.assertFacts(
                    network.eq(duration, network.subtract(end, start)),
                    network.geq(start, origin),
                    network.leq(end, horizon),
                    network.geq(duration, network.newReal("0")),
                    network.geq(amount, network.newReal("0")),
                    network.leq(c_amount, amount),
                    network.geq(c_amount, network.newReal("0"))
            );
        } else {
            network.assertFacts(
                    network.eq(duration, network.subtract(end, start)),
                    network.geq(start, origin),
                    network.leq(end, horizon),
                    network.geq(duration, network.newReal("0")),
                    network.geq(amount, network.newReal("0"))
            );
        }

        if (!lazy_scheduling) {
            throw new UnsupportedOperationException("Eager scheduling for batteries is not supported yet..");
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

            IStaticCausalGraph causal_graph = solver.getStaticCausalGraph();
            if (!closed_batteries && solver.getCurrentNode().getFlaws().stream().map(flaw -> {
                if (flaw instanceof IGoal) {
                    return causal_graph.getNode(((IGoal) flaw).getFormula().getType());
                } else if (flaw instanceof IFact) {
                    return causal_graph.getNode(((IFact) flaw).getFormula().getType());
                } else if (flaw instanceof IDisjunctionFlaw) {
                    return causal_graph.getNode(((IDisjunctionFlaw) flaw).getDisjunction());
                } else if (flaw instanceof IPreferenceFlaw) {
                    return causal_graph.getNode(((IPreferenceFlaw) flaw).getPreference());
                } else {
                    throw new AssertionError("Flaw " + flaw.getClass().getName() + " is supported yet..");
                }
            }).noneMatch(node -> causal_graph.existsPath(node, causal_graph.getNode(charge_predicate)) || causal_graph.existsPath(node, causal_graph.getNode(consume_predicate)))) {
                // We need a resolver in order to re-open the resource when backtracking
                solver.getCurrentNode().addResolver(new IResolver() {

                    private boolean resolved = false;

                    @Override
                    public double getKnownCost() {
                        return 0;
                    }

                    @Override
                    public void resolve() {
                        assert !resolved;

                        // Let's close the batteries
                        closed_batteries = true;
                        resolved = true;
                    }

                    @Override
                    public boolean isResolved() {
                        return resolved;
                    }

                    @Override
                    public void retract() {
                        assert resolved;
                        closed_batteries = false;
                        resolved = false;
                    }
                });
            }

            if (closed_batteries) {
                instances.stream().forEach(battery -> {
                    Collection<IFormula> c_formulas = formulas.get(battery);
                    BatteryTimeline timeline = new BatteryTimeline(solver, model, battery, c_formulas);
                    for (int i = 0; i < timeline.values.size(); i++) {
                        //<editor-fold defaultstate="collapsed" desc="battery overcharge">
                        if (model.evaluate(network.gt(timeline.values.get(i).max_amount, timeline.capacity))) {
                            // We have a battery overcharge so we need to anticipate consumptions to charges
                            Collection<IFormula> good_charges = new ArrayList<>(c_formulas.size());
                            Collection<IFormula> good_consumptions = new ArrayList<>(c_formulas.size());
                            for (IFormula f : c_formulas) {
                                switch (f.getType().getName()) {
                                    case CHARGE_PREDICATE_NAME:
                                        if (model.evaluate(network.leq((INumber) f.get(Constants.START), network.newReal(timeline.pulses.get(i).toString())))) {
                                            // Charges that affect current overcharge are all those that start before this timeline value
                                            good_charges.add(f);
                                        }
                                        break;
                                    case CONSUME_PREDICATE_NAME:
                                        if (model.evaluate(network.geq((INumber) f.get(Constants.END), network.newReal(timeline.pulses.get(i + 1).toString())))) {
                                            // Consumptions that might resolve the current overcharge are all those that end after this timeline value
                                            good_consumptions.add(f);
                                        }
                                        break;
                                    default:
                                        throw new AssertionError(f.getType().getName());
                                }
                            }
                            List<IBool> or = new ArrayList<>(good_charges.size() * good_consumptions.size());
                            good_consumptions.stream().forEach((cons) -> {
                                good_charges.stream().forEach((charge) -> {
                                    or.add(network.leq(cons.get(Constants.END), charge.get(Constants.START)));
                                    or.add(network.not(cons.getScope().eq(charge.getScope())));
                                });
                            });
                            or.add(network.geq(battery.get(CAPACITY), timeline.values.get(i).max_amount));
                            constraints.add(network.or(or.toArray(new IBool[or.size()])));
                        }
                        //</editor-fold>
                        //<editor-fold defaultstate="collapsed" desc="battery overconsumption">
                        if (model.evaluate(network.lt(timeline.values.get(i).min_amount, network.newReal("0")))) {
                            // We have a battery overconsumption so we need to anticipate charges to consumption
                            Collection<IFormula> good_charges = new ArrayList<>(c_formulas.size());
                            Collection<IFormula> good_consumptions = new ArrayList<>(c_formulas.size());
                            for (IFormula f : c_formulas) {
                                switch (f.getType().getName()) {
                                    case CHARGE_PREDICATE_NAME:
                                        if (model.evaluate(network.geq((INumber) f.get(Constants.END), network.newReal(timeline.pulses.get(i + 1).toString())))) {
                                            // Charges that might resolve the current overconsumption are all those that end after this timeline value
                                            good_charges.add(f);
                                        }
                                        break;
                                    case CONSUME_PREDICATE_NAME:
                                        if (model.evaluate(network.leq((INumber) f.get(Constants.START), network.newReal(timeline.pulses.get(i).toString())))) {
                                            // Consumptions that affect current overconsumption are all those that start before this timeline value
                                            good_consumptions.add(f);
                                        }
                                        break;
                                    default:
                                        throw new AssertionError(f.getType().getName());
                                }
                            }
                            List<IBool> or = new ArrayList<>(good_charges.size() * good_consumptions.size());
                            good_consumptions.stream().forEach((cons) -> {
                                good_charges.stream().forEach((charge) -> {
                                    or.add(network.leq(charge.get(Constants.END), cons.get(Constants.START)));
                                    or.add(network.not(charge.getScope().eq(cons.getScope())));
                                });
                            });
                            or.add(network.leq(network.newReal("0"), timeline.values.get(i).min_amount));
                            constraints.add(network.or(or.toArray(new IBool[or.size()])));
                        }
                        //</editor-fold>
                    }

                    if (timeline.values.isEmpty() ? model.evaluate(network.not(timeline.initial_amount.eq(timeline.final_amount))) : model.evaluate(network.not(timeline.values.get(timeline.values.size() - 1).final_amount.eq(timeline.final_amount)))) {
                        // The initial amount plus the sum of charges and consumptions is not equal to the final amount
                        final List<INumber> sum = new ArrayList<>(c_formulas.size() + 1);
                        sum.add(battery.get(INITIAL_AMOUNT));
                        sum.addAll(c_formulas.stream().map(f -> {
                            switch (f.getType().getName()) {
                                case CHARGE_PREDICATE_NAME:
                                    return f.get(C_AMOUNT);
                                case CONSUME_PREDICATE_NAME:
                                    return network.negate((INumber) f.get(AMOUNT));
                                default:
                                    throw new AssertionError(f.getType().getName());
                            }
                        }).collect(Collectors.toList()));

                        final List<IBool> or = new ArrayList<>();
                        c_formulas.stream().forEach(formula -> {
                            or.add(network.not(battery.eq(formula.getScope())));
                        });
                        instances.stream().filter(instance -> (instance != battery)).forEach(instance -> {
                            formulas.get(instance).stream().forEach(formula -> {
                                or.add(battery.eq(formula.getScope()));
                            });
                        });
                        if (sum.size() == 1) {
                            or.add(network.eq(sum.get(0), (INumber) battery.get(FINAL_AMOUNT)));
                        } else {
                            or.add(network.eq(network.add(sum.toArray(new INumber[sum.size()])), (INumber) battery.get(FINAL_AMOUNT)));
                        }

                        constraints.add(network.or(or.toArray(new IBool[or.size()])));
                    }
                });
            }

            return constraints;
        }
    }

    private static class BatteryConstructor0 extends Constructor {

        BatteryConstructor0(ISolver solver, IScope enclosingScope, IField... parameters) {
            super(solver, enclosingScope, parameters);
        }

        @Override
        public void init(IObject instance) {
        }

        @Override
        public void invoke(IObject instance, IObject... expressions) {
            IConstraintNetwork network = solver.getConstraintNetwork();
            instance.set(CAPACITY, expressions[0]);
            INumber initial_amount = network.newReal();
            instance.set(INITIAL_AMOUNT, initial_amount);
            INumber final_amount = network.newReal();
            instance.set(FINAL_AMOUNT, final_amount);
            solver.getCurrentNode().addResolver(new IResolver() {

                private boolean resolved = false;

                @Override
                public double getKnownCost() {
                    return 0;
                }

                @Override
                public void resolve() {
                    assert !resolved;
                    network.assertFacts(
                            network.geq(initial_amount, network.newReal("0")),
                            network.leq(initial_amount, (INumber) expressions[0]),
                            network.geq(final_amount, network.newReal("0")),
                            network.leq(final_amount, (INumber) expressions[0])
                    );
                    resolved = true;
                }

                @Override
                public boolean isResolved() {
                    return resolved;
                }

                @Override
                public void retract() {
                    assert resolved;
                    resolved = false;
                }
            });
        }
    }

    private static class BatteryConstructor1 extends Constructor {

        BatteryConstructor1(ISolver solver, IScope enclosingScope, IField... parameters) {
            super(solver, enclosingScope, parameters);
        }

        @Override
        public void init(IObject instance) {
        }

        @Override
        public void invoke(IObject instance, IObject... expressions) {
            IConstraintNetwork network = solver.getConstraintNetwork();
            instance.set(CAPACITY, expressions[0]);
            instance.set(INITIAL_AMOUNT, expressions[1]);
            instance.set(FINAL_AMOUNT, expressions[2]);
            solver.getCurrentNode().addResolver(new IResolver() {

                private boolean resolved = false;

                @Override
                public double getKnownCost() {
                    return 0;
                }

                @Override
                public void resolve() {
                    assert !resolved;
                    network.assertFacts(
                            network.geq((INumber) expressions[1], network.newReal("0")),
                            network.leq((INumber) expressions[1], (INumber) expressions[0]),
                            network.geq((INumber) expressions[2], network.newReal("0")),
                            network.leq((INumber) expressions[2], (INumber) expressions[0])
                    );
                    resolved = true;
                }

                @Override
                public boolean isResolved() {
                    return resolved;
                }

                @Override
                public void retract() {
                    assert resolved;
                    resolved = false;
                }
            });
        }
    }

    private static class ChargeBatteryPredicate extends Predicate {

        ChargeBatteryPredicate(ISolver solver, IScope enclosingScope, IField... parameters) {
            super(solver, enclosingScope, CHARGE_PREDICATE_NAME, parameters);
        }

        @Override
        public void applyRule(IFormula formula) {
            formula.setActiveState();
        }
    }

    private static class ConsumeBatteryPredicate extends Predicate {

        ConsumeBatteryPredicate(ISolver solver, IScope enclosingScope, IField... parameters) {
            super(solver, enclosingScope, CONSUME_PREDICATE_NAME, parameters);
        }

        @Override
        public void applyRule(IFormula formula) {
            formula.setActiveState();
        }
    }
}
