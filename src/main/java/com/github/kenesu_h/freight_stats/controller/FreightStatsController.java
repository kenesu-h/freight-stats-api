package com.github.kenesu_h.freight_stats.controller;

import com.github.kenesu_h.freight_stats.common.*;
import com.github.kenesu_h.freight_stats.model.FreightStatsModel;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;;

/**
 * A freight-stats REST controller. Any time that a user makes a visit to any of the paths this controller is bound to,
 * it will respond with some String content of some kind.
 */
@CrossOrigin
@RestController
public class FreightStatsController {
    @Autowired
    private FreightStatsModel model;

    private String basicQuery(String query) {
        StringBuilder errorBuilder = new StringBuilder();

        try {
            this.useSchema();
            ResultSet rs = model.executeQuery(query);
            try {
                return FreightStatUtils.rsToJson(rs);
            } catch (SQLException | JSONException e) {
                errorBuilder.append("The following exception occurred when reading from a result set: ");
                errorBuilder.append(e);
                return errorBuilder.toString();
            }
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when making a generic query: ");
            errorBuilder.append(e);
            return errorBuilder.toString();
        }
    }

    private void limitResults(StringBuilder queryBuilder) {
        queryBuilder.append(" limit ");
        queryBuilder.append(FreightStatUtils.RESULT_LIMIT);
    }

    private void useSchema() throws SQLException {
        StringBuilder b = new StringBuilder("use ");
        b.append(System.getenv("FREIGHT_STATS_SCHEMA"));
        model.execute(b.toString());
    }

