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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class TemporalNetwork {

    private int n_vars = 2;
    private double[][] dist;
    private final Collection<DistanceConstraint> constraints = new ArrayList<>();
    private final Collection<TemporalNetworkListener> listeners = new ArrayList<>();

    public TemporalNetwork(double horizon) {
        this(10, horizon);
    }

    public TemporalNetwork(int initialCapacity, double horizon) {
        assert initialCapacity >= 2;
        dist = new double[initialCapacity][initialCapacity];
        for (int i = 0; i < dist.length; i++) {
            Arrays.fill(dist[i], horizon);
            dist[i][i] = 0;
        }
        constraints.add(new DistanceConstraint(0, 1, 0, horizon));
    }

    private TemporalNetwork(double[][] dist, int n_vars) {
        this.dist = new double[dist.length][dist.length];
        for (int i = 0; i < dist.length; i++) {
            System.arraycopy(dist[i], 0, this.dist[i], 0, dist[i].length);
        }
        this.n_vars = n_vars;
    }

    public int newTimePoint() {
        ensureCapacity(n_vars + 1);
        addConstraints(new DistanceConstraint(0, n_vars, 0, Double.POSITIVE_INFINITY), new DistanceConstraint(n_vars, 1, 0, Double.POSITIVE_INFINITY));
        int tp = n_vars++;
        listeners.stream().forEach(listener -> {
            listener.timePointAdded(tp);
        });
        return tp;
    }

    public void addConstraints(DistanceConstraint... constraints) {
        this.constraints.addAll(Arrays.asList(constraints));
    }

    public boolean propagate() {
        if (constraints.size() >= n_vars * 5) {
            /**
             * Performs O(n^3) propagation through Floyd-Warshall algorithm
             */
            constraints.stream().forEach(c -> {
                dist[c.from][c.to] = Math.min(dist[c.from][c.to], c.max);
                dist[c.to][c.from] = Math.min(dist[c.to][c.from], -c.min);
            });
            for (int k = 0; k < n_vars; k++) {
                for (int i = 0; i < n_vars; i++) {
                    for (int j = 0; j < n_vars; j++) {
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            setWeight(i, j, dist[i][k] + dist[k][j]);
                            if (i == j && dist[i][j] < 0) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            for (DistanceConstraint c : constraints) {
                if (dist[c.to][c.from] < -c.max || dist[c.from][c.to] < c.min) {
                    return false;
                }
                if (dist[c.from][c.to] > c.max) {
                    propagate(c.from, c.to, c.max);
                }
                if (dist[c.to][c.from] > -c.min) {
                    propagate(c.to, c.from, -c.min);
                }
            }
        }
        constraints.clear();
        return true;
    }

    /**
     * Performs IFPC propagation in O(n^2) as described in "Incrementally
     * Solving the STP by Enforcing Partial Path Consistency" by Léon Planken.
     */
    private void propagate(final int a, final int b, final double w_ab) {
        // they didn't, so we must propagate.
        setWeight(a, b, w_ab);

        final int[] set_i = new int[n_vars];
        final int[] set_j = new int[n_vars];
        int size_i = 0, size_j = 0;

        // start with an O(n) loop
        for (int k = 0; k < n_vars; ++k) {
            if (dist[k][a] < dist[k][b] - w_ab) {
                // u -> from -> to is shorter than u -> to
                setWeight(k, b, dist[k][a] + w_ab);
                set_i[size_i++] = k;
            }
            if (dist[b][k] < dist[a][k] - w_ab) {
                // from -> to -> u is shorter than from -> u
                setWeight(a, k, w_ab + dist[b][k]);
                set_j[size_j++] = k;
            }
        }

        // finally, loop over set_i and set_j in O(n^2) time (but possibly much less)
        for (int i = 0; i < size_i; ++i) {
            for (int j = 0; j < size_j; ++j) {
                if (set_i[i] != set_j[j]) {
                    if (dist[set_i[i]][b] + dist[b][set_j[j]] < dist[set_i[i]][set_j[j]]) {
                        // set_i[i] -> from -> to -> set_j[j] is shorter than set_i[i] -> set_j[j]
                        setWeight(set_i[i], set_j[j], dist[set_i[i]][b] + dist[b][set_j[j]]);
                    }
                }
            }
        }
    }

    private void ensureCapacity(int minCapacity) {
        int capacity = dist.length;
        while (minCapacity > capacity) {
            capacity = (capacity * 3) / 2 + 1;
            if (minCapacity < capacity) {
                double[][] tp_dist = new double[capacity][capacity];
                for (int i = 0; i < dist.length; i++) {
                    System.arraycopy(dist[i], 0, tp_dist[i], 0, dist[i].length);
                    Arrays.fill(tp_dist[i], dist.length, tp_dist.length, Double.POSITIVE_INFINITY);
                }
                for (int i = dist.length; i < tp_dist.length; i++) {
                    Arrays.fill(tp_dist[i], Double.POSITIVE_INFINITY);
                    tp_dist[i][i] = 0;
                }
                dist = tp_dist;
            }
        }
    }

    private void setWeight(final int from, final int to, final double weight) {
        dist[from][to] = weight;
        if (from == 0) {
            listeners.stream().forEach(listener -> {
                listener.timePointChange(to, -dist[to][from], dist[from][to]);
            });
        } else if (to == 0) {
            listeners.stream().forEach(listener -> {
                listener.timePointChange(from, -dist[from][to], dist[to][from]);
            });
        } else {
            listeners.stream().forEach(listener -> {
                listener.distanceChange(from, to, -dist[to][from], dist[from][to]);
            });
        }
    }

    public Bound distance(int from, int to) {
        return new Bound(-dist[to][from], dist[from][to]);
    }

    public Bound bound(int tp) {
        return new Bound(-dist[tp][0], dist[0][tp]);
    }

    public double min(int tp) {
        return -dist[tp][0];
    }

    public double max(int tp) {
        return dist[0][tp];
    }

    @Override
    public TemporalNetwork clone() {
        return new TemporalNetwork(dist, n_vars);
    }

    public void addTemporalNetworkListener(TemporalNetworkListener listener) {
        listeners.add(listener);
    }

    public void removeTemporalNetworkListener(TemporalNetworkListener listener) {
        listeners.remove(listener);
    }

    @Override
    public String toString() {
        StringBuilder network = new StringBuilder();
        network.append("*****************************************************************************\n");
        for (int tp = 0; tp < n_vars; tp++) {
            network.append("tp").append(tp).append(" = ").append(domainString(tp)).append('\n');
        }
        network.append("*****************************************************************************\n");
        return network.toString();
    }

    private String domainString(int tp) {
        if (-dist[tp][0] == dist[0][tp]) {
            if (-dist[tp][0] == Double.NEGATIVE_INFINITY) {
                return "-inf";
            }
            if (-dist[tp][0] == Double.POSITIVE_INFINITY) {
                return "+inf";
            }
            return String.valueOf(-dist[tp][0]);
        }
        return "[" + (-dist[tp][0] == Double.NEGATIVE_INFINITY ? "-inf" : -dist[tp][0]) + ", " + (dist[0][tp] == Double.POSITIVE_INFINITY ? "+inf" : dist[0][tp]) + "]";
    }
}
