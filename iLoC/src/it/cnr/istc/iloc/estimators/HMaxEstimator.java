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
import it.cnr.istc.iloc.api.IEstimator;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An adaptation of the h_max heuristic as described in
 * <P>
 * Haslum, Patrik, and Héctor Geffner. "Admissible Heuristics for Optimal
 * Planning." AIPS. 2000.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class HMaxEstimator implements IEstimator {

    private final ISolver solver;
    private final Map<IStaticCausalGraph.INode, Integer> min_causal_distance = new HashMap<>();

    public HMaxEstimator(ISolver solver) {
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
                return ((IStaticCausalGraph.IDisjunctionNode) node).getDisjunction().getDisjuncts().stream().mapToInt(disjunct -> estimate(solver.getStaticCausalGraph().getNode(disjunct), new HashSet<>(visited))).min().orElse(0);
            } else if (!(node instanceof IStaticCausalGraph.INoOpNode)) {
                return 1 + node.getExitingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).mapToInt(edge -> estimate(edge.getTarget(), visited)).max().orElse(0);
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

    @Override
    public double estimate(Collection<IStaticCausalGraph.INode> nodes) {
        return nodes.stream().mapToDouble(node -> estimate(node)).max().orElse(0);
    }
}
