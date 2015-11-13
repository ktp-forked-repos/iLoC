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

import it.cnr.istc.iloc.api.IDisjunct;
import it.cnr.istc.iloc.api.IDisjunction;
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
    private final Map<INode, Map<INode, Collection<IEdge>>> incoming_edges = new IdentityHashMap<>();
    private final Map<INode, Map<INode, Collection<IEdge>>> outgoing_edges = new IdentityHashMap<>();
    private final Map<INode, Set<INode>> all_reachable_nodes = new HashMap<>();
    private final Collection<IStaticCausalGraphListener> listeners = new ArrayList<>();

    @Override
    public void addType(IType type) {
        assert !types.contains(type);
        types.add(type);
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
            listener.typeAdded(type);
        });
    }

    @Override
    public void removeType(IType type) {
        assert types.contains(type);
        types.remove(type);
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
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
        incoming_edges.put(node, new HashMap<>());
        outgoing_edges.put(node, new HashMap<>());
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
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
        incoming_edges.put(node, new HashMap<>());
        outgoing_edges.put(node, new HashMap<>());
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
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
        incoming_edges.put(node, new HashMap<>());
        outgoing_edges.put(node, new HashMap<>());
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
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
        incoming_edges.put(node, new HashMap<>());
        outgoing_edges.put(node, new HashMap<>());
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
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
        outgoing_edges.put(node, new HashMap<>());
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
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
        incoming_edges.remove(node);
        outgoing_edges.remove(node);
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
            listener.nodeAdded(node);
        });
    }

    @Override
    public Collection<INode> getNodes() {
        return outgoing_edges.keySet();
    }

    @Override
    public IEdge addEdge(IEdge.Type type, INode source, INode target) {
        Edge edge = new Edge(type, source, target);
        if (!incoming_edges.get(target).containsKey(source)) {
            incoming_edges.get(target).put(source, new ArrayList<>());
        }
        incoming_edges.get(target).get(source).add(edge);
        if (!outgoing_edges.get(source).containsKey(target)) {
            outgoing_edges.get(source).put(target, new ArrayList<>());
        }
        outgoing_edges.get(source).get(target).add(edge);
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
            listener.edgeAdded(edge);
        });
        return edge;
    }

    @Override
    public void removeEdge(IEdge edge) {
        incoming_edges.get(edge.getTarget()).get(edge.getSource()).remove(edge);
        if (incoming_edges.get(edge.getTarget()).get(edge.getSource()).isEmpty()) {
            incoming_edges.get(edge.getTarget()).remove(edge.getSource());
        }
        outgoing_edges.get(edge.getSource()).get(edge.getTarget()).remove(edge);
        if (outgoing_edges.get(edge.getSource()).get(edge.getTarget()).isEmpty()) {
            outgoing_edges.get(edge.getSource()).remove(edge.getTarget());
        }
        all_reachable_nodes.clear();
        listeners.forEach(listener -> {
            listener.edgeRemoved(edge);
        });
    }

    @Override
    public Collection<IEdge> getEdges() {
        return outgoing_edges.values().stream().flatMap(t -> t.values().stream().flatMap(edges -> edges.stream())).collect(Collectors.toList());
    }

    @Override
    public Set<INode> getAllReachableNodes(INode node) {
        if (!all_reachable_nodes.containsKey(node)) {
            Set<INode> c_nodes = new HashSet<>(outgoing_edges.size());
            Set<INode> next_nodes = new HashSet<>();
            next_nodes.add(node);
            while (!next_nodes.isEmpty()) {
                INode c_node = next_nodes.iterator().next();
                next_nodes.remove(c_node);
                if (c_nodes.add(c_node)) {
                    next_nodes.addAll(outgoing_edges.get(c_node).keySet());
                }
            }
            all_reachable_nodes.put(node, c_nodes);
        }
        return all_reachable_nodes.get(node);
    }

    @Override
    public void addCausalGraphListener(IStaticCausalGraphListener listener) {
        listeners.add(listener);
        types.forEach(type -> {
            listener.typeAdded(type);
        });
        outgoing_edges.keySet().forEach(node -> {
            listener.nodeAdded(node);
        });
        outgoing_edges.keySet().forEach(source -> {
            outgoing_edges.get(source).keySet().forEach(target -> {
                outgoing_edges.get(source).get(target).forEach(edge -> {
                    listener.edgeAdded(edge);
                });
            });
        });
    }

    @Override
    public void removeCausalGraphListener(IStaticCausalGraphListener listener) {
        listeners.remove(listener);
    }

    //<editor-fold defaultstate="collapsed" desc="Nodes and edges.">
    private abstract class Node implements INode {

        @Override
        public Collection<IEdge> getIncomingEdges() {
            return incoming_edges.get(this).values().stream().flatMap(c_edges -> c_edges.stream()).collect(Collectors.toList());
        }

        @Override
        public Collection<IEdge> getOutgoingEdges() {
            return outgoing_edges.get(this).values().stream().flatMap(c_edges -> c_edges.stream()).collect(Collectors.toList());
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
