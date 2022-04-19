package com.github.kenesu_h.freight_stats.common;

public enum DF {
    UNKNOWN, DOMESTIC, FOREIGN;

    public static DF fromInt(int i) {
        if (i == 0) {
            return UNKNOWN;
        } else if (i == 1) {
            return DOMESTIC;
        } else if (i == 2) {
            return FOREIGN;
        } else {
            throw new IllegalArgumentException("The integer " + i + " provided to DF.fromInt(...) must be 1 or 2.");
        }
    }
}
