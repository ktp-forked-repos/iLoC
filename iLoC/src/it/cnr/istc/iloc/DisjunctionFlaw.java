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

import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IDisjunction;
import it.cnr.istc.iloc.api.IDisjunctionFlaw;
import it.cnr.istc.iloc.api.IEnvironment;
import it.cnr.istc.iloc.api.IResolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class DisjunctionFlaw implements IDisjunctionFlaw {

    private final IEnvironment environment;
    private final IDisjunction disjunction;
    private final IStaticCausalGraph staticCausalGraph;

    public DisjunctionFlaw(IEnvironment environment, IDisjunction disjunction) {
        assert !disjunction.getDisjuncts().isEmpty();
        this.environment = environment;
        this.disjunction = disjunction;
        this.staticCausalGraph = environment.getSolver().getStaticCausalGraph();
    }

    @Override
    public IDisjunction getDisjunction() {
        return disjunction;
    }

    @Override
    public double getEstimatedCost() {
        return environment.getSolver().getStaticCausalGraph().estimateCost(environment.getSolver().getStaticCausalGraph().getNode(disjunction));
    }

    @Override
    public Collection<IResolver> getResolvers() {
        List<IDisjunct> disjuncts = new ArrayList<>(disjunction.getDisjuncts());
        Collections.sort(disjuncts, (IDisjunct d0, IDisjunct d1) -> Double.compare(staticCausalGraph.estimateCost(staticCausalGraph.getNode(d0)), staticCausalGraph.estimateCost(staticCausalGraph.getNode(d1))));
        return disjuncts.stream().map(disjunct -> new IResolver() {

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
                    // We create a new environment for each disjunct starting from the current environment..
                    disjunct.execute(new Environment(environment.getSolver(), environment));
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
        }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "disjunction [" + disjunction.getDisjuncts().size() + "]";
    }
}
