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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Real extends BigDecimal {

    private static final long serialVersionUID = 1L;
    public static final Real POSITIVE_INFINITY = new Real(false, true);
    public static final Real NEGATIVE_INFINITY = new Real(true, false);
    public static final Real ZERO = new Real("0");
    public final boolean negative_inifinity;
    public final boolean positive_inifinity;

    public Real(String string) {
        super(string);
        this.negative_inifinity = false;
        this.positive_inifinity = false;
    }

    private Real(boolean negative_inifinity, boolean positive_inifinity) {
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

    public boolean eq(Real val) {
        return (negative_inifinity && val.negative_inifinity) || (positive_inifinity && val.positive_inifinity) || (!isInifinity() && !val.isInifinity() && compareTo(val) == 0);
    }

    public boolean leq(Real val) {
        return negative_inifinity || val.positive_inifinity || (!positive_inifinity && !val.negative_inifinity && (!isInifinity() && !val.isInifinity() && compareTo(val) <= 0));
    }

    public boolean geq(Real val) {
        return positive_inifinity || val.negative_inifinity || (!negative_inifinity && !val.positive_inifinity && (!isInifinity() && !val.isInifinity() && compareTo(val) >= 0));
    }

    public boolean lt(Real val) {
        return (negative_inifinity && !val.negative_inifinity) || (!positive_inifinity && val.positive_inifinity) || (!isInifinity() && !val.isInifinity() && compareTo(val) < 0);
    }

    public boolean gt(Real val) {
        return (positive_inifinity && !val.positive_inifinity) || (!negative_inifinity && val.negative_inifinity) || (!isInifinity() && !val.isInifinity() && compareTo(val) > 0);
    }

    public Real add(Real i) {
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
            return new Real(super.add(i).toString());
        }
    }

    public Real subtract(Real i) {
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
            return new Real(super.subtract(i).toString());
        }
    }

    public Real multiply(Real i) {
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
            return new Real(super.multiply(i).toString());
        }
    }

    public Real divide(Real i) {
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
            return new Real(super.divide(i).toString());
        }
    }

    @Override
    public int compareTo(BigDecimal bi) {
        // this method has been added for debugging purposes..
        assert !isInifinity() && !((Real) bi).isInifinity() : "Comparing " + this + " and " + bi;
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
     * Returns a Real whose value is the absolute value of the given Real.
     *
     * @param i the Real whose absolute value is desired.
     * @return a Int whose value is the absolute value of the given Real.
     */
    public static Real abs(Real i) {
        if (i.positive_inifinity || i.negative_inifinity) {
            return Real.POSITIVE_INFINITY;
        } else {
            return new Real(i.abs().toString());
        }
    }

    /**
     * Returns a Int whose value is (-i).
     *
     * @param i the Int whose negated value is desired.
     * @return a Int whose value is (-i).
     */
    public static Real negate(Real i) {
        if (i.positive_inifinity) {
            return Real.NEGATIVE_INFINITY;
        } else if (i.negative_inifinity) {
            return Real.POSITIVE_INFINITY;
        } else {
            return new Real(i.negate().toString());
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
    public static Real[] multiply(Real a, Real b, Real c, Real d) {
        Real ac = a.multiply(c), ad = a.multiply(d), bc = b.multiply(c), bd = b.multiply(d);
        return new Real[]{min(ac, ad, bc, bd), max(ac, ad, bc, bd)};
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
    public static Real[] divide(Real a, Real b, Real c, Real d) {
        if (c.leq(Real.ZERO) && d.geq(Real.ZERO)) {
            // 0 appears in the denominator..
            return new Real[]{Real.NEGATIVE_INFINITY, Real.POSITIVE_INFINITY};
        } else {
            Real ac = a.divide(c), ad = a.divide(d), bc = b.divide(c), bd = b.divide(d);
            return new Real[]{min(ac, ad, bc, bd), max(ac, ad, bc, bd)};
        }
    }

    /**
     * Returns the smaller of the given values.
     *
     * @param val an array of values.
     * @return the smaller of the given values.
     */
    public static Real min(Real... val) {
        Real min = Real.POSITIVE_INFINITY;
        for (Real i : val) {
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
    public static Real max(Real... val) {
        Real max = Real.NEGATIVE_INFINITY;
        for (Real i : val) {
            if (i.gt(max)) {
                max = i;
            }
        }
        return max;
    }
}
