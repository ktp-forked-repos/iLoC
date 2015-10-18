package it.cnr.istc.iloc.utils;

import java.math.BigInteger;

/**
 *
 * @author Riccardo De Benedictis
 */
public class MathUtils {

    public static final double log2 = Math.log(2);

    public static double log2(double a) {

        return Math.log(a) / log2;
    }

    public static long safeAdd(long a, long b) {
        long r = a + b;
        return ((a ^ r) & (b ^ r)) < 0 ? a > 0 ? Long.MAX_VALUE : Long.MIN_VALUE : r;
    }

    public static long safeSub(long a, long b) {
        if (b == Long.MAX_VALUE || a == Long.MIN_VALUE) {
            return Long.MIN_VALUE;
        } else if (a == Long.MAX_VALUE) {
            assert b != Long.MIN_VALUE;
            return Long.MAX_VALUE;
        } else {
            return safeAdd(a, -b);
        }
    }

    public static long safeMult(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        long r = a * b;
        return ((a ^ b) ^ r) < 0 ? (a ^ b) > 0 ? Long.MIN_VALUE : Long.MAX_VALUE : r;
    }

    public static BigInteger factorial(int x) {
        BigInteger f = BigInteger.ONE;
        for (int i = 2; i <= x; i++) {
            f = f.multiply(BigInteger.valueOf(i));
        }
        return f;
    }

    public static int combinations(int n, int k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n - k))).intValue();
    }

    private MathUtils() {
    }
}
