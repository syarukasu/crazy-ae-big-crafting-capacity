package com.syaru.crazycapacity;

import java.math.BigInteger;
import java.util.Objects;

public final class CapacityMath {
    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    private CapacityMath() {}

    public static BigInteger addStack(BigInteger total, BigInteger perUnit, int count) {
        Objects.requireNonNull(total);
        Objects.requireNonNull(perUnit);
        if (total.signum() < 0 || perUnit.signum() < 0 || count < 0) {
            throw new IllegalArgumentException("Negative crafting storage contribution");
        }
        return total.add(perUnit.multiply(BigInteger.valueOf(count)));
    }

    public static long longFacade(BigInteger exact) {
        if (exact.signum() < 0) throw new IllegalArgumentException("Negative crafting capacity");
        return exact.min(LONG_MAX).longValueExact();
    }

    public static boolean requiresExtraRebuild(BigInteger before, BigInteger after) {
        return !before.equals(after) && longFacade(before) == longFacade(after);
    }
}
