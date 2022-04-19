package com.github.kenesu_h.freight_stats.controller;

import com.github.kenesu_h.freight_stats.common.*;
import com.github.kenesu_h.freight_stats.model.FreightStatsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;;

/**
 * A freight-stats REST controller. Any time that a user makes a visit to any of the paths this controller is bound to,
 * it will respond with some String content of some kind.
 */
@RestController
public class FreightStatsController {
    @Autowired
    private FreightStatsModel model;

    @GetMapping("/api/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/api/shipment")
    public String shipment() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from shipment;");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToShipment(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying shipments: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/commodity")
    public String commodity() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from commodity;");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToCommodity(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying commodities: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/transportMethod")
    public String transportMethod() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from transport_method;");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToTransportMethod(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying transport methods: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/state")
    public String state() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from state;");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToState(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying states: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/country")
    public String country() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from country;");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToCountry(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying countries: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/location")
    public String location() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from location;");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToLocation(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying locations: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/covidCase")
    public String covidCase() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from covid_case");
        try {
            model.execute("use `freight-stats`");
            ResultSet rs = model.execute(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToCovidCase(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e.toString());
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying COVID cases: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }
}
