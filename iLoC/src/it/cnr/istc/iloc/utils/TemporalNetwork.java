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
 * An incremental full path consistency algorithm. Propagation performs IFPC
 * propagation in O(n^2) as described in "Incrementally Solving the STP by
 * Enforcing Partial Path Consistency" by Léon Planken.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class TemporalNetwork implements Cloneable {

    private int n_vars = 2;
    private double[][] dist;
    private final Collection<DistanceConstraint> constraints = new ArrayList<>();

    /**
     * Creates a new temporal network.
     */
    public TemporalNetwork() {
        this(Double.POSITIVE_INFINITY);
    }

    /**
     * Creates a new temporal network having the given horizon.
     *
     * @param horizon a {@code double} representing the horizon of the new
     * temporal network.
     */
    public TemporalNetwork(double horizon) {
        this(10, horizon);
    }

    /**
     * Creates a new temporal network having the given initial capacity and the
     * given horizon.
     *
     * @param initialCapacity an {@code int} representing the initial capacity
     * (i.e., the expected number of time-points).
     * @param horizon a {@code double} representing the horizon of the new
     * temporal network.
     */
    public TemporalNetwork(int initialCapacity, double horizon) {
        assert initialCapacity >= 2;
        dist = new double[initialCapacity][initialCapacity];
        for (int i = 0; i < dist.length; i++) {
            Arrays.fill(dist[i], horizon);
            dist[i][i] = 0;
        }
        constraints.add(new DistanceConstraint(0, 1, 0, horizon));
    }

    /**
     * Creates a new time-point and returns an integer for identifying it.
     *
     * @return an {@code int} representing the newly created time-point.
     */
    public int newTimePoint() {
        ensureCapacity(n_vars + 1);
        addConstraints(new DistanceConstraint(0, n_vars, 0, Double.POSITIVE_INFINITY), new DistanceConstraint(n_vars, 1, 0, Double.POSITIVE_INFINITY));
        return n_vars++;
    }

    /**
     * Adds a temporal constraint to this temporal network without performing
     * propagation.
     *
     * @param from the source of the temporal constraint.
     * @param to the target of the temporal constraint.
     * @param min the minimum distance of the temporal constraint.
     * @param max the maximum distance of the temporal constraint.
     */
    public void addConstraint(int from, int to, double min, double max) {
        this.constraints.add(new DistanceConstraint(from, to, min, max));
    }

    /**
     * Adds an array of temporal constraints to this temporal network without
     * performing propagation.
     *
     * @param constraints an array of temporal constraints.
     */
    public void addConstraints(DistanceConstraint... constraints) {
        this.constraints.addAll(Arrays.asList(constraints));
    }

    /**
     * Checks whether this temporal network requires propagation. A temporal
     * network requires propagation if there is some constraint which has not
     * yet propagated.
     *
     * @return
     */
    public boolean requiresPropagation() {
        return !constraints.isEmpty();
    }

    /**
     * Performs propagation on this temporal network and returns {@code true} if
     * the propagated temporal network is consistent.
     *
     * @return {@code true} if the propagated temporal network is consistent and
     * {@code false} otherwise.
     */
    public boolean propagate() {
        if (constraints.size() >= n_vars * 5) {
            /**
             * Performs O(n^3) propagation through Floyd-Warshall algorithm
             */
            constraints.stream().forEach(constraint -> {
                dist[constraint.from][constraint.to] = Math.min(dist[constraint.from][constraint.to], constraint.max);
                dist[constraint.to][constraint.from] = Math.min(dist[constraint.to][constraint.from], -constraint.min);
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
            for (DistanceConstraint constraint : constraints) {
                if (dist[constraint.to][constraint.from] < -constraint.max || dist[constraint.from][constraint.to] < constraint.min) {
                    return false;
                }
                if (dist[constraint.from][constraint.to] > constraint.max) {
                    propagate(constraint.from, constraint.to, constraint.max);
                }
                if (dist[constraint.to][constraint.from] > -constraint.min) {
                    propagate(constraint.to, constraint.from, -constraint.min);
                }
            }
        }
        constraints.clear();
        return true;
    }

    private void setWeight(final int from, final int to, final double weight) {
        dist[from][to] = weight;
    }

    /**
     * Performs IFPC propagation in O(n^2) as described in "Incrementally
     * Solving the STP by Enforcing Partial Path Consistency" by LÃ©on Planken.
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

    /**
     * Returns a bound representing the distance between the given time-points.
     *
     * @param from the source of the required distance.
     * @param to the target of the required distance.
     * @return a bound representing the distance between the given time-points.
     */
    public Bound distance(int from, int to) {
        return new Bound(-dist[to][from], dist[from][to]);
    }

    /**
     * Returns a bound representing the distance between the origin and the
     * given time-point.
     *
     * @param tp the target of the required distance.
     * @return a bound representing the distance between the origin and the
     * given time-point.
     */
    public Bound bound(int tp) {
        return new Bound(-dist[tp][0], dist[0][tp]);
    }

    /**
     * Returns the minimum allowed value of the given time-point. The returned
     * value is the same as if calling {@link #bound(int)} on the same
     * time-point {@code tp} and getting the lower bound.
     *
     * @param tp the time-point whose minimum is required.
     * @return the minimum allowed value of the given time-point.
     */
    public double min(int tp) {
        return -dist[tp][0];
    }

    /**
     * Returns the maximum allowed value of the given time-point. The returned
     * value is the same as if calling {@link #bound(int)} on the same
     * time-point {@code tp} and getting the upper bound.
     *
     * @param tp the time-point whose maximum is required.
     * @return the maximum allowed value of the given time-point.
     */
    public double max(int tp) {
        return dist[0][tp];
    }

    @Override
    public TemporalNetwork clone() throws CloneNotSupportedException {
        TemporalNetwork temporalNetwork = (TemporalNetwork) super.clone();
        temporalNetwork.dist = new double[dist.length][dist.length];
        for (int i = 0; i < dist.length; i++) {
            System.arraycopy(dist[i], 0, temporalNetwork.dist[i], 0, dist[i].length);
        }
        temporalNetwork.n_vars = n_vars;
        return temporalNetwork;
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

    public static class DistanceConstraint {

        public final int from, to;
        public final double min, max;

        public DistanceConstraint(int from, int to, double min, double max) {
            this.from = from;
            this.to = to;
            this.min = min;
            this.max = max;
        }
    }

    public static class Bound {

        public final double lb, ub;

        private Bound(double lb, double ub) {
            this.lb = lb;
            this.ub = ub;
        }
    }
}
