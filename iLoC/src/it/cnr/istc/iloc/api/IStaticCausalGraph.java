/*
 * Copyright (C) 2015 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package it.cnr.istc.iloc.api;

import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public interface IStaticCausalGraph {

    /**
     * Adds a type to the current static causal graph.
     *
     * @param type the type to be added to the current static causal graph.
     */
    public void addType(IType type);

    /**
     * Removes a type to the current static causal graph.
     *
     * @param type the type to be removed from the current static causal graph.
     */
    public void removeType(IType type);

    /**
     * Returns all the types currently registered on the current static causal
     * graph.
     *
     * @return a collection of types currently registered on the current static
     * causal graph.
     */
    public Collection<IType> getTypes();

    /**
     * Creates a node associated to the given predicate and adds it to the
     * current static causal graph.
     *
     * @param predicate the predicate associated to the new node.
     * @return the new node associated to the given predicate.
     */
    public IPredicateNode addNode(IPredicate predicate);

    /**
     * Retrieves the node associated to the given predicate.
     *
     * @param predicate the predicate associated to the node to be retrieved.
     * @return the node associated to the given predicate.
     */
    public IPredicateNode getNode(IPredicate predicate);

    /**
     * Creates a node associated to the given disjunction and adds it to the
     * current static causal graph.
     *
     * @param disjunction the disjunction associated to the new node.
     * @return the new node associated to the given disjunction.
     */
    public IDisjunctionNode addNode(IDisjunction disjunction);

    /**
     * Retrieves the node associated to the given disjunction.
     *
     * @param disjunction the disjunction associated to the node to be
     * retrieved.
     * @return the node associated to the given disjunction.
     */
    public IDisjunctionNode getNode(IDisjunction disjunction);

    /**
     * Creates a node associated to the given disjunct and adds it to the
     * current static causal graph.
     *
     * @param disjunct the disjunct associated to the new node.
     * @return the new node associated to the given disjunct.
     */
    public IDisjunctNode addNode(IDisjunct disjunct);

    /**
     * Retrieves the node associated to the given disjunct.
     *
     * @param disjunct the disjunct associated to the node to be retrieved.
     * @return the node associated to the given disjunct.
     */
    public IDisjunctNode getNode(IDisjunct disjunct);

    /**
     * Creates a node associated to the given preference and adds it to the
     * current static causal graph.
     *
     * @param preference the preference associated to the new node.
     * @return the new node associated to the given preference.
     */
    public IPreferenceNode addNode(IPreference preference);

    /**
     * Retrieves the node associated to the given preference.
     *
     * @param preference the preference associated to the node to be retrieved.
     * @return the node associated to the given preference.
     */
    public IPreferenceNode getNode(IPreference preference);

    /**
     * Creates a node associated to no-op and adds it to the current static
     * causal graph.
     *
     * @return the new node associated to the no-op.
     */
    public INoOpNode addNoOp();

    /**
     * Removes a node from the current static causal graph.
     *
     * @param node the node to be removed from the current static causal.
     */
    public void removeNode(INode node);

    /**
     * Returns all the nodes currently registered on the current static causal
     * graph.
     *
     * @return a collection of nodes currently registered on the current static
     * causal graph.
     */
    public Collection<INode> getNodes();

    /**
     * Adds an edge between two nodes in this static causal graph.
     *
     * @param type the type of the edge
     * @param source the source of the edge.
     * @param target the target of the edge.
     * @return the new edge between the given nodes.
     */
    public IEdge addEdge(IEdge.Type type, INode source, INode target);

    /**
     * Removes an edge from this static causal graph.
     *
     * @param edge the edge to be removed from this static causal graph.
     */
    public void removeEdge(IEdge edge);

    /**
     * Returns all the edges currently registered on the current static causal
     * graph.
     *
     * @return a collection of edges currently registered on the current static
     * causal graph.
     */
    public Collection<IEdge> getEdges();

    /**
     * Returns all the nodes that are reachable from the given node.
     *
     * @param node the node from which all the reachable nodes are computed.
     * @return all the nodes that are reachable from the given node.
     */
    public Set<INode> getAllReachableNodes(INode node);

    /**
     * Returns {@code true} if and only if there exists a causal path between
     * the node {@code source} and the node {@code target}.
     *
     * @param source the initial node of the path.
     * @param target the final node of the path.
     * @return {@code true} if and only if there exists a causal path between
     * the node {@code source} and the node {@code target}.
     */
    public default boolean existsPath(INode source, INode target) {
        return getAllReachableNodes(source).contains(target);
    }

    /**
     * Adds a listener to the given static causal graph for detecting
     * addition/removal of nodes and edges.
     *
     * @param listener a static causal graph listener.
     */
    public void addCausalGraphListener(IStaticCausalGraphListener listener);

    /**
     *
     * Removes a listener from the given static causal graph.
     *
     * @param listener a static causal graph listener.
     */
    public void removeCausalGraphListener(IStaticCausalGraphListener listener);

    public interface INode {

        /**
         * Returns a collection containing all the edges incoming into this
         * node.
         *
         * @return a collection containing all the edges incoming into this
         * node.
         */
        public Collection<IEdge> getIncomingEdges();

        /**
         * Returns a collection containing all the edges outgoing from this
         * node.
         *
         * @return a collection containing all the edges outgoing from this
         * node.
         */
        public Collection<IEdge> getOutgoingEdges();
    }

    public interface IEdge {

        public Type getType();

        /**
         * Returns the source of this edge.
         *
         * @return the source of this edge.
         */
        public INode getSource();

        /**
         * Returns the target of this edge.
         *
         * @return the target of this edge.
         */
        public INode getTarget();

        public enum Type {
            Goal, Fact
        }
    }

    public interface IPredicateNode extends INode {

        /**
         * Returns the predicate associated to this node.
         *
         * @return the predicate associated to this node.
         */
        public IPredicate getPredicate();
    }

    public interface IDisjunctionNode extends INode {

        /**
         * Returns the disjunction associated to this node.
         *
         * @return the disjunction associated to this node.
         */
        public IDisjunction getDisjunction();
    }

    public interface IDisjunctNode extends INode {

        /**
         * Returns the disjunct associated to this node.
         *
         * @return the disjunct associated to this node.
         */
        public IDisjunct getDisjunct();
    }

    public interface IPreferenceNode extends INode {

        /**
         * Returns the preference associated to this node.
         *
         * @return the preference associated to this node.
         */
        public IPreference getPreference();
    }

    public interface INoOpNode extends INode {

        @Override
        public default Collection<IEdge> getOutgoingEdges() {
            throw new AssertionError("No-op nodes do not have exiting edges..");
        }
    }
}
