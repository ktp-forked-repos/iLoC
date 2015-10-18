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
package it.cnr.istc.iloc.types.reusableresource;

import it.cnr.istc.iloc.Constructor;
import it.cnr.istc.iloc.Field;
import it.cnr.istc.iloc.Predicate;
import it.cnr.istc.iloc.Type;
import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IBool;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IField;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IType;
import it.cnr.istc.iloc.utils.CombinationGenerator;
import it.cnr.istc.iloc.utils.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ReusableResourceType extends Type {

    public static final String TYPE_NAME = "ReusableResource";
    public static final String CAPACITY = "capacity";
    public static final String USE_PREDICATE_NAME = "Use";
    public static final String AMOUNT = "amount";
    private final Properties properties;
    private final boolean lazy_scheduling;

    public ReusableResourceType(ISolver solver, Properties properties) {
        super(solver, solver, TYPE_NAME);
        this.properties = properties;
        this.lazy_scheduling = Boolean.parseBoolean(properties.getProperty("ReusableResourceLazyScheduling", "true"));

        try {
            final IType numeric_type = solver.getType(Constants.NUMBER);

            // Let's add the default field to this type ..
            defineField(new Field(CAPACITY, numeric_type));

            // .. the default constructor ..
            defineConstructor(new ReusableResourceConstructor(solver, this, new Field(CAPACITY, numeric_type)));

            // .. and the default predicate
            final ReusableResourcePredicate use_predicate = new ReusableResourcePredicate(solver, this, new Field(Constants.START, numeric_type), new Field(Constants.END, numeric_type), new Field(Constants.DURATION, numeric_type), new Field(AMOUNT, numeric_type));
            definePredicate(use_predicate);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReusableResourceType.class.getName()).log(Level.SEVERE, null, ex);
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
        throw new AssertionError("Formulas having a ReusableResource type scope cannot unify..");
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
        network.assertFacts(
                network.eq(duration, network.subtract(end, start)),
                network.geq(start, origin),
                network.leq(end, horizon),
                network.geq(duration, network.newReal("0")),
                network.geq(amount, network.newReal("0"))
        );

        if (!lazy_scheduling) {
            throw new UnsupportedOperationException("Eager scheduling for reusable resources is not supported yet..");
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

            instances.stream().map(instance -> new ReusableResourceTimeline(solver, model, instance, formulas.get(instance))).forEach(timeline -> {
                timeline.values.stream().filter(value -> model.evaluate(network.gt(value.var_amount, timeline.var_capacity))).forEach(peak -> {
                    // the contribution of all the overlapping tokens exceed the resource c_capacity
                    INumber c_amount = network.newReal("0");
                    LinkedList<IFormula> c_fs = new LinkedList<>();
                    Arrays.sort(peak.formulas, (IFormula f0, IFormula f1) -> Double.compare(model.evaluate((INumber) f1.get(AMOUNT)).doubleValue(), model.evaluate((INumber) f0.get(AMOUNT)).doubleValue()));
                    for (IFormula f : peak.formulas) {
                        c_amount = network.add(c_amount, (INumber) f.get(AMOUNT));
                        c_fs.add(f);
                        if (model.evaluate(network.gt(c_amount, timeline.var_capacity))) {
                            // we have found a minimal conflict set (MCS)
                            List<IBool> or = new ArrayList<>(MathUtils.combinations(1, c_fs.size()));
                            for (IFormula[] fs : new CombinationGenerator<>(2, c_fs.toArray(new IFormula[c_fs.size()]))) {
                                or.add(network.leq(fs[0].get(Constants.END), fs[1].get(Constants.START)));
                                or.add(network.leq(fs[1].get(Constants.END), fs[0].get(Constants.START)));
                                or.add(network.not(fs[0].getScope().eq(fs[1].getScope())));
                            }
                            or.add(network.geq(timeline.var_capacity, c_amount));
                            IFormula c_f = c_fs.pollFirst();
                            c_amount = network.subtract(c_amount, (INumber) c_f.get(AMOUNT));
                            constraints.add(network.or(or.toArray(new IBool[or.size()])));
                        }
                    }
                });
            });

            return constraints;
        }
    }

    private static class ReusableResourceConstructor extends Constructor {

        ReusableResourceConstructor(ISolver solver, IScope enclosingScope, IField... parameters) {
            super(solver, enclosingScope, parameters);
        }

        @Override
        public void init(IObject instance) {
        }

        @Override
        public void invoke(IObject instance, IObject... expressions) {
            IConstraintNetwork network = solver.getConstraintNetwork();
            instance.set(CAPACITY, expressions[0]);
            solver.getCurrentNode().addResolver(new IResolver() {

                private boolean resolved = false;

                @Override
                public double getKnownCost() {
                    return 0;
                }

                @Override
                public void resolve() {
                    assert !resolved;
                    network.assertFacts(network.geq((INumber) expressions[0], network.newReal("0")));
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

    private static class ReusableResourcePredicate extends Predicate {

        ReusableResourcePredicate(ISolver solver, IScope enclosingScope, IField... parameters) {
            super(solver, enclosingScope, USE_PREDICATE_NAME, parameters);
        }

        @Override
        public void applyRule(IFormula formula) {
            formula.setActiveState();
        }
    }
}
