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

import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IEstimator;
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
 * Heuristic estimator which estimates the costs of the nodes without
 * considering the currently active formulas of the current status of the
 * dynamic causal graph.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class AllMinReachableEstimator implements IEstimator {

    private final ISolver solver;
    private final Map<IStaticCausalGraph.INode, Set<IStaticCausalGraph.INode>> all_min_reachable_nodes = new HashMap<>();

    public AllMinReachableEstimator(ISolver solver) {
        this.solver = solver;
    }

    @Override
    public void recomputeCosts() {
        all_min_reachable_nodes.clear();
        Collection<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes();
        nodes.forEach(node -> {
            all_min_reachable_nodes.put(node, estimate(node, new HashSet<>(nodes.size())));
        });
    }

    private Set<IStaticCausalGraph.INode> estimate(IStaticCausalGraph.INode node, Set<IStaticCausalGraph.INode> visited) {
        if (!all_min_reachable_nodes.containsKey(node)) {
            all_min_reachable_nodes.put(node, estimate(node, new HashSet<>()));
        }
        if (!visited.contains(node)) {
            if (node instanceof IStaticCausalGraph.IPredicateNode) {
                visited.add(node);
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
                visited.addAll(node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).flatMap(edge -> estimate(edge.getTarget(), visited).stream()).collect(Collectors.toSet()));
            }
            return visited;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return all_min_reachable_nodes.get(node).size();
    }
}
