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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of a landmark graph.
 * <p>
 * Landmarks are extracted according to "Extending landmarks analysis to reason
 * about resources and repetition", Julie Porteous & Stephen Cresswell,
 * Proceedings of the 21st Workshop of the UK Planning and Scheduling Special
 * Interest Group (PLANSIG'02), 2002
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class LandmarkGraph {

    private final Set<Landmark> candidates = new HashSet<>();
    private final Set<Landmark> landmarks = new HashSet<>();

    LandmarkGraph(ISolver solver) {
        IStaticCausalGraph causal_graph = solver.getStaticCausalGraph();
        Set<IStaticCausalGraph.INode> nodes = solver.getStaticCausalGraph().getNodes().stream().collect(Collectors.toSet());
        Set<IStaticCausalGraph.INode> init_state = nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Active)).map(formula -> causal_graph.getNode(formula.getType())).collect(Collectors.toSet());
        nodes.stream().filter(node -> node instanceof IStaticCausalGraph.IPredicateNode).map(node -> (IStaticCausalGraph.IPredicateNode) node).flatMap(predicate -> predicate.getPredicate().getInstances().stream().map(instance -> (IFormula) instance).filter(formula -> formula.getFormulaState() == FormulaState.Inactive)).map(formula -> causal_graph.getNode(formula.getType())).filter(node -> !init_state.contains(node)).forEach(node -> candidates.add(new Landmark(node)));

        candidates.forEach(candidate -> System.out.println(candidate));

        while (!candidates.isEmpty()) {
            Landmark candidate = candidates.stream().findAny().get();

            System.out.println(candidate);

            // we remove the landmark candidate from the candidates..
            candidates.remove(candidate);
            // .. and we add it to the landmarks
            landmarks.add(candidate);

            // we compute the relaxed planning graph excluding the candidate ..
            RelaxedPlanningGraph rpg = new RelaxedPlanningGraph(solver, solver.getStaticCausalGraph().getNodes().stream().filter(node -> !candidate.nodes.contains(node)).collect(Collectors.toSet()));
            // .. and extract the first achievers for the candidate
            List<IStaticCausalGraph.INode> first_achievers = candidate.nodes.stream().filter(node -> node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).allMatch(target -> !Double.isInfinite(rpg.estimate(target)))).collect(Collectors.toList());

            first_achievers.forEach(first_achiever -> System.out.println(first_achiever));

            // we compute the preconditions of the achievers
            Set<Set<IStaticCausalGraph.INode>> preconditions = new HashSet<>();
            first_achievers.forEach(first_achiever -> preconditions.addAll(getPreconditions(first_achiever)));

            preconditions.forEach(pre -> System.out.println(pre));

            // we compute the intersection of the preconditions
            Set<IStaticCausalGraph.INode> intersection = new HashSet<>(preconditions.stream().findAny().get());
            preconditions.forEach(conjunction -> {
                intersection.retainAll(conjunction);
            });
            intersection.removeIf(node -> init_state.contains(node) || landmarks.contains(new Landmark(node)));
            System.out.println(intersection);

            // .. and add it to candidates
            intersection.forEach(node -> candidates.add(new Landmark(node)));

            // we compute a disjunctive landmark with what is left..
            Set<IStaticCausalGraph.INode> symmetric_difference = preconditions.stream().filter(conjunction -> conjunction.stream().noneMatch(node -> init_state.contains(node) || intersection.contains(node))).flatMap(conjunction -> conjunction.stream()).collect(Collectors.toSet());

            System.out.println(symmetric_difference);

            // .. and add it to the candidates
            if (!symmetric_difference.isEmpty() && !candidates.stream().anyMatch(c -> symmetric_difference.containsAll(c.nodes)) && !landmarks.stream().anyMatch(c -> symmetric_difference.containsAll(c.nodes))) {
                System.out.println("to achieve");
                System.out.println("    " + candidate);
                System.out.println("  is required");
                System.out.println("    " + symmetric_difference);
                candidates.add(new Landmark(symmetric_difference));
            }
        }
    }

    private Set<Set<IStaticCausalGraph.INode>> getPreconditions(IStaticCausalGraph.INode node) {
        if (node instanceof IStaticCausalGraph.IPredicateNode || node instanceof IStaticCausalGraph.IDisjunctNode) {
            return new HashSet<>(Arrays.asList(new HashSet<>(node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> edge.getTarget()).collect(Collectors.toSet()))));
        } else if (node instanceof IStaticCausalGraph.IDisjunctionNode) {
            return node.getOutgoingEdges().stream().filter(edge -> edge.getType() == IStaticCausalGraph.IEdge.Type.Goal).map(edge -> new HashSet<>(Arrays.asList(edge.getTarget()))).collect(Collectors.toSet());
        } else {
            throw new UnsupportedOperationException(node.getClass().getName());
        }
    }

    Set<Landmark> getLandmarks() {
        return Collections.unmodifiableSet(landmarks);
    }

    static class Landmark {

        private final Set<IStaticCausalGraph.INode> nodes;

        Landmark(IStaticCausalGraph.INode... nodes) {
            this.nodes = new HashSet<>(Arrays.asList(nodes));
        }

        Landmark(Set<IStaticCausalGraph.INode> nodes) {
            this.nodes = nodes;
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
