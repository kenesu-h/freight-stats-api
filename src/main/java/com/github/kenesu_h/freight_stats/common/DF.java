package com.github.kenesu_h.freight_stats.common;

public enum DF {
    DOMESTIC, FOREIGN;

    public static DF fromInt(int i) {
        if (i == 1) {
            return DOMESTIC;
        } else if (i == 2) {
            return FOREIGN;
        } else {
            throw new IllegalArgumentException("The integer provided to DF.fromInt(...) must be 1 or 2.");
        }
    }
}
