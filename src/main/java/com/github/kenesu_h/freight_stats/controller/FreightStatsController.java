package com.github.kenesu_h.freight_stats.controller;

import com.github.kenesu_h.freight_stats.common.*;
import com.github.kenesu_h.freight_stats.model.FreightStatsModel;
import org.json.JSONException;
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

    private String genericQuery(String query) {
        StringBuilder errorBuilder = new StringBuilder();

        try {
            model.execute("use `cs3200Project`;");
            ResultSet rs = model.executeQuery(query);
            try {
                return FreightStatUtils.rsToJson(rs);
            } catch (SQLException | JSONException e) {
                errorBuilder.append("The following exception occurred when reading from a result set: ");
                errorBuilder.append(e.toString());
                return errorBuilder.toString();
            }
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when making a generic query: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/shipment")
    public String shipment() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from shipment limit 100;");
        try {
            model.execute("use `cs3200Project`;");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
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

    @GetMapping("/api/monthly_value")
    public String monthlyValue() {
        return this.genericQuery("select ship_date, sum(value) from shipment group by ship_date order by ship_date;");
    }

    @GetMapping("/api/commodity")
    public String commodity(@RequestParam(required = false) String id) {
        if (id == null) {
            StringBuilder queryBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();

            queryBuilder.append("select * from commodity limit 100;");
            try {
                model.execute("use cs3200Project;");
                ResultSet rs = model.executeQuery(queryBuilder.toString());
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
        } else {
            return this.genericQuery("select commodity_name from commodity where commodity_id = " + id);
        }
    }

    @GetMapping("/api/transportMethod")
    public String transportMethod() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from transport_method limit 100;");
        try {
            model.execute("use cs3200Project;");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
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

        queryBuilder.append("select * from state limit 100;");
        try {
            model.execute("use cs3200Project;");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
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

        queryBuilder.append("select * from country limit 100;");
        try {
            model.execute("use cs3200Project;");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
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

    @GetMapping("/api/covidCase")
    public String covidCase() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from covid_case limit 100;");
        try {
            model.execute("use cs3200Project;");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
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