    @GetMapping("/api/shipment")
    public String shipment(@RequestParam Map<String,String> params) {
        StringBuilder queryBuilder = new StringBuilder("select ");
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        // Ignore all params without a mapped field
        params.keySet().removeIf((String key) -> !FreightStatUtils.SHIPMENT_PARAMS.containsKey(key));

        if (!params.keySet().isEmpty()) {
            int i = 0;
            if (params.containsKey("columns")) {
                String[] subparams = params.get("columns").split(",");
                Arrays.stream(subparams).filter((String s) -> !FreightStatUtils.SHIPMENT_PARAMS.containsKey(s));
                if (subparams.length > 0) {
                    i = 0;
                    for (String subparam : subparams) {
                        columnBuilder.append(FreightStatUtils.SHIPMENT_PARAMS.get(subparam));
                        if (i < subparams.length - 1) {
                            columnBuilder.append(", ");
                        }
                        i += 1;
                    }
                } else {
                    columnBuilder.append("*");
                }
            } else {
                columnBuilder.append("*");
            }

            int columnParams = 0;
            for (String key : params.keySet()) {
                if (!key.equals("columns") && !key.equals("orderBy") && !key.equals("order")) {
                    columnParams += 1;
                    break;
                }
            }

            queryBuilder.append(columnBuilder);
            queryBuilder.append(" from shipment");

            if (columnParams > 0) {
                queryBuilder.append(" where ");

                i = 0;
                for (String key : params.keySet()) {
                    boolean isColumnParam = false;
                    if (!key.equals("columns") && !key.equals("orderBy") && !key.equals("order")) {
                        isColumnParam = true;
                        queryBuilder.append(FreightStatUtils.SHIPMENT_PARAMS.get(key));
                    }
                    switch (key) {
                        case "date":
                            queryBuilder.append(" = str_to_date(\"");
                            queryBuilder.append(params.get(key));
                            queryBuilder.append("\", \"%Y-%m-%d\")");
                            break;
                        case "startDate":
                            queryBuilder.append(" >= str_to_date(\"");
                            queryBuilder.append(params.get(key));
                            queryBuilder.append("\", \"%Y-%m-%d\")");
                            break;
                        case "endDate":
                            queryBuilder.append(" <= str_to_date(\"");
                            queryBuilder.append(params.get(key));
                            queryBuilder.append("\", \"%Y-%m-%d\")");
                            break;
                        case "columns":
                        case "orderBy":
                        case "order":
                            continue;
                        default:
                            queryBuilder.append(" = ");
                            queryBuilder.append(params.get(key));
                    }
                    if (i < columnParams) {
                        queryBuilder.append(" and ");
                    }
                    if (isColumnParam) {
                        i += 1;
                    }
                }
            }

            if (params.containsKey("orderBy")) {
                queryBuilder.append(" order by ");
                String[] subparams = params.get("orderBy").split(",");
                Arrays.stream(subparams).filter((String s) -> !FreightStatUtils.SHIPMENT_PARAMS.containsKey(s));
                i = 0;
                for (String subparam : subparams) {
                    queryBuilder.append(FreightStatUtils.SHIPMENT_PARAMS.get(subparam));
                    if (i < subparams.length - 1) {
                        queryBuilder.append(", ");
                    }
                    i += 1;
                }
                if (params.containsKey("order")) {
                    if (params.get("order").equals("asc")) {
                        queryBuilder.append(" asc");
                    } else if (params.get("order").equals("desc")) {
                        queryBuilder.append(" desc");
                    }
                }
            }
        } else {
            queryBuilder.append("* from shipment");
        }
        this.limitResults(queryBuilder);
        try {
            this.useSchema();
            return this.basicQuery(queryBuilder.toString());
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying shipments: ");
            errorBuilder.append(e);
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/monthlyValue")
    public String monthlyValue() {
        return this.basicQuery("select ship_date, sum(value) from shipment group by ship_date order by ship_date;");
    }

    @GetMapping("/api/commodity")
    public String commodity(@RequestParam(required = false) String id) {
        if (id == null) {
            StringBuilder queryBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();

            queryBuilder.append("select * from commodity");
            this.limitResults(queryBuilder);
            try {
                model.execute("use cs3200Project");
                ResultSet rs = model.executeQuery(queryBuilder.toString());
                return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                    try {
                        return FreightStatUtils.rsToCommodity(r);
                    } catch (SQLException e) {
                        errorBuilder.append("The following exception occurred when reading from a result set: ");
                        errorBuilder.append(e);
                        return errorBuilder.toString();
                    }
                });
            } catch (SQLException e) {
                errorBuilder.append("The following exception occurred when querying commodities: ");
                errorBuilder.append(e);
                return errorBuilder.toString();
            }
        } else {
            return this.basicQuery("select commodity_name from commodity where commodity_id = " + id);
        }
    }

