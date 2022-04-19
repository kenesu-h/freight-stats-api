package com.github.kenesu_h.freight_stats.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A freight-stats model. This doesn't really act as a model. It mostly just acts as a wrapper for the internal database
 * connection. In other words, if the application needs to make queries to the database, it will be through the model.
 */
public interface FreightStatsModel {
    ResultSet execute(String query) throws SQLException;
}
