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
class ReusableResourceTimeline {

    final ISolver solver;
    final IModel model;
    final IObject reusable_resource;
    final INumber var_capacity;
    final List<Number> pulses;
    final List<ResourceValue> values;

    ReusableResourceTimeline(ISolver solver, IModel model, IObject reusable_resource, Collection<IFormula> formulas) {
        this.solver = solver;
        this.model = model;
        this.reusable_resource = reusable_resource;
        this.var_capacity = reusable_resource.get(ReusableResourceType.CAPACITY);
        this.pulses = new ArrayList<>(formulas.size() * 2);
        this.values = new ArrayList<>(formulas.size() * 2);
        IConstraintNetwork network = solver.getConstraintNetwork();
        // For each pulse the tokens starting at that pulse
        Map<Number, Collection<IFormula>> starting_values = new HashMap<>(formulas.size());
        // For each pulse the tokens ending at that pulse
        Map<Number, Collection<IFormula>> ending_values = new HashMap<>(formulas.size());
        // The pulses of the timeline
        Set<Number> c_pulses = new HashSet<>(formulas.size() * 2);
        c_pulses.add(model.evaluate((INumber) solver.get(Constants.ORIGIN)));
        c_pulses.add(model.evaluate((INumber) solver.get(Constants.HORIZON)));

        formulas.forEach(f -> {
            Number start_pulse = model.evaluate((INumber) f.get(Constants.START));
            c_pulses.add(start_pulse);
            if (!starting_values.containsKey(start_pulse)) {
                starting_values.put(start_pulse, new ArrayList<>(formulas.size()));
            }
            starting_values.get(start_pulse).add(f);
            Number end_pulse = model.evaluate((INumber) f.get(Constants.END));
            c_pulses.add(end_pulse);
            if (!ending_values.containsKey(end_pulse)) {
                ending_values.put(end_pulse, new ArrayList<>(formulas.size()));
            }
            ending_values.get(end_pulse).add(f);
        });

        // Sort current pulses
        Number[] c_pulses_array = c_pulses.toArray(new Number[c_pulses.size()]);
        Arrays.sort(c_pulses_array);

        // Push values to timeline according to pulses...
        List<IFormula> overlapping_formulas = new ArrayList<>(formulas.size());
        this.pulses.add(c_pulses_array[0]);
        if (starting_values.containsKey(c_pulses_array[0])) {
            overlapping_formulas.addAll(starting_values.get(c_pulses_array[0]));
        }
        if (ending_values.containsKey(c_pulses_array[0])) {
            overlapping_formulas.removeAll(ending_values.get(c_pulses_array[0]));
        }
        for (int i = 1; i < c_pulses_array.length; i++) {
            INumber c_amount = network.newReal("0");
            for (IFormula f : overlapping_formulas) {
                c_amount = network.add(c_amount, (INumber) f.get(ReusableResourceType.AMOUNT));
            }
            this.values.add(new ResourceValue(model, c_amount, overlapping_formulas.toArray(new IFormula[overlapping_formulas.size()])));
            this.pulses.add(c_pulses_array[i]);
            if (starting_values.containsKey(c_pulses_array[i])) {
                overlapping_formulas.addAll(starting_values.get(c_pulses_array[i]));
            }
            if (ending_values.containsKey(c_pulses_array[i])) {
                overlapping_formulas.removeAll(ending_values.get(c_pulses_array[i]));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder timeline = new StringBuilder();
        timeline.append("capacity: ").append(model.evaluate(var_capacity)).append("\n");
        for (int i = 0; i < values.size(); i++) {
            timeline.append(pulses.get(i)).append(" - ").append(pulses.get(i + 1)).append(": ").append(model.evaluate(values.get(i).var_amount)).append("\n");
        }
        return timeline.toString();
    }

    static class ResourceValue {

        final INumber var_amount;
        final IFormula[] formulas;

        ResourceValue(IModel model, INumber var_amount, IFormula... formulas) {
            this.var_amount = var_amount;

            // TODO: This method should not be here but Z3 might not evaluate this value!
            model.evaluate(var_amount);

            this.formulas = formulas;
        }
    }
}
