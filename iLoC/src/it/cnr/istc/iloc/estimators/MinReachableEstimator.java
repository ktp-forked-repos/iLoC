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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Heuristic estimator which estimates the costs of the nodes considering the
 * currently active formulas of the dynamic causal graph.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class MinReachableEstimator implements IEstimator {

    private final ISolver solver;
    private final Map<IStaticCausalGraph.INode, Set<IStaticCausalGraph.INode>> min_reachable_nodes = new HashMap<>();

    public MinReachableEstimator(ISolver solver) {
        this.solver = solver;
    }

    @Override
    public void recomputeCosts() {
        min_reachable_nodes.clear();
        Collection<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes();
        nodes.forEach(node -> {
            min_reachable_nodes.put(node, estimate(node, new HashSet<>(nodes.size())));
        });
    }

    private Set<IStaticCausalGraph.INode> estimate(IStaticCausalGraph.INode node, Set<IStaticCausalGraph.INode> visited) {
        if (!min_reachable_nodes.containsKey(node)) {
            min_reachable_nodes.put(node, estimate(node, new HashSet<>()));
        }
        if (!visited.contains(node)) {
            if (node instanceof IStaticCausalGraph.IPredicateNode) {
                if (((IStaticCausalGraph.IPredicateNode) node).getPredicate().getInstances().stream().map(instance -> (IFormula) instance).noneMatch(formula -> formula.getFormulaState() == FormulaState.Active)) {
                    visited.add(node);
                } else {
                    return visited;
                }
            }
            if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
                int min_nodes_size = Integer.MAX_VALUE;
                Set<IStaticCausalGraph.INode> min_nodes = null;
                for (IDisjunct disjunct : ((IStaticCausalGraph.IDisjunctionNode) node).getDisjunction().getDisjuncts()) {
                    Set<IStaticCausalGraph.INode> c_nodes = estimate(solver.getStaticCausalGraph().getNode(disjunct), new HashSet<>(visited));
                    if (c_nodes.size() < min_nodes_size) {
                        min_nodes_size = c_nodes.size();
                        min_nodes = c_nodes;
                    }
                }
                visited.addAll(min_nodes);
            } else if (!(node instanceof IStaticCausalGraph.INoOpNode)) {
                visited.addAll(node.getExitingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).flatMap(edge -> estimate(edge.getTarget(), visited).stream()).collect(Collectors.toSet()));
            }
            return visited;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return min_reachable_nodes.get(node).size();
    }

    @Override
    public double estimate(Collection<IStaticCausalGraph.INode> nodes) {
        return nodes.stream().mapToDouble(node -> estimate(node)).sum();
    }
}