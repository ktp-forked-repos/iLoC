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

import it.cnr.istc.iloc.api.Constants;
import it.cnr.istc.iloc.api.IConstraintNetwork;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IModel;
import it.cnr.istc.iloc.api.INumber;
import it.cnr.istc.iloc.api.IObject;
import it.cnr.istc.iloc.api.ISolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class BatteryTimeline {

    final ISolver solver;
    final IModel model;
    final IObject battery;
    final INumber initial_amount;
    final INumber final_amount;
    final INumber capacity;
    final List<Number> pulses;
    final List<BatteryValue> values;

    BatteryTimeline(ISolver solver, IModel model, IObject battery, Collection<IFormula> formulas) {
        this.solver = solver;
        this.model = model;
        this.battery = battery;
        this.initial_amount = (INumber) battery.get(BatteryType.INITIAL_AMOUNT);
        this.final_amount = (INumber) battery.get(BatteryType.FINAL_AMOUNT);
        this.capacity = ((INumber) battery.get(BatteryType.CAPACITY));
        this.pulses = new ArrayList<>(formulas.size() * 2);
        this.values = new ArrayList<>(formulas.size() * 2);
        IConstraintNetwork network = solver.getConstraintNetwork();
        INumber c_amount = battery.get(BatteryType.INITIAL_AMOUNT);
        INumber min_amount = c_amount;
        INumber max_amount = c_amount;
        // For each pulse the charging_tokens starting at that pulse
        Map<Number, Collection<IFormula>> starting_values = new HashMap<>(formulas.size());
        // For each pulse the charging_tokens ending at that pulse
        Map<Number, Collection<IFormula>> ending_values = new HashMap<>(formulas.size());
        // The pulses of the timeline
        Set<Number> c_pulses = new HashSet<>(formulas.size() * 2);
        c_pulses.add(model.evaluate((INumber) solver.get(Constants.ORIGIN)));
        c_pulses.add(model.evaluate((INumber) solver.get(Constants.HORIZON)));

        formulas.stream().forEach(formula -> {
            Number start_pulse = model.evaluate((INumber) formula.get(Constants.START));
            c_pulses.add(start_pulse);
            if (!starting_values.containsKey(start_pulse)) {
                starting_values.put(start_pulse, new ArrayList<>(formulas.size()));
            }
            starting_values.get(start_pulse).add(formula);
            Number end_pulse = model.evaluate((INumber) formula.get(Constants.END));
            c_pulses.add(end_pulse);
            if (!ending_values.containsKey(end_pulse)) {
                ending_values.put(end_pulse, new ArrayList<>(formulas.size()));
            }
            ending_values.get(end_pulse).add(formula);
        });

        // Sort current pulses
        Number[] c_pulses_array = c_pulses.toArray(new Number[c_pulses.size()]);
        Arrays.sort(c_pulses_array);

        // Push values to timeline according to pulses...
        List<IFormula> current_formulas = new ArrayList<>(formulas.size());
        this.pulses.add(c_pulses_array[0]);
        if (starting_values.containsKey(c_pulses_array[0])) {
            current_formulas.addAll(starting_values.get(c_pulses_array[0]));
            for (IFormula f : starting_values.get(c_pulses_array[0])) {
                switch (f.getType().getName()) {
                    case BatteryType.CHARGE_PREDICATE_NAME:
                        max_amount = network.add(max_amount, (INumber) f.get(BatteryType.C_AMOUNT));
                        break;
                    case BatteryType.CONSUME_PREDICATE_NAME:
                        min_amount = network.subtract(min_amount, (INumber) f.get(BatteryType.AMOUNT));
                        break;
                    default:
                        throw new AssertionError(f.getType().getName());
                }
            }
        }
        if (ending_values.containsKey(c_pulses_array[0])) {
            current_formulas.removeAll(ending_values.get(c_pulses_array[0]));
            for (IFormula f : ending_values.get(c_pulses_array[0])) {
                switch (f.getType().getName()) {
                    case BatteryType.CHARGE_PREDICATE_NAME:
                        min_amount = network.add(min_amount, (INumber) f.get(BatteryType.C_AMOUNT));
                        break;
                    case BatteryType.CONSUME_PREDICATE_NAME:
                        max_amount = network.subtract(max_amount, (INumber) f.get(BatteryType.AMOUNT));
                        break;
                    default:
                        throw new AssertionError(f.getType().getName());
                }
            }
        }
        for (int i = 1; i < c_pulses_array.length; i++) {
            INumber m = network.newReal("0");
            for (IFormula f : current_formulas) {
                switch (f.getType().getName()) {
                    case BatteryType.CHARGE_PREDICATE_NAME:
                        m = network.add(m, network.divide(f.get(BatteryType.C_AMOUNT), network.subtract(f.get(Constants.END), f.get(Constants.START))));
                        break;
                    case BatteryType.CONSUME_PREDICATE_NAME:
                        m = network.subtract(m, network.divide(f.get(BatteryType.AMOUNT), network.subtract(f.get(Constants.END), f.get(Constants.START))));
                        break;
                    default:
                        throw new AssertionError(f.getType().getName());
                }
            }
            m = network.multiply(m, network.subtract(network.newReal(c_pulses_array[i].toString()), network.newReal(c_pulses_array[i - 1].toString())));
            this.values.add(new BatteryValue(min_amount, max_amount, c_amount, network.add(c_amount, m)));
            this.pulses.add(c_pulses_array[i]);
            c_amount = network.add(c_amount, m);
            if (starting_values.containsKey(c_pulses_array[i])) {
                current_formulas.addAll(starting_values.get(c_pulses_array[i]));
                for (IFormula f : starting_values.get(c_pulses_array[i])) {
                    switch (f.getType().getName()) {
                        case BatteryType.CHARGE_PREDICATE_NAME:
                            max_amount = network.add(max_amount, (INumber) f.get(BatteryType.C_AMOUNT));
                            break;
                        case BatteryType.CONSUME_PREDICATE_NAME:
                            min_amount = network.subtract(min_amount, (INumber) f.get(BatteryType.AMOUNT));
                            break;
                        default:
                            throw new AssertionError(f.getType().getName());
                    }
                }
            }
            if (ending_values.containsKey(c_pulses_array[i])) {
                current_formulas.removeAll(ending_values.get(c_pulses_array[i]));
                for (IFormula f : ending_values.get(c_pulses_array[i])) {
                    switch (f.getType().getName()) {
                        case BatteryType.CHARGE_PREDICATE_NAME:
                            min_amount = network.add(min_amount, (INumber) f.get(BatteryType.C_AMOUNT));
                            break;
                        case BatteryType.CONSUME_PREDICATE_NAME:
                            max_amount = network.subtract(max_amount, (INumber) f.get(BatteryType.AMOUNT));
                            break;
                        default:
                            throw new AssertionError(f.getType().getName());
                    }
                }
            }
        }
    }

    static class BatteryValue {

        final INumber max_amount;
        final INumber min_amount;
        final INumber initial_amount;
        final INumber final_amount;

        BatteryValue(INumber min_amount, INumber max_amount, INumber initial_amount, INumber final_amount) {
            this.min_amount = min_amount;
            this.max_amount = max_amount;
            this.initial_amount = initial_amount;
            this.final_amount = final_amount;
        }
    }
}
