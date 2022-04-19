package com.github.kenesu_h.freight_stats.common;

public class TransportMethod {
    private int id;
    private String name;

    public TransportMethod(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
