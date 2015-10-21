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
 * This class represents a distance constraint between two time points. The
 * constraint generated is:
 * <p>
 * min ≤ to - from ≤ max
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class DistanceConstraint {

    public final int from, to;
    public final double min, max;

    public DistanceConstraint(int from, int to, double min, double max) {
        assert min <= max;
        this.from = from;
        this.to = to;
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return min + " ≤ tp" + to + " - tp" + from + " ≤ " + max;
    }
}
