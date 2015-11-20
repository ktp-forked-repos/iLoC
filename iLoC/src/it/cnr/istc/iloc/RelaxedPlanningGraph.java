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
package it.cnr.istc.iloc;

import it.cnr.istc.iloc.api.FormulaState;
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
 * Represents a relaxed planning graph (RPG) built using the given nodes of the
 * static causal graph.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class RelaxedPlanningGraph {

    private final ISolver solver;
    private final Set<IStaticCausalGraph.INode> nodes;
    private final Map<IStaticCausalGraph.INode, Double> table = new HashMap<>();
    private final Map<IStaticCausalGraph.INode, Double> old_table = new HashMap<>();

    RelaxedPlanningGraph(ISolver solver, Set<IStaticCausalGraph.INode> nodes) {
        this.solver = solver;
        this.nodes = nodes;
        IStaticCausalGraph cg = solver.getStaticCausalGraph();
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
                c1 = 1 + node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).filter(target -> nodes.contains(target)).mapToDouble(c_node -> estimate(c_node, new HashSet<>(visited))).min().orElse(0);
            } else if (node instanceof IStaticCausalGraph.IPreferenceNode) {
                throw new UnsupportedOperationException("Preferences estimation is not supported yet..");
            } else {
                c1 = 1 + node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).filter(target -> nodes.contains(target)).mapToDouble(c_node -> estimate(c_node, visited)).max().orElse(0);
            }
            // We can admit infinite values now..
            table.put(node, c1);
            return c1;
        } else {
            return 0;
        }
    }

    /**
     * Deactivates the given node setting its cost to infinite. Deactivation
     * propagates to all the other reachable nosed.
     *
     * @param node the node to be deactivated.
     */
    void deactivate(IStaticCausalGraph.INode node) {
        if (!Double.isInfinite(table.get(node))) {
            old_table.put(node, table.get(node));
            table.put(node, Double.POSITIVE_INFINITY);
            node.getIncomingEdges().stream()
                    .filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal)
                    .map(edge -> edge.getSource())
                    .filter(source -> !(source instanceof IStaticCausalGraph.IDisjunctionNode) || source.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).allMatch(n -> Double.isInfinite(table.get(n)))).forEach(source -> {
                deactivate(source);
            });
        }
    }

    /**
     * Restores the values of the table after deactivations.
     */
    void restore() {
        table.putAll(old_table);
        old_table.clear();
    }

    /**
     * Returns the level of the given causal node inside the current relaxed
     * planning graph.
     *
     * @param node the causal node whose level is requested.
     * @return the level of the given causal node.
     */
    double level(IStaticCausalGraph.INode node) {
        return table.get(node);
    }

    @Override
    public String toString() {
        return table.keySet().stream().sorted((IStaticCausalGraph.INode n0, IStaticCausalGraph.INode n1) -> Double.compare(table.get(n0), table.get(n1))).map(node -> node.toString() + "   " + table.get(node)).collect(Collectors.joining("\n"));
    }
}
