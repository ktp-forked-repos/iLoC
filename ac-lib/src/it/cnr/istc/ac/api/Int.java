/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>.
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
package it.cnr.istc.ac.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Int extends BigInteger {

    private static final long serialVersionUID = 1L;
    public static final Int POSITIVE_INFINITY = new Int(false, true);
    public static final Int NEGATIVE_INFINITY = new Int(true, false);
    public static final Int ZERO = new Int("0");
    public static final Int ONE = new Int("1");
    public final boolean negative_inifinity;
    public final boolean positive_inifinity;

    public Int(String string) {
        super(string);
        this.negative_inifinity = false;
        this.positive_inifinity = false;
    }

    private Int(boolean negative_inifinity, boolean positive_inifinity) {
        super("0");
        this.negative_inifinity = negative_inifinity;
        this.positive_inifinity = positive_inifinity;
    }

    public boolean isPositiveInifinity() {
        return positive_inifinity;
    }

    public boolean isNegativeInifinity() {
        return negative_inifinity;
    }

    public boolean isInifinity() {
        return negative_inifinity || positive_inifinity;
    }

    public boolean eq(Int val) {
        return (negative_inifinity && val.negative_inifinity) || (positive_inifinity && val.positive_inifinity) || (!isInifinity() && !val.isInifinity() && compareTo(val) == 0);
    }

    public boolean leq(Int val) {
        return negative_inifinity || val.positive_inifinity || (!positive_inifinity && !val.negative_inifinity && (!isInifinity() && !val.isInifinity() && compareTo(val) <= 0));
    }

    public boolean geq(Int val) {
        return positive_inifinity || val.negative_inifinity || (!negative_inifinity && !val.positive_inifinity && (!isInifinity() && !val.isInifinity() && compareTo(val) >= 0));
    }

    public boolean lt(Int val) {
        return (negative_inifinity && !val.negative_inifinity) || (!positive_inifinity && val.positive_inifinity) || (!isInifinity() && !val.isInifinity() && compareTo(val) < 0);
    }

    public boolean gt(Int val) {
        return (positive_inifinity && !val.positive_inifinity) || (!negative_inifinity && val.negative_inifinity) || (!isInifinity() && !val.isInifinity() && compareTo(val) > 0);
    }

    public Int add(Int i) {
        assert !(negative_inifinity && i.positive_inifinity) : "Indeterminate form -inf + +inf";
        assert !(positive_inifinity && i.negative_inifinity) : "Indeterminate form +inf + -inf";
        if (isInifinity() || i.isInifinity()) {
            if (positive_inifinity) {
                return POSITIVE_INFINITY;
            } else if (negative_inifinity) {
                return NEGATIVE_INFINITY;
            } else if (i.positive_inifinity) {
                return POSITIVE_INFINITY;
            } else {
                assert i.negative_inifinity;
                return NEGATIVE_INFINITY;
            }
        } else {
            assert !isInifinity() && !i.isInifinity();
            return new Int(super.add(i).toString());
        }
    }

    public Int subtract(Int i) {
        assert !(negative_inifinity && i.negative_inifinity) : "Indeterminate form -inf - -inf";
        assert !(positive_inifinity && i.positive_inifinity) : "Indeterminate form +inf - +inf";
        if (isInifinity() || i.isInifinity()) {
            if (positive_inifinity) {
                return POSITIVE_INFINITY;
            } else if (negative_inifinity) {
                return NEGATIVE_INFINITY;
            } else if (i.positive_inifinity) {
                return NEGATIVE_INFINITY;
            } else {
                assert i.negative_inifinity;
                return POSITIVE_INFINITY;
            }
        } else {
            assert !isInifinity() && !i.isInifinity();
            return new Int(super.subtract(i).toString());
        }
    }

    public Int multiply(Int i) {
        assert !(eq(ZERO) && i.isInifinity()) : "Indeterminate form 0 * inf";
        assert !(isInifinity() && i.eq(ZERO)) : "Indeterminate form inf * 0";
        if (isInifinity() || i.isInifinity()) {
            if (geq(ZERO)) {
                if (i.geq(ZERO)) {
                    return POSITIVE_INFINITY;
                } else {
                    return NEGATIVE_INFINITY;
                }
            } else {
                assert leq(ZERO);
                if (i.geq(ZERO)) {
                    return NEGATIVE_INFINITY;
                } else {
                    return POSITIVE_INFINITY;
                }
            }
        } else {
            assert !isInifinity() && !i.isInifinity();
            return new Int(super.multiply(i).toString());
        }
    }

    public Int divide(Int i, RoundingMode mode) {
        assert !(eq(ZERO) && i.eq(ZERO)) : "Indeterminate form 0 / 0";
        assert !(isInifinity() && i.isInifinity()) : "Indeterminate form inf / inf";
        if (isInifinity() || i.isInifinity()) {
            if (i.isInifinity()) {
                return ZERO;
            } else if (geq(ZERO)) {
                if (i.geq(ZERO)) {
                    return POSITIVE_INFINITY;
                } else {
                    return NEGATIVE_INFINITY;
                }
            } else {
                assert leq(ZERO);
                if (i.geq(ZERO)) {
                    return NEGATIVE_INFINITY;
                } else {
                    return POSITIVE_INFINITY;
                }
            }
        } else {
            assert !isInifinity() && !i.isInifinity();
            return new Int(new BigDecimal(this).divide(new BigDecimal(i)).setScale(0, mode).toString());
        }
    }

    @Override
    public int compareTo(BigInteger bi) {
        // this method has been added for debugging purposes..
        assert !isInifinity() && !((Int) bi).isInifinity() : "Comparing " + this + " and " + bi;
        return super.compareTo(bi);
    }

    @Override
    public String toString() {
        if (positive_inifinity) {
            return "+inf";
        } else if (negative_inifinity) {
            return "-inf";
        } else {
            return super.toString();
        }
    }

    /**
     * Returns a Int whose value is the absolute value of the given Int.
     *
     * @param i the Int whose absolute value is desired.
     * @return a Int whose value is the absolute value of the given Int.
     */
    public static Int abs(Int i) {
        if (i.positive_inifinity || i.negative_inifinity) {
            return Int.POSITIVE_INFINITY;
        } else {
            return new Int(i.abs().toString());
        }
    }

    /**
     * Returns a Int whose value is (-i).
     *
     * @param i the Int whose negated value is desired.
     * @return a Int whose value is (-i).
     */
    public static Int negate(Int i) {
        if (i.positive_inifinity) {
            return Int.NEGATIVE_INFINITY;
        } else if (i.negative_inifinity) {
            return Int.POSITIVE_INFINITY;
        } else {
            return new Int(i.negate().toString());
        }
    }

    /**
     * Finds result interval for multiplication of [a, b] * [c, d]
     *
     * @param a the lower-bound of first variable.
     * @param b the upper-bound of first variable.
     * @param c the lower-bound of second variable.
     * @param d the upper-bound of second variable.
     * @return the lower-bound and the upper-bound of the multiplication.
     */
    public static Int[] multiply(Int a, Int b, Int c, Int d) {
        Int ac = a.multiply(c), ad = a.multiply(d), bc = b.multiply(c), bd = b.multiply(d);
        return new Int[]{min(ac, ad, bc, bd), max(ac, ad, bc, bd)};
    }

    /**
     * Finds result interval for division of [a, b] / [c, d]
     *
     * @param a the lower-bound of first variable.
     * @param b the upper-bound of first variable.
     * @param c the lower-bound of second variable.
     * @param d the upper-bound of second variable.
     * @return the lower-bound and the upper-bound of the multiplication.
     */
    public static Int[] divide(Int a, Int b, Int c, Int d) {
        if (c.leq(Int.ZERO) && d.geq(Int.ZERO)) {
            // 0 appears in the denominator..
            return new Int[]{Int.NEGATIVE_INFINITY, Int.POSITIVE_INFINITY};
        } else {
            Int min_ac = a.divide(c, RoundingMode.DOWN), min_ad = a.divide(d, RoundingMode.DOWN), min_bc = b.divide(c, RoundingMode.DOWN), min_bd = b.divide(d, RoundingMode.DOWN), max_ac = a.divide(c, RoundingMode.UP), max_ad = a.divide(d, RoundingMode.UP), max_bc = b.divide(c, RoundingMode.UP), max_bd = b.divide(d, RoundingMode.UP);
            return new Int[]{min(min_ac, min_ad, min_bc, min_bd), max(max_ac, max_ad, max_bc, max_bd)};
        }
    }

    /**
     * Returns the smaller of the given values.
     *
     * @param val an array of values.
     * @return the smaller of the given values.
     */
    public static Int min(Int... val) {
        Int min = Int.POSITIVE_INFINITY;
        for (Int i : val) {
            if (i.lt(min)) {
                min = i;
            }
        }
        return min;
    }

    /**
     * Returns the greater of the given values.
     *
     * @param val an array of values.
     * @return the greater of the given values.
     */
    public static Int max(Int... val) {
        Int max = Int.NEGATIVE_INFINITY;
        for (Int i : val) {
            if (i.gt(max)) {
                max = i;
            }
        }
        return max;
    }
}
