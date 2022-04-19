package com.github.kenesu_h.freight_stats.common;

public class State {
    private int id;
    private String stateName;
    private int countryId;

    public State(int id, String stateName, int countryId) {
        this.id = id;
        this.stateName = stateName;
        this.countryId = countryId;
    }

    public int getId() {
        return this.id;
    }

    public String getStateName() {
        return this.stateName;
    }

    public int getCountryId() {
        return this.countryId;
    }
}
