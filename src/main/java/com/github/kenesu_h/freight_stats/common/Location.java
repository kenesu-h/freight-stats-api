package com.github.kenesu_h.freight_stats.common;

import java.util.Optional;

public class Location {
    private int stateId;
    private Optional<Integer> stateCountryId;
    private int countryId;

    public Location(int stateId, Optional<Integer> stateCountryId, int countryId) {
        this.stateId = stateId;
        this.stateCountryId = stateCountryId;
        this.countryId = countryId;
    }

    public int getStateId() {
        return this.stateId;
    }

    public Optional<Integer> getStateCountryId() {
        return this.stateCountryId;
    }

    public int getCountryId() {
        return this.countryId;
    }
}
