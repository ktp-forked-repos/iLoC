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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class TemporalNetwork {

    private final APSPNetwork network;
    private final int origin, horizon;

    public TemporalNetwork() {
        this.network = new APSPNetwork();
        this.origin = network.newVariable();
        this.horizon = network.newVariable();
        network.addConstraint(origin, horizon, 0, Double.POSITIVE_INFINITY);
    }

    /**
     * Returns the origin of this temporal network.
     *
     * @return the origin of this temporal network.
     */
    public int getOrigin() {
        return origin;
    }

    /**
     * Returns the horizon of this temporal network.
     *
     * @return the horizon of this temporal network.
     */
    public int getHorizon() {
        return horizon;
    }

    /**
     * Creates a new time-point variable and returns an integer for identifying
     * it.
     *
     * @return an {@code int} representing the newly created time-point
     * variable.
     */
    public int newTimePoint() {
        int tp = network.newVariable();
        network.addConstraint(origin, tp, 0, Double.POSITIVE_INFINITY);
        network.addConstraint(tp, horizon, 0, Double.POSITIVE_INFINITY);
        return tp;
    }

    /**
     * Adds a temporal constraint to this temporal network without performing
     * propagation.
     *
     * @param from the source time-point variable of the temporal constraint.
     * @param to the target time-point variable of the temporal constraint.
     * @param min the minimum distance of the temporal constraint.
     * @param max the maximum distance of the temporal constraint.
     */
    public void addConstraint(int from, int to, double min, double max) {
        network.addConstraint(from, to, min, max);
    }

    /**
     * Checks whether this temporal network requires propagation. A temporal
     * network requires propagation if and only if there is some temporal
     * constraint which has not yet propagated.
     *
     * @return {@code true} if this temporal network requires propagation.
     */
    public boolean requiresPropagation() {
        return network.requiresPropagation();
    }

    /**
     * Performs propagation on this temporal network and returns {@code true} if
     * the propagated temporal network is consistent.
     *
     * @return {@code true} if the propagated temporal network is consistent and
     * {@code false} otherwise.
     */
    public boolean propagate() {
        return network.propagate();
    }

    /**
     * Returns the minimum allowed value of the given time-point variable.
     *
     * @param tp the time-point variable whose minimum allowed value is
     * required.
     * @return the minimum allowed value of the given time-point variable.
     */
    public double min(int tp) {
        assert !network.requiresPropagation();
        return network.distance(origin, tp).lb;
    }

    /**
     * Returns the maximum allowed value of the given time-point variable.
     *
     * @param tp the time-point variable whose maximum allowed value is
     * required.
     * @return the maximum allowed value of the given time-point variable.
     */
    public double max(int tp) {
        assert !network.requiresPropagation();
        return network.distance(origin, tp).ub;
    }

    /**
     * Returns a bound representing the distance between the given time-point
     * variables.
     *
     * @param from the source time-point variable of the required distance.
     * @param to the target time-point variable of the required distance.
     * @return a bound representing the distance between the given time-point
     * variables.
     */
    public APSPNetwork.Bound distance(int from, int to) {
        assert !network.requiresPropagation();
        return network.distance(from, to);
    }
}
