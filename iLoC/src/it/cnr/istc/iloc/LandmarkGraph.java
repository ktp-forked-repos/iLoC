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
import it.cnr.istc.iloc.api.ILandmarkGraph;
import it.cnr.istc.iloc.api.ISolver;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a landmark graph.
 * <p>
 * The method used for extracting landmarks is an adaptation of the method
 * described in "Extending landmarks analysis to reason about resources and
 * repetition", Julie Porteous & Stephen Cresswell, Proceedings of the 21st
 * Workshop of the UK Planning and Scheduling Special Interest Group
 * (PLANSIG'02), 2002
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class LandmarkGraph implements ILandmarkGraph {

    private final ISolver solver;
    private final IStaticCausalGraph causal_graph;
    private RelaxedPlanningGraph rpg;
    /**
     * a set of landmark candidates
     */
    private final Set<ILandmark> candidates = new HashSet<>();
    /**
     * the final landmark set
     */
    private final Set<ILandmark> landmarks = new HashSet<>();
    private final Map<ILandmark, RelaxedPlanningGraph> rpgs = new HashMap<>();

    /**
     * Landmark graph constructor.
     *
     * @param solver the solver for which landmarks are computed.
     */
    LandmarkGraph(ISolver solver) {
        this.solver = solver;
        this.causal_graph = solver.getStaticCausalGraph();
    }

    @Override
    public void extractLandmarks() {
        candidates.clear();
        landmarks.clear();

        Set<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes().stream().collect(Collectors.toSet());
        // We define the initial state ..
        Set<IStaticCausalGraph.INode> init_state = nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Active)).map(formula -> causal_graph.getNode(formula.getType())).collect(Collectors.toSet());
        // .. and add high level goals to initial landmark candidates
        nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Inactive)).map(formula -> causal_graph.getNode(formula.getType())).filter(node -> !init_state.contains(node)).forEach(node -> candidates.add(new Landmark(node)));

        // We create a relaxed planning graph starting fron the initial state and the current goals
        rpg = new RelaxedPlanningGraph(solver, nodes, true);

        // Main landmark extraction procedure loop
        while (!candidates.isEmpty()) {
            // the landmark candidate to analyze
            ILandmark candidate = candidates.stream().findAny().get();

            // we remove the landmark candidate from the candidates..
            candidates.remove(candidate);
            // .. and we add it to the landmarks
            landmarks.add(candidate);

            // these are the (disjunctive) causal preconditions of the first achievers..
            Set<Set<IStaticCausalGraph.INode>> first_achievers_preconditions = new HashSet<>();
            candidate.getNodes().forEach(node -> first_achievers_preconditions.addAll(getPreconditions(node)));
            // we compute the relaxed planning graph excluding the candidate ..
            RelaxedPlanningGraph c_rpg = new RelaxedPlanningGraph(solver, nodes.stream().filter(node -> !candidate.getNodes().contains(node)).collect(Collectors.toSet()), false);
            rpgs.put(candidate, c_rpg);
            // .. and extract the causal preconditions of the first achievers according to the relaxed planning graph
            // specifically, we remove those causal preconditions which are not reachable according to the relaxed planning graph without the candidate
            first_achievers_preconditions.removeIf(preconditions -> preconditions.stream().anyMatch(pre -> Double.isInfinite(c_rpg.level(pre))));

            // we compute the intersection of the preconditions
            Set<IStaticCausalGraph.INode> intersection = new HashSet<>(first_achievers_preconditions.stream().findAny().get());
            first_achievers_preconditions.forEach(conjunction -> {
                intersection.retainAll(conjunction);
            });
            intersection.removeIf(node -> init_state.contains(node) || landmarks.contains(new Landmark(node)));

            // .. and add it to candidates
            intersection.forEach(node -> candidates.add(new Landmark(node)));

            // we compute a disjunctive landmark with what is left..
            Set<IStaticCausalGraph.INode> symmetric_difference = first_achievers_preconditions.stream().filter(conjunction -> conjunction.stream().noneMatch(node -> init_state.contains(node) || intersection.contains(node))).flatMap(conjunction -> conjunction.stream()).collect(Collectors.toSet());

            // .. and add it to the candidates
            if (!symmetric_difference.isEmpty() && !candidates.stream().anyMatch(c -> symmetric_difference.containsAll(c.getNodes())) && !landmarks.stream().anyMatch(c -> symmetric_difference.containsAll(c.getNodes()))) {
                candidates.add(new Landmark(symmetric_difference));
            }
        }

        // we compute natural orders between landmarks..
        ILandmark[] lms = landmarks.stream().toArray(ILandmark[]::new);
        for (int i = 0; i < lms.length; i++) {
            RelaxedPlanningGraph c_rpg = rpgs.get(lms[i]);
            for (int j = i + 1; j < lms.length; j++) {
                if (lms[j].getNodes().stream().allMatch(node -> Double.isInfinite(c_rpg.level(node)))) {
                    // there is a natural order between landmarks lms[i] and lms[j]
                }
            }
        }
    }

    /**
     * This method returns a disjunction of conjunctions. The returned nodes
     * represent the causal preconditions of the given node.
     *
     * @param node the node of which we want to compute the causal
     * preconditions.
     * @return a disjunction of conjunctions representing the causal
     * preconditions of the given node.
     */
    private Set<Set<IStaticCausalGraph.INode>> getPreconditions(IStaticCausalGraph.INode node) {
        if (node instanceof IStaticCausalGraph.IPredicateNode || node instanceof IStaticCausalGraph.IDisjunctNode) {
            return new HashSet<>(Arrays.asList(new HashSet<>(node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).collect(Collectors.toSet()))));
        } else if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
            return node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> new HashSet<>(Arrays.asList(edge.getTarget()))).collect(Collectors.toSet());
        } else {
            throw new UnsupportedOperationException(node.getClass().getName());
        }
    }

    /**
     * Returns the computed (disjunctive) landmarks.
     *
     * @return a set containing the computed (disjunctive) landmarks.
     */
    @Override
    public Set<ILandmark> getLandmarks() {
        return Collections.unmodifiableSet(landmarks);
    }

    @Override
    public double estimate(IStaticCausalGraph.INode node) {
        return rpg.level(node);
    }

    /**
     * This class is used to represent a disjunctive landmark.
     */
    static class Landmark implements ILandmark {

        private final Set<IStaticCausalGraph.INode> nodes;

        Landmark(IStaticCausalGraph.INode... nodes) {
            this.nodes = new HashSet<>(Arrays.asList(nodes));
        }

        Landmark(Set<IStaticCausalGraph.INode> nodes) {
            this.nodes = nodes;
        }

        @Override
        public Set<IStaticCausalGraph.INode> getNodes() {
            return Collections.unmodifiableSet(nodes);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.nodes);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Landmark other = (Landmark) obj;
            return Objects.equals(this.nodes, other.nodes);
        }

        @Override
        public String toString() {
            return nodes.toString();
        }
    }
}
