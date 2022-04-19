package com.github.kenesu_h.freight_stats.common;

import java.util.Optional;

public class Country {
    private int id;
    private Optional<String> name;

    public Country(int id, Optional<String> name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public Optional<String> getName() {
        return this.name;
    }
}
