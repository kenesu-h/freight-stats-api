package com.github.kenesu_h.freight_stats.common;

import java.time.LocalDate;
import java.util.Optional;

public class CovidCase {
    private int id;
    private int positiveTests;
    private int totalTests;
    private Optional<Integer> testingRate;
    private LocalDate date;
    private int locationStateId;

    public CovidCase(int id, int positiveTests, int totalTests, Optional<Integer> testingRate, LocalDate date, int locationStateId) {
        this.id = id;
        this.positiveTests = positiveTests;
        this.totalTests = totalTests;
        this.testingRate = testingRate;
        this.date = date;
        this.locationStateId = locationStateId;
    }

    public int getId() {
        return this.id;
    }

    public int getPositiveTests() {
        return this.positiveTests;
    }

    public int getTotalTests() {
        return this.totalTests;
    }

    public Optional<Integer> getTestingRate() {
        return this.testingRate;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public int getLocationStateId() {
        return this.locationStateId;
    }
}