    @GetMapping("/api/transportMethod")
    public String transportMethod() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from transport_method");
        this.limitResults(queryBuilder);
        try {
            model.execute("use cs3200Project");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToTransportMethod(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e);
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying transport methods: ");
            errorBuilder.append(e);
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/state")
    public String state() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from state limit 100;");
        this.limitResults(queryBuilder);
        try {
            model.execute("use cs3200Project");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToState(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e);
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying states: ");
            errorBuilder.append(e);
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/country")
    public String country() {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        queryBuilder.append("select * from country limit 100;");
        try {
            model.execute("use cs3200Project");
            ResultSet rs = model.executeQuery(queryBuilder.toString());
            return FreightStatUtils.rsToSerializedObjects(rs, (ResultSet r) -> {
                try {
                    return FreightStatUtils.rsToCountry(r);
                } catch (SQLException e) {
                    errorBuilder.append("The following exception occurred when reading from a result set: ");
                    errorBuilder.append(e);
                    return errorBuilder.toString();
                }
            });
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying countries: ");
            errorBuilder.append(e);
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/covidData")
    public String covidData(@RequestParam Map<String,String> params) {
        StringBuilder queryBuilder = new StringBuilder("select ");
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        // Ignore all params without a mapped field
        params.keySet().removeIf((String key) -> !FreightStatUtils.COVID_DATA_PARAMS.containsKey(key));

        if (!params.keySet().isEmpty()) {
            int i = 0;
            if (params.containsKey("columns")) {
                String[] subparams = params.get("columns").split(",");
                Arrays.stream(subparams).filter((String s) -> !FreightStatUtils.COVID_DATA_PARAMS.containsKey(s));
                if (subparams.length > 0) {
                    i = 0;
                    for (String subparam : subparams) {
                        columnBuilder.append(FreightStatUtils.COVID_DATA_PARAMS.get(subparam));
                        if (i < subparams.length - 1) {
                            columnBuilder.append(", ");
                        }
                        i += 1;
                    }
                } else {
                    columnBuilder.append("*");
                }
            } else {
                columnBuilder.append("*");
            }

            int columnParams = 0;
            for (String key : params.keySet()) {
                if (!key.equals("columns") && !key.equals("orderBy") && !key.equals("order")) {
                    columnParams += 1;
                    break;
                }
            }

            queryBuilder.append(columnBuilder);
            queryBuilder.append(" from covid_data");

            if (columnParams > 0) {
                queryBuilder.append(" where ");

                i = 0;
                for (String key : params.keySet()) {
                    boolean isColumnParam = false;
                    if (!key.equals("columns") && !key.equals("orderBy") && !key.equals("order")) {
                        isColumnParam = true;
                        queryBuilder.append(FreightStatUtils.COVID_DATA_PARAMS.get(key));
                    }
                    switch (key) {
                        case "date":
                            queryBuilder.append(" = str_to_date(\"");
                            queryBuilder.append(params.get(key));
                            queryBuilder.append("\", \"%Y-%m-%d\")");
                            break;
                        case "startDate":
                            queryBuilder.append(" >= str_to_date(\"");
                            queryBuilder.append(params.get(key));
                            queryBuilder.append("\", \"%Y-%m-%d\")");
                            break;
                        case "endDate":
                            queryBuilder.append(" <= str_to_date(\"");
                            queryBuilder.append(params.get(key));
                            queryBuilder.append("\", \"%Y-%m-%d\")");
                            break;
                        case "columns":
                        case "orderBy":
                        case "order":
                            continue;
                        default:
                            queryBuilder.append(" = ");
                            queryBuilder.append(params.get(key));
                    }
                    if (i < columnParams) {
                        queryBuilder.append(" and ");
                    }
                    if (isColumnParam) {
                        i += 1;
                    }
                }
            }

            if (params.containsKey("orderBy")) {
                queryBuilder.append(" order by ");
                String[] subparams = params.get("orderBy").split(",");
                Arrays.stream(subparams).filter((String s) -> !FreightStatUtils.COVID_DATA_PARAMS.containsKey(s));
                i = 0;
                for (String subparam : subparams) {
                    queryBuilder.append(FreightStatUtils.COVID_DATA_PARAMS.get(subparam));
                    if (i < subparams.length - 1) {
                        queryBuilder.append(", ");
                    }
                    i += 1;
                }
                if (params.containsKey("order")) {
                    if (params.get("order").equals("asc")) {
                        queryBuilder.append(" asc");
                    } else if (params.get("order").equals("desc")) {
                        queryBuilder.append(" desc");
                    }
                }
            }
        } else {
            queryBuilder.append("* from covid_data");
        }
        this.limitResults(queryBuilder);
        try {
            this.useSchema();
            return this.basicQuery(queryBuilder.toString());
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when querying shipments: ");
            errorBuilder.append(e);
            return errorBuilder.toString();
        }
    }

    @GetMapping("/api/monthlyCases")
    public String monthlyCases() {
        return this.basicQuery("select cases_month, sum(covid_cases) from covid_data group by cases_month order by cases_month;");
    }

    @GetMapping("/api/monthlyDeaths")
    public String monthlyDeaths() {
        return this.basicQuery("select cases_month, sum(covid_deaths) from covid_data group by cases_month order by cases_month;");
    }
}
