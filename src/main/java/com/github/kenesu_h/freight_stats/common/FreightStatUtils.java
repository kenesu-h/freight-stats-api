package com.github.kenesu_h.freight_stats.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

public class FreightStatUtils {
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
        int id = rs.getInt("Shipment_id");
        TradeType tradeType = TradeType.fromInt(rs.getInt("trade_type"));
        int commodityId = rs.getInt("CommodityIndex");
        int transportMethod = rs.getInt("TransportationMethod_id");
        int source = rs.getInt("Source");
        int destination = rs.getInt("Destination");
        int value = rs.getInt("Value");
        int weight = rs.getInt("Weight");
        int freightCharges = rs.getInt("FreightCharges");
        DF df = DF.valueOf(rs.getString("DF"));
        boolean containerized = rs.getInt("Containerized") == 1;
        LocalDate date = LocalDate.parse(
                rs.getDate("month_month_id").toString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        return new Shipment(
                id, tradeType, commodityId, transportMethod, source, destination, value, weight,
                freightCharges, df, containerized, date
        );
    }

    public static Commodity rsToCommodity(ResultSet rs) throws SQLException {
        int id = rs.getInt("CommodityIndex");
        String name = rs.getString("name");
        return new Commodity(id, name);
    }

    public static TransportMethod rsToTransportMethod(ResultSet rs) throws SQLException {
        int id = rs.getInt("TransportationMethod_id");
        String name = rs.getString("name");
        return new TransportMethod(id, name);
    }

    public static State rsToState(ResultSet rs) throws SQLException {
        int id = rs.getInt("State_id");
        String name = rs.getString("StateName");
        int countryId = rs.getInt("Country_Country_id");
        return new State(id, name, countryId);
    }

    public static Country rsToCountry(ResultSet rs) throws SQLException {
        int id = rs.getInt("Country_id");
        Optional<String> name = Optional.ofNullable(rs.getString("Name"));
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
