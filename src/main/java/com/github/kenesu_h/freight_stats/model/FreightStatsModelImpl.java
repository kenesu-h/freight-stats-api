package com.github.kenesu_h.freight_stats.model;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A generic model implementation of a FreightStatsModel. This relies on environment variables to be set, and will not
 * catch exceptions --- the database connection is crucial to the functionality of this API, after all.
 */
@Component("model")
public class FreightStatsModelImpl implements FreightStatsModel {
    private Connection connection;

    FreightStatsModelImpl() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("jdbc:mysql://");
        urlBuilder.append(System.getenv("FREIGHT_STATS_HOSTNAME"));
        urlBuilder.append("?currentSchema=");
        urlBuilder.append(System.getenv("FREIGHT_STATS_SCHEMA"));

        this.connection = DriverManager.getConnection(
                urlBuilder.toString(),
                System.getenv("FREIGHT_STATS_USERNAME"),
                System.getenv("FREIGHT_STATS_PASSWORD")
        );
    }

    @Override
    public ResultSet execute(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(query);
    }
}
