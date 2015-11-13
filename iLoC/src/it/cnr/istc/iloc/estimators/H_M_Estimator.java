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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An adaptation of the h_n heuristic as described in
 * <P>
 * Haslum, Patrik, and Héctor Geffner. "Admissible Heuristics for Optimal
 * Planning." AIPS. 2000.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class H_M_Estimator implements IEstimator {

    private final ISolver solver;
    private final Map<Object, Double> table = new HashMap<>();

    public H_M_Estimator(ISolver solver) {
        this.solver = solver;
    }

    @Override
    public void recomputeCosts() {
        table.clear();
        IStaticCausalGraph cg = solver.getStaticCausalGraph();
        List<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes().stream().collect(Collectors.toList());
        List<IFormula> active_formulas = nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Active)).collect(Collectors.toList());

        // Initialization..
        active_formulas.forEach(formula -> table.put(cg.getNode(formula.getType()), 0d));
        nodes.stream().filter(node -> !table.containsKey(node)).forEach(node -> table.put(node, Double.POSITIVE_INFINITY));

        // Main loop (repeat until no change)
        Collection<IStaticCausalGraph.INode> c_nodes = new LinkedList<>(nodes);
        active_formulas.forEach(formula -> c_nodes.remove(cg.getNode(formula.getType())));

        boolean changed;
        do {
            changed = false;
            Collection<IStaticCausalGraph.INode> to_remove = new ArrayList<>();
            for (IStaticCausalGraph.INode c_node : c_nodes) {
                double c1;
                if (c_node instanceof IStaticCausalGraph.IDisjunctionNode) {
                    c1 = 1 + c_node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).mapToDouble(node -> table.get(node)).min().orElse(Double.POSITIVE_INFINITY);
                } else if (c_node instanceof IStaticCausalGraph.IPreferenceNode) {
                    throw new UnsupportedOperationException("Preferences estimation is not supported yet..");
                } else {
                    c1 = 1 + c_node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).mapToDouble(node -> table.get(node)).max().orElse(Double.POSITIVE_INFINITY);
                }
                // update single atoms added by action a
                if (table.get(c_node) > c1) {
                    table.put(c_node, c1);
                    to_remove.add(c_node);
                    changed = true;
                }
            }
            c_nodes.removeAll(to_remove);
            to_remove.clear();
        } while (changed);

        // c_nodes contains nodes which are unreachable from the facts..
        // these nodes might still be reachable from the goals!
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return table.get(node);
    }
}
