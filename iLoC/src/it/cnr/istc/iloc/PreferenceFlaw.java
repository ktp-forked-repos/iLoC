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

import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IPreference;
import it.cnr.istc.iloc.api.IPreferenceFlaw;
import it.cnr.istc.iloc.api.IRelaxedPlanningGraph;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PreferenceFlaw implements IPreferenceFlaw {

    private final IEnvironment environment;
    private final IRelaxedPlanningGraph rpg;
    private final IPreference preference;
    private final IStaticCausalGraph staticCausalGraph;

    public PreferenceFlaw(IEnvironment environment, IPreference preference) {
        this.environment = environment;
        this.rpg = environment.getSolver().getRelaxedPlanningGraph();
        this.preference = preference;
        this.staticCausalGraph = environment.getSolver().getStaticCausalGraph();
    }

    @Override
    public IPreference getPreference() {
        return preference;
    }

    @Override
    public double getEstimatedCost() {
        return rpg.level(staticCausalGraph.getNode(preference));
    }

    @Override
    public Collection<IResolver> getResolvers() {
        final Collection<IResolver> resolvers = new ArrayList<>(2);
        resolvers.add(new IResolver() {
            private boolean expanded = false;
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return 0;
            }

            @Override
            public void resolve() {
                assert !resolved;
                resolved = true;
                if (!expanded) {
                    expanded = true;
                    preference.execute(environment);
                }
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
        resolvers.add(new IResolver() {
            private boolean resolved = false;

            @Override
            public double getKnownCost() {
                return environment.getSolver().getConstraintNetwork().getModel().evaluate(preference.getCost()).doubleValue();
            }

            @Override
            public void resolve() {
                assert !resolved;
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
        return resolvers;
    }

    @Override
    public String toString() {
        return "preference";
    }
}
