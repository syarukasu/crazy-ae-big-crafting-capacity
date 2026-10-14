package com.syaru.crazycapacity;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;

class CapacityMathTest {
    @Test void countsEveryStoredUnitWithoutLongOverflow() {
        BigInteger unit = BigInteger.TEN.pow(1024).subtract(BigInteger.ONE);
        BigInteger total = CapacityMath.addStack(BigInteger.ZERO, unit, 64);
        total = CapacityMath.addStack(total, unit, 64);
        total = CapacityMath.addStack(total, BigInteger.valueOf(1024), 32);
        assertEquals(unit.multiply(BigInteger.valueOf(128)).add(BigInteger.valueOf(32768)), total);
        assertEquals(Long.MAX_VALUE, CapacityMath.longFacade(total));
        assertEquals(0, total.subtract(total).signum());
    }

    @Test void removalChangesExactCapacityEvenWhenLongFacadeIsUnchanged() {
        BigInteger unit = BigInteger.TEN.pow(1024).subtract(BigInteger.ONE);
        assertTrue(CapacityMath.requiresExtraRebuild(unit.multiply(BigInteger.TWO), unit));
        assertFalse(CapacityMath.requiresExtraRebuild(BigInteger.valueOf(2048), BigInteger.valueOf(1024)));
        assertFalse(CapacityMath.requiresExtraRebuild(unit, unit));
    }

    @Test void ordinaryStorageAndEmptySlotsRetainTheirExactValues() {
        assertEquals(BigInteger.valueOf(131072), CapacityMath.addStack(BigInteger.ZERO, BigInteger.valueOf(1024), 128));
        assertEquals(BigInteger.ZERO, CapacityMath.addStack(BigInteger.ZERO, BigInteger.ZERO, 0));
        assertEquals(131072, CapacityMath.longFacade(BigInteger.valueOf(131072)));
        assertThrows(IllegalArgumentException.class,
                () -> CapacityMath.addStack(BigInteger.ZERO, BigInteger.valueOf(-1), 1));
    }
}
