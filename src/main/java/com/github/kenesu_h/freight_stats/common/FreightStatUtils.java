package com.github.kenesu_h.freight_stats.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class FreightStatUtils {
    // Thanks to https://stackoverflow.com/a/507658
    public static Map<String, String> shipmentFields;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "shipment_id");
        map.put("tradeType", "trade_type");
        map.put("commodityId", "commodity_type");
        map.put("transportMethod", "transport_method");
        map.put("source", "source_state");
        map.put("destination", "destination_state");
        map.put("value", "value");
        map.put("weight", "weight");
        map.put("freightCharges", "freight_charges");
        map.put("df", "df");
        map.put("containerized", "containerized");
        map.put("date", "ship_date");
        shipmentFields = Collections.unmodifiableMap(map);
    }

    // Thanks to https://stackoverflow.com/a/59727839
    public static boolean inRs(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Thanks to https://stackoverflow.com/a/41453021
    public static String rsToJson(ResultSet rs) throws SQLException, JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i=1; i<=numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json.toString();
    }

    public static <T> String serializeObjects(ArrayList<T> list) throws JsonProcessingException {
        StringBuilder serialized = new StringBuilder();
        serialized.append("[");

        // I took the code below from https://stackoverflow.com/a/15786175
        ObjectWriter ow = new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .writer();
        for (int i = 0; i < list.size(); i++) {
            serialized.append(ow.writeValueAsString(list.get(i)));
            if (i != (list.size() - 1)) {
                serialized.append(",");
            }
        }

        serialized.append("]");
        return serialized.toString();
    }

    public static <T> String rsToSerializedObjects(ResultSet rs, Function<ResultSet, T> f) {
        ArrayList<T> objects = new ArrayList<>();
        StringBuilder errorBuilder = new StringBuilder();

        try {
            while (rs.next()) {
                objects.add(f.apply(rs));
            }
            try {
                return FreightStatUtils.serializeObjects(objects);
            } catch (JsonProcessingException e) {
                errorBuilder.append("The following exception occurred when serializing shipments: ");
                errorBuilder.append(e.toString());
                return errorBuilder.toString();
            }
        } catch (SQLException e) {
            errorBuilder.append("The following exception occurred when reading objects: ");
            errorBuilder.append(e.toString());
            return errorBuilder.toString();
        }
    }

    public static Shipment rsToShipment(ResultSet rs) throws SQLException {
        int id = rs.getInt("shipment_id");
        TradeType tradeType = TradeType.fromInt(rs.getInt("trade_type"));
        int commodityId = rs.getInt("commodity_type");
        int transportMethod = rs.getInt("transport_method");
        int source = rs.getInt("source_state");
        int destination = rs.getInt("destination_state");
        int value = rs.getInt("value");
        int weight = rs.getInt("weight");
        int freightCharges = rs.getInt("freight_charges");
        DF df = DF.fromInt(rs.getInt("df"));
        boolean containerized = rs.getInt("containerized") == 1;
        LocalDate date = LocalDate.parse(
                rs.getDate("ship_date").toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        return new Shipment(
                id, tradeType, commodityId, transportMethod, source, destination, value, weight,
                freightCharges, df, containerized, date
        );
    }

    public static Commodity rsToCommodity(ResultSet rs) throws SQLException {
        int id = rs.getInt("commodity_id");
        String name = rs.getString("commodity_name");
        return new Commodity(id, name);
    }

    public static TransportMethod rsToTransportMethod(ResultSet rs) throws SQLException {
        int id = rs.getInt("transport_method_id");
        String name = rs.getString("transport_method_name");
        return new TransportMethod(id, name);
    }

    public static State rsToState(ResultSet rs) throws SQLException {
        int id = rs.getInt("state_id");
        String name = rs.getString("state_name");
        int countryId = rs.getInt("country_id");
        return new State(id, name, countryId);
    }

    public static Country rsToCountry(ResultSet rs) throws SQLException {
        int id = rs.getInt("country_id");
        Optional<String> name = Optional.ofNullable(rs.getString("country_name"));
        return new Country(id, name);
    }

    public static Location rsToLocation(ResultSet rs) throws SQLException {
        int stateId = rs.getInt("state_State_id");
        Optional<Integer> stateCountryId = Optional.of(rs.getInt("state_Country_Country_id"));
        int countryId = rs.getInt("country_Country_id");
        return new Location(stateId, stateCountryId, countryId);
    }

    public static CovidCase rsToCovidCase(ResultSet rs) throws SQLException {
        int id = rs.getInt("covid_case_id");
        int positiveTests = rs.getInt("positive_tests");
        int totalTests = rs.getInt("total_tests");
        Optional<Integer> testingRate = Optional.of(rs.getInt("testing_rate"));
        LocalDate date = LocalDate.parse(
                rs.getDate("month_month_id").toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
        int locationId = rs.getInt("covidLocation__state_State_id");
        return new CovidCase(id, positiveTests, totalTests, testingRate, date, locationId);
    }
}
