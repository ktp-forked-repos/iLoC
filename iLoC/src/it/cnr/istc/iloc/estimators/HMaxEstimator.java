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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An adaptation of the h_n heuristic as described in
 * <P>
 * Haslum, Patrik, and Héctor Geffner. "Admissible Heuristics for Optimal
 * Planning." AIPS. 2000.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class HMaxEstimator implements IEstimator {

    private final ISolver solver;
    private final Map<Object, Double> table = new HashMap<>();

    public HMaxEstimator(ISolver solver) {
        this.solver = solver;
    }

    @Override
    public void recomputeCosts() {
        table.clear();
        IStaticCausalGraph cg = solver.getStaticCausalGraph();
        Set<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes().stream().collect(Collectors.toSet());
        Set<IStaticCausalGraph.INode> init_state = nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Active).map(formula -> cg.getNode(formula.getType()))).collect(Collectors.toSet());
        Set<IStaticCausalGraph.INode> goal = nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Inactive).map(formula -> cg.getNode(formula.getType())).filter(node -> !init_state.contains(node))).collect(Collectors.toSet());

        // Initialization..
        init_state.forEach(node -> table.put(node, 0d));
        nodes.stream().filter(node -> !table.containsKey(node)).forEach(node -> table.put(node, Double.POSITIVE_INFINITY));

        // Main loop (repeat until no change)
        Collection<IStaticCausalGraph.INode> c_nodes = new LinkedList<>(nodes);
        init_state.forEach(node -> c_nodes.remove(node));

        boolean changed;
        do {
            changed = false;
            Collection<IStaticCausalGraph.INode> to_remove = new ArrayList<>();
            for (IStaticCausalGraph.INode c_node : c_nodes) {
                double c1;
                if (c_node instanceof IStaticCausalGraph.IDisjunctionNode) {
                    c1 = 1 + c_node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).mapToDouble(node -> table.containsKey(node) ? table.get(node) : Double.POSITIVE_INFINITY).min().orElse(Double.POSITIVE_INFINITY);
                } else if (c_node instanceof IStaticCausalGraph.IPreferenceNode) {
                    throw new UnsupportedOperationException("Preferences estimation is not supported yet..");
                } else {
                    c1 = 1 + c_node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).mapToDouble(node -> table.containsKey(node) ? table.get(node) : Double.POSITIVE_INFINITY).max().orElse(Double.POSITIVE_INFINITY);
                }
                // update single value by rule c_node
                if (table.get(c_node) > c1) {
                    table.put(c_node, c1);
                    to_remove.add(c_node);
                    changed = true;
                }
            }
            c_nodes.removeAll(to_remove);
            to_remove.clear();
        } while (changed);

        table.values().removeIf(value -> Double.isInfinite(value));

        // c_nodes contains nodes which are unreachable from the facts..
        // these nodes might still be reachable from the goals!
        goal.forEach(g -> estimate(g, new HashSet<>()));

        // not stored nodes are now definitely unreachable..
        c_nodes.stream().filter(node -> !table.containsKey(node)).forEach(node -> table.put(node, Double.POSITIVE_INFINITY));
    }

    private double estimate(IStaticCausalGraph.INode node, Set<IStaticCausalGraph.INode> visited) {
        if (table.containsKey(node)) {
            return table.get(node);
        }
        if (!visited.contains(node)) {
            visited.add(node);
            double c1;
            if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
                c1 = 1 + node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).mapToDouble(c_node -> estimate(c_node, new HashSet<>(visited))).min().orElse(0);
            } else if (node instanceof IStaticCausalGraph.IPreferenceNode) {
                throw new UnsupportedOperationException("Preferences estimation is not supported yet..");
            } else {
                c1 = 1 + node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).mapToDouble(c_node -> estimate(c_node, visited)).max().orElse(0);
            }
            // We can admit infinite values now..
            table.put(node, c1);
            return c1;
        } else {
            return 0;
        }
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return table.get(node);
    }
}
