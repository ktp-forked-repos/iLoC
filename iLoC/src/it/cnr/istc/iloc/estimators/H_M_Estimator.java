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
import it.cnr.istc.iloc.utils.CombinationGenerator;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final int m;
    private final Map<Object, Double> table = new HashMap<>();

    public H_M_Estimator(ISolver solver, int m) {
        this.solver = solver;
        this.m = m;
    }

    @Override
    public void recomputeCosts() {
        table.clear();
        IStaticCausalGraph cg = solver.getStaticCausalGraph();
        List<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes().stream().filter(node -> !node.getIncomingEdges().isEmpty() || !node.getOutgoingEdges().isEmpty()).collect(Collectors.toList());
        List<IFormula> active_formulas = nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Active)).collect(Collectors.toList());
        int c_m = Math.min(m, active_formulas.stream().map(formula -> formula.getType()).collect(Collectors.toSet()).size());

        // Initialization..
        for (int i = 1; i <= c_m; i++) {
            for (IStaticCausalGraph.INode[] c_nodes : new CombinationGenerator<>(i, active_formulas.stream().map(formula -> cg.getNode(formula.getType())).toArray(IStaticCausalGraph.INode[]::new))) {
                table.put(new HashSet<>(Arrays.asList(c_nodes)), 0d);
            }
        }

        nodes.stream().filter(node -> !table.containsKey(node)).forEach(node -> table.put(new HashSet<>(Arrays.asList(node)), Double.POSITIVE_INFINITY));
        for (int i = 2; i <= c_m; i++) {
            for (IStaticCausalGraph.INode[] c_nodes : new CombinationGenerator<>(i, nodes.stream().toArray(IStaticCausalGraph.INode[]::new))) {
                HashSet<IStaticCausalGraph.INode> ns = new HashSet<>(Arrays.asList(c_nodes));
                if (!table.containsKey(ns)) {
                    table.put(ns, Stream.of(c_nodes).mapToDouble(node -> table.get(node)).sum());
                }
            }
        }

        // Main loop (repeat until no change)
        Collection<IStaticCausalGraph.INode> c_nodes = new LinkedList<>(nodes);

        boolean changed;
        do {
            changed = false;
            for (IStaticCausalGraph.INode c_node : c_nodes) {
                if (c_node.toString().equals("Pick_up_a(BlocksAgent scope, number at)")) {
                    System.out.println(c_node);
                }
                double c1;
                if (c_node instanceof IStaticCausalGraph.IPreferenceNode) {
                    throw new UnsupportedOperationException("Preferences estimation is not supported yet..");
                } else {
                    c1 = 1 + evaluate(c_m, c_node.getOutgoingEdges().stream().map(edge -> edge.getTarget()).collect(Collectors.toSet()));
                }
                // update single atoms added by action a
                if (table.get(new HashSet<>(Arrays.asList(c_node))) > c1) {
                    table.put(new HashSet<>(Arrays.asList(c_node)), c1);
                    changed = true;
                }

                for (int i = 1; i < c_m; i++) {
                    for (IStaticCausalGraph.INode[] other_nodes : new CombinationGenerator<>(i, nodes.stream().toArray(IStaticCausalGraph.INode[]::new))) {
                        HashSet<IStaticCausalGraph.INode> ns = new HashSet<>(Arrays.asList(other_nodes));
                        if (!ns.contains(c_node)) {
                            double c2 = 1 + evaluate(c_m, Stream.concat(c_node.getOutgoingEdges().stream().map(edge -> edge.getTarget()), Stream.of(other_nodes)).collect(Collectors.toSet()));
                            // update other atoms
                            if (table.get(ns) > c2) {
                                table.put(ns, c2);
                                changed = true;
                            }
                        }
                    }
                }
            }
        } while (changed);
    }

    private double evaluate(int k, Collection<IStaticCausalGraph.INode> nodes) {
        assert k > 0;
        assert !nodes.isEmpty();
        double v = 0;
        for (int i = 1; i <= k; i++) {
            for (IStaticCausalGraph.INode[] c_nodes : new CombinationGenerator<>(i, nodes.stream().toArray(IStaticCausalGraph.INode[]::new))) {
                System.out.println(new HashSet<>(Arrays.asList(c_nodes)));
                v = Math.max(v, table.get(new HashSet<>(Arrays.asList(c_nodes))));
            }
        }
        return v;
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return table.get(new HashSet<>(Arrays.asList(node)));
    }
}
