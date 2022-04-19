package com.github.kenesu_h.freight_stats.common;

public enum TradeType {
    IMPORT, EXPORT;

    public static TradeType fromInt(int i) {
        if (i == 1) {
            return IMPORT;
        } else if (i == 2) {
            return EXPORT;
        } else {
            throw new IllegalArgumentException("The integer provided to TradeType.fromInt(...) must be 1 or 2.");
        }
    }
}
