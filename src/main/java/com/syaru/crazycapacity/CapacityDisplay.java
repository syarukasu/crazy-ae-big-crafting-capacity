package com.syaru.crazycapacity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class CapacityDisplay {
    private CapacityDisplay() {}

    public static String compact(String decimal) {
        if (decimal.length() <= 12) return decimal + " B";
        return decimal.charAt(0) + "." + decimal.substring(1, 4) + " x 10^" + (decimal.length() - 1) + " B";
    }

    public static List<String> exactLines(String decimal) {
        return exactLines(decimal, 32);
    }

    public static List<String> exactLines(String decimal, int columns) {
        // The synchronized decimal is authoritative; formatting never feeds accounting.
        if (new BigInteger(decimal).signum() < 0) throw new IllegalArgumentException("Negative capacity");
        if (columns < 1) throw new IllegalArgumentException("Non-positive display width");
        List<String> lines = new ArrayList<>();
        for (int offset = 0; offset < decimal.length(); offset += columns)
            lines.add(decimal.substring(offset, Math.min(offset + columns, decimal.length())));
        return lines;
    }
}
