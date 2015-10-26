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
import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IDisjunction;
import it.cnr.istc.iloc.api.IFormula;
import it.cnr.istc.iloc.api.IPredicate;
import it.cnr.istc.iloc.api.IPreference;
import it.cnr.istc.iloc.api.IScope;
import it.cnr.istc.iloc.api.IStaticCausalGraph;
import it.cnr.istc.iloc.api.IStaticCausalGraphListener;
import it.cnr.istc.iloc.api.IType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StaticCausalGraph implements IStaticCausalGraph {

    private final Set<IType> types = new HashSet<>();
    private final Map<IPredicate, IPredicateNode> predicates = new IdentityHashMap<>();
    private final Map<IDisjunction, IDisjunctionNode> disjunctions = new IdentityHashMap<>();
    private final Map<IDisjunct, IDisjunctNode> disjuncts = new IdentityHashMap<>();
    private final Map<IPreference, IPreferenceNode> preferences = new IdentityHashMap<>();
    private final Map<INode, IScope> nodes = new IdentityHashMap<>();
    private final Map<INode, Map<INode, Collection<IEdge>>> links = new IdentityHashMap<>();
    private final Map<INode, Set<INode>> all_reachable_nodes = new HashMap<>();
    private final Map<INode, Set<INode>> all_min_reachable_nodes = new HashMap<>();
    private final Map<INode, Set<INode>> min_reachable_nodes = new HashMap<>();
    private final Map<INode, Integer> min_causal_distance = new HashMap<>();
    private final Collection<IStaticCausalGraphListener> listeners = new ArrayList<>();

    @Override
    public void addType(IType type) {
        assert !types.contains(type);
        types.add(type);
        listeners.stream().forEach(listener -> {
            listener.typeAdded(type);
        });
    }

    @Override
    public void removeType(IType type) {
        assert types.contains(type);
        types.remove(type);
        listeners.stream().forEach(listener -> {
            listener.typeRemoved(type);
        });
    }

    @Override
    public Collection<IType> getTypes() {
        return Collections.unmodifiableCollection(types);
    }

    @Override
    public IPredicateNode addNode(IPredicate predicate) {
        assert !predicates.containsKey(predicate);
        IPredicateNode node = new PredicateNode(predicate);
        predicates.put(predicate, node);
        nodes.put(node, predicate);
        links.put(node, new HashMap<>());
        listeners.stream().forEach(listener -> {
            listener.nodeAdded(node);
        });
        return node;
    }

    @Override
    public IPredicateNode getNode(IPredicate predicate) {
        return predicates.get(predicate);
    }

    @Override
    public IDisjunctionNode addNode(IDisjunction disjunction) {
        assert !disjunctions.containsKey(disjunction);
        IDisjunctionNode node = new DisjunctionNode(disjunction);
        disjunctions.put(disjunction, node);
        nodes.put(node, disjunction);
        links.put(node, new HashMap<>());
        listeners.stream().forEach(listener -> {
            listener.nodeAdded(node);
        });
        return node;
    }

    @Override
    public IDisjunctionNode getNode(IDisjunction disjunction) {
        return disjunctions.get(disjunction);
    }

    @Override
    public IDisjunctNode addNode(IDisjunct disjunct) {
        assert !disjuncts.containsKey(disjunct);
        IDisjunctNode node = new DisjunctNode(disjunct);
        disjuncts.put(disjunct, node);
        nodes.put(node, disjunct);
        links.put(node, new HashMap<>());
        listeners.stream().forEach(listener -> {
            listener.nodeAdded(node);
        });
        return node;
    }

    @Override
    public IDisjunctNode getNode(IDisjunct disjunct) {
        return disjuncts.get(disjunct);
    }

    @Override
    public IPreferenceNode addNode(IPreference preference) {
        assert !preferences.containsKey(preference);
        IPreferenceNode node = new PreferenceNode(preference);
        preferences.put(preference, node);
        nodes.put(node, preference);
        links.put(node, new HashMap<>());
        listeners.stream().forEach(listener -> {
            listener.nodeAdded(node);
        });
        return node;
    }

    @Override
    public IPreferenceNode getNode(IPreference preference) {
        return preferences.get(preference);
    }

    @Override
    public INoOpNode addNoOp() {
        INoOpNode node = new NoOpNode();
        links.put(node, new HashMap<>());
        listeners.stream().forEach(listener -> {
            listener.nodeAdded(node);
        });
        return node;
    }

    @Override
    public void removeNode(INode node) {
        assert nodes.containsKey(node);
        assert predicates.containsValue(node) || disjunctions.containsValue(node) || disjuncts.containsValue(node) || preferences.containsValue(node);
        IScope scope = nodes.get(node);
        predicates.remove(scope);
        disjunctions.remove(scope);
        disjuncts.remove(scope);
        preferences.remove(scope);
        links.remove(node, new HashMap<>());
        listeners.stream().forEach(listener -> {
            listener.nodeAdded(node);
        });
    }

    @Override
    public IEdge addEdge(IEdge.Type type, INode source, INode target) {
        Edge edge = new Edge(type, source, target);
        if (!links.get(source).containsKey(target)) {
            links.get(source).put(target, new ArrayList<>());
        }
        links.get(source).get(target).add(edge);
        listeners.stream().forEach(listener -> {
            listener.edgeAdded(edge);
        });
        return edge;
    }

    @Override
    public void removeEdge(IEdge edge) {
        links.get(edge.getSource()).get(edge.getTarget()).remove(edge);
        if (links.get(edge.getSource()).get(edge.getTarget()).isEmpty()) {
            links.get(edge.getSource()).remove(edge.getTarget());
        }
        listeners.stream().forEach(listener -> {
            listener.edgeRemoved(edge);
        });
    }

    @Override
    public Set<INode> getAllReachableNodes(INode node) {
        if (!all_reachable_nodes.containsKey(node)) {
            Set<INode> c_nodes = new HashSet<>(links.size());
            Set<INode> next_nodes = new HashSet<>();
            next_nodes.add(node);
            while (!next_nodes.isEmpty()) {
                INode c_node = next_nodes.iterator().next();
                next_nodes.remove(c_node);
                if (c_nodes.add(c_node)) {
                    next_nodes.addAll(links.get(c_node).keySet());
                }
            }
            all_reachable_nodes.put(node, c_nodes);
        }
        return all_reachable_nodes.get(node);
    }

    @Override
    public Set<INode> getAllMinReachableNodes(INode node) {
        if (!all_min_reachable_nodes.containsKey(node)) {
            all_min_reachable_nodes.put(node, getAllMinReachableNodes(node, new HashSet<>(nodes.size())));
        }
        return all_min_reachable_nodes.get(node);
    }

    private Set<INode> getAllMinReachableNodes(INode node, Set<INode> visited) {
        if (!all_min_reachable_nodes.containsKey(node)) {
            all_min_reachable_nodes.put(node, getMinReachableNodes(node, new HashSet<>(nodes.size())));
        }
        if (!visited.contains(node)) {
            if (node instanceof IPredicateNode) {
                visited.add(node);
            }
            if (node instanceof IDisjunctionNode) {
                int min_nodes_size = Integer.MAX_VALUE;
                Set<INode> min_nodes = null;
                for (IDisjunct disjunct : ((IDisjunctionNode) node).getDisjunction().getDisjuncts()) {
                    Set<INode> c_nodes = getAllMinReachableNodes(getNode(disjunct), new HashSet<>(visited));
                    if (c_nodes.size() < min_nodes_size) {
                        min_nodes_size = c_nodes.size();
                        min_nodes = c_nodes;
                    }
                }
                visited.addAll(min_nodes);
            } else if (!(node instanceof INoOpNode)) {
                visited.addAll(node.getExitingEdges().stream().filter(edge -> edge.getType() == IEdge.Type.Goal).flatMap(edge -> getAllMinReachableNodes(edge.getTarget(), visited).stream()).collect(Collectors.toSet()));
            }
            return visited;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<INode> getMinReachableNodes(INode node) {
        if (!min_reachable_nodes.containsKey(node)) {
            min_reachable_nodes.put(node, getMinReachableNodes(node, new HashSet<>(nodes.size())));
        }
        return min_reachable_nodes.get(node);
    }

    private Set<INode> getMinReachableNodes(INode node, Set<INode> visited) {
        if (min_reachable_nodes.containsKey(node)) {
            return min_reachable_nodes.get(node);
        }
        if (!visited.contains(node)) {
            if (node instanceof IPredicateNode) {
                if (((IPredicateNode) node).getPredicate().getInstances().stream().map(instance -> (IFormula) instance).noneMatch(formula -> formula.getFormulaState() == FormulaState.Active)) {
                    visited.add(node);
                } else {
                    return visited;
                }
            }
            if (node instanceof IDisjunctionNode) {
                int min_nodes_size = Integer.MAX_VALUE;
                Set<INode> min_nodes = null;
                for (IDisjunct disjunct : ((IDisjunctionNode) node).getDisjunction().getDisjuncts()) {
                    Set<INode> c_nodes = getMinReachableNodes(getNode(disjunct), new HashSet<>(visited));
                    if (c_nodes.size() < min_nodes_size) {
                        min_nodes_size = c_nodes.size();
                        min_nodes = c_nodes;
                    }
                }
                visited.addAll(min_nodes);
            } else if (!(node instanceof INoOpNode)) {
                visited.addAll(node.getExitingEdges().stream().filter(edge -> edge.getType() == IEdge.Type.Goal).flatMap(edge -> getMinReachableNodes(edge.getTarget(), visited).stream()).collect(Collectors.toSet()));
            }
            return visited;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public int getMinCausalDistance(INode node) {
        if (!min_causal_distance.containsKey(node)) {
            min_causal_distance.put(node, getMinCausalDistance(node, new HashSet<>(nodes.size())));
        }
        return min_causal_distance.get(node);
    }

    private int getMinCausalDistance(INode node, Set<INode> visited) {
        if (min_causal_distance.containsKey(node)) {
            return min_causal_distance.get(node);
        }
        if (!visited.contains(node)) {
            if (node instanceof IPredicateNode) {
                if (((IPredicateNode) node).getPredicate().getInstances().stream().map(instance -> (IFormula) instance).noneMatch(formula -> formula.getFormulaState() == FormulaState.Active)) {
                    visited.add(node);
                } else {
                    return 0;
                }
            }
            if (node instanceof IDisjunctionNode) {
                int min_distance = Integer.MAX_VALUE;
                for (IDisjunct disjunct : ((IDisjunctionNode) node).getDisjunction().getDisjuncts()) {
                    int c_distance = getMinCausalDistance(getNode(disjunct), new HashSet<>(visited));
                    if (c_distance < min_distance) {
                        min_distance = c_distance;
                    }
                }
                return min_distance;
            } else if (!(node instanceof INoOpNode)) {
                OptionalInt max = node.getExitingEdges().stream().filter(edge -> edge.getType() == IEdge.Type.Goal).mapToInt(edge -> getMinCausalDistance(edge.getTarget(), visited)).max();
                return 1 + (max.isPresent() ? max.getAsInt() : 0);
            }
            return 0;
        } else {
            return 0;
        }
    }

    @Override
    public void recomputeCosts() {
        all_reachable_nodes.clear();
        all_min_reachable_nodes.clear();
        min_reachable_nodes.clear();
        min_causal_distance.clear();
        links.keySet().stream().forEach(node -> {
            estimateCost(node);
        });
    }

    @Override
    public void addCausalGraphListener(IStaticCausalGraphListener listener) {
        listeners.add(listener);
        types.stream().forEach(type -> {
            listener.typeAdded(type);
        });
        links.keySet().stream().forEach(node -> {
            listener.nodeAdded(node);
        });
        links.keySet().stream().forEach(source -> {
            links.get(source).keySet().stream().forEach(target -> {
                links.get(source).get(target).stream().forEach(edge -> {
                    listener.edgeAdded(edge);
                });
            });
        });
    }

    @Override
    public void removeCausalGraphListener(IStaticCausalGraphListener listener) {
        listeners.remove(listener);
    }

    //<editor-fold defaultstate="collapsed" desc="Nodes and edges..">
    private abstract class Node implements INode {

        @Override
        public Collection<IEdge> getExitingEdges() {
            return links.get(this).values().stream().flatMap(c_edges -> c_edges.stream()).collect(Collectors.toList());
        }
    }

    private class PredicateNode extends Node implements IPredicateNode {

        private final IPredicate predicate;

        PredicateNode(IPredicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public IPredicate getPredicate() {
            return predicate;
        }

        @Override
        public String toString() {
            return predicate.toString();
        }
    }

    private class DisjunctionNode extends Node implements IDisjunctionNode {

        private final IDisjunction disjunction;

        DisjunctionNode(IDisjunction disjunction) {
            this.disjunction = disjunction;
        }

        @Override
        public IDisjunction getDisjunction() {
            return disjunction;
        }

        @Override
        public String toString() {
            return disjunction.toString();
        }
    }

    private class DisjunctNode extends Node implements IDisjunctNode {

        private final IDisjunct disjunct;

        DisjunctNode(IDisjunct disjunct) {
            this.disjunct = disjunct;
        }

        @Override
        public IDisjunct getDisjunct() {
            return disjunct;
        }

        @Override
        public String toString() {
            return disjunct.toString();
        }
    }

    private class PreferenceNode extends Node implements IPreferenceNode {

        private final IPreference preference;

        PreferenceNode(IPreference preference) {
            this.preference = preference;
        }

        @Override
        public IPreference getPreference() {
            return preference;
        }

        @Override
        public String toString() {
            return preference.toString();
        }
    }

    private class NoOpNode extends Node implements INoOpNode {
    }

    private class Edge implements IEdge {

        private final Type type;
        private final INode source;
        private final INode target;

        Edge(Type type, INode source, INode target) {
            this.type = type;
            this.source = source;
            this.target = target;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public INode getSource() {
            return source;
        }

        @Override
        public INode getTarget() {
            return target;
        }
    }
    //</editor-fold>
}
