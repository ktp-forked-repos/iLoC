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
 * Represents a bound between two temporal points.
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Bound {

    public final double min;
    public final double max;

    public Bound(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return "[" + (min == Double.NEGATIVE_INFINITY ? "-inf" : min) + ", " + (max == Double.POSITIVE_INFINITY ? "+inf" : max) + "]";
    }
}
