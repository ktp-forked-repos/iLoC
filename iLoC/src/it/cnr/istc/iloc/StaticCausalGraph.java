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
    private final Map<INode, Map<INode, Collection<IEdge>>> links = new IdentityHashMap<>();
    private final Map<INode, Set<INode>> all_reachable_nodes = new HashMap<>();
    private final Map<INode, Set<IEdge>> all_walkable_edges = new HashMap<>();
    private final Map<INode, Set<INode>> min_reachable_nodes = new HashMap<>();
    private final Map<INode, Set<IEdge>> min_walkable_edges = new HashMap<>();
    private final Map<INode, Double> min_walkable_edges_weight = new HashMap<>();
    private final Collection<IStaticCausalGraphListener> listeners = new ArrayList<>();

    @Override
    public void addType(IType type) {
        assert !types.contains(type);
        types.add(type);
        clear();
        listeners.stream().forEach(listener -> {
            listener.typeAdded(type);
        });
    }

    @Override
    public void removeType(IType type) {
        assert types.contains(type);
        types.remove(type);
        clear();
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
        clear();
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
        clear();
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
        clear();
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
        clear();
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
        clear();
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
        clear();
    }

    @Override
    public IEdge addEdge(IEdge.Type type, INode source, INode target, double weight) {
        Edge edge = new Edge(type, source, target, weight);
        if (!links.get(source).containsKey(target)) {
            links.get(source).put(target, new ArrayList<>());
        }
        links.get(source).get(target).add(edge);
        clear();
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
        clear();
        listeners.stream().forEach(listener -> {
            listener.edgeRemoved(edge);
        });
    }

    @Override
    public boolean existsPath(INode source, INode target) {
        return source.getAllReachableNodes().contains(target);
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

    private void clear() {
        all_reachable_nodes.clear();
        all_walkable_edges.clear();
        min_reachable_nodes.clear();
        min_walkable_edges.clear();
        min_walkable_edges_weight.clear();
    }

    private abstract class Node implements INode {

        @Override
        public Collection<IEdge> getExitingEdges() {
            return links.get(this).values().stream().flatMap(c_edges -> c_edges.stream()).collect(Collectors.toList());
        }

        @Override
        public Set<INode> getAllReachableNodes() {
            if (!all_reachable_nodes.containsKey(this)) {
                Set<INode> c_nodes = new HashSet<>(links.size());
                Set<INode> next_nodes = new HashSet<>();
                next_nodes.add(this);
                while (!next_nodes.isEmpty()) {
                    INode node = next_nodes.iterator().next();
                    next_nodes.remove(node);
                    if (c_nodes.add(node)) {
                        next_nodes.addAll(links.get(node).keySet());
                    }
                }
                all_reachable_nodes.put(this, c_nodes);
            }
            return all_reachable_nodes.get(this);
        }

        @Override
        public Set<IEdge> getAllWalkableEdges() {
            if (!all_walkable_edges.containsKey(this)) {
                Set<IEdge> c_min_walkable_edges = new HashSet<>();
                Set<INode> minReachableNodes = getAllReachableNodes();
                links.keySet().stream().forEach(source -> {
                    links.get(source).keySet().stream().forEach(target -> {
                        links.get(source).get(target).stream().filter(edge -> (minReachableNodes.contains(edge.getSource()) && minReachableNodes.contains(edge.getTarget()))).forEach(edge -> {
                            c_min_walkable_edges.add(edge);
                        });
                    });
                });
                all_walkable_edges.put(this, c_min_walkable_edges);
            }
            return all_walkable_edges.get(this);
        }

        @Override
        public Set<INode> getMinReachableNodes() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set<IEdge> getMinWalkableEdges() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getMinWalkableEdgesWeight() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class Edge implements IEdge {

        private final IEdge.Type type;
        private final INode source;
        private final INode target;
        private final double weight;

        Edge(IEdge.Type type, INode source, INode target, double weight) {
            this.type = type;
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        @Override
        public IEdge.Type getType() {
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

        @Override
        public double getWeight() {
            return weight;
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
    }

    private class NoOpNode extends Node implements INoOpNode {
    }
}
