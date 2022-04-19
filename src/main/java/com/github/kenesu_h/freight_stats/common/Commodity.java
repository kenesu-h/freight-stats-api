package com.github.kenesu_h.freight_stats.common;

public class Commodity {
    private int index;
    private String name;

    public Commodity(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }
}
