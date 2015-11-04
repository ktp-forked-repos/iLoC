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
package it.cnr.istc.iloc.estimators;

import it.cnr.istc.iloc.api.FormulaState;
import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IEstimator;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class D0Estimator implements IEstimator {

    private final ISolver solver;
    private final Map<IStaticCausalGraph.INode, Integer> min_causal_distance = new HashMap<>();

    public D0Estimator(ISolver solver) {
        this.solver = solver;
    }

    @Override
    public void recomputeCosts() {
        min_causal_distance.clear();
        Collection<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes();
        nodes.forEach(node -> {
            min_causal_distance.put(node, estimate(node, new HashSet<>(nodes.size())));
        });
    }

    private int estimate(IStaticCausalGraph.INode node, Set<IStaticCausalGraph.INode> visited) {
        if (min_causal_distance.containsKey(node)) {
            return min_causal_distance.get(node);
        }
        if (!visited.contains(node)) {
            if (node instanceof IStaticCausalGraph.IPredicateNode) {
                if (((IStaticCausalGraph.IPredicateNode) node).getPredicate().getInstances().stream().map(instance -> (IFormula) instance).noneMatch(formula -> formula.getFormulaState() == FormulaState.Active)) {
                    visited.add(node);
                } else {
                    return 0;
                }
            }
            if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
                int min_distance = Integer.MAX_VALUE;
                for (IDisjunct disjunct : ((IStaticCausalGraph.IDisjunctionNode) node).getDisjunction().getDisjuncts()) {
                    int c_distance = estimate(solver.getStaticCausalGraph().getNode(disjunct), new HashSet<>(visited));
                    if (c_distance < min_distance) {
                        min_distance = c_distance;
                    }
                }
                return min_distance;
            } else if (!(node instanceof IStaticCausalGraph.INoOpNode)) {
                OptionalInt max = node.getExitingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).mapToInt(edge -> estimate(edge.getTarget(), visited)).max();
                return 1 + (max.isPresent() ? max.getAsInt() : 0);
            }
            return 0;
        } else {
            return 0;
        }
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return min_causal_distance.get(node);
    }
}
