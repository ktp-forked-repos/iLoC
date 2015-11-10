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
package it.cnr.istc.iloc.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Dijkstra<V> {

    private final Map<V, Vertex> graph = new LinkedHashMap<>();

    public void addVertex(V v, double init_dist) {
        if (!graph.containsKey(v)) {
            graph.put(v, new Vertex(v, init_dist));
        }
    }

    public boolean containsVertex(V v) {
        return graph.containsKey(v);
    }

    public double getDistance(V v) {
        return graph.get(v).dist;
    }

    public void addEdge(V v1, V v2, double dist) {
        if (!graph.containsKey(v1)) {
            graph.put(v1, new Vertex(v1, Double.POSITIVE_INFINITY));
        }
        if (!graph.containsKey(v2)) {
            graph.put(v2, new Vertex(v2, Double.POSITIVE_INFINITY));
        }
        graph.get(v1).neighbours.put(graph.get(v2), dist);
    }

    public void dijkstra(V start) {
        if (!graph.containsKey(start)) {
            throw new AssertionError("Graph does not contain start vertex" + start);
        }
        Queue<Vertex> q = new PriorityQueue<Vertex>();

        graph.values().forEach(v -> {
            q.add(v);
        });

        dijkstra(q);
    }

    private void dijkstra(final Queue<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {
            // vertex with shortest distance (first iteration will return source)
            u = q.poll();
            if (u.dist == Double.POSITIVE_INFINITY) {
                // we can ignore u (and any other remaining vertices) since they are unreachable
                break;
            }
            //look at distances to each neighbour
            for (Map.Entry<Vertex, Double> a : u.neighbours.entrySet()) {
                //the neighbour in this iteration
                v = a.getKey();

                final double alternateDist = u.dist + a.getValue();
                // shorter path to neighbour found
                if (alternateDist < v.dist) {
                    q.remove(v);
                    v.dist = alternateDist;
                    q.add(v);
                }
            }
        }
    }

    private class Vertex implements Comparable<Vertex> {

        private final V object;
        private double dist;
        private final Map<Vertex, Double> neighbours = new HashMap<>();

        Vertex(V object, double dist) {
            this.object = object;
            this.dist = dist;
        }

        @Override
        public int compareTo(Vertex t) {
            return Double.compare(dist, t.dist);
        }

        @Override
        public String toString() {
            return object.toString() + " " + dist;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        graph.keySet().stream().forEach(v -> {
            sb.append(v).append(" ").append(graph.get(v).dist).append("\n");
            graph.get(v).neighbours.forEach((t, c) -> {
                sb.append("  -> ").append(t.object).append(" ").append(c).append("\n");
            });
        });
        return sb.toString();
    }
}
