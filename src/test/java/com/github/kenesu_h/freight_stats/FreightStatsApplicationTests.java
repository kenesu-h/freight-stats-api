package com.github.kenesu_h.freight_stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kenesu_h.freight_stats.common.*;
import com.github.kenesu_h.freight_stats.controller.FreightStatsController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

class FreightStatsApplicationTests {

	private FreightStatsController controller;
	private DateTimeFormatter df;

	private Shipment shipmentA;
	private Shipment shipmentB;
	private Shipment shipmentC;

	private CovidCase caseA;
	private CovidCase caseB;
	private CovidCase caseC;

	private void initializeExamples() throws ParseException {
		controller = new FreightStatsController();
		df = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;

		shipmentA = new Shipment(1, TradeType.fromInt(1), 1, 1, 1, 2, 100, 100, 100, DF.DOMESTIC, true, LocalDate.parse("2020-04-19", df));
		shipmentB = new Shipment(2, TradeType.fromInt(2), 2, 2, 3, 4, 500, 1000, 1000, DF.FOREIGN, false, LocalDate.parse("2021-05-20", df));
		shipmentC = new Shipment(3, TradeType.fromInt(1), 3, 1, 1, 4, 333, 999, 10000, DF.FOREIGN, true, LocalDate.parse("2022-12-31", df));

		caseA = new CovidCase(1, 2, 3, null, LocalDate.parse("2020-04-19", df), 1);
		caseB = new CovidCase(2, 100, 200, Optional.of(5), LocalDate.parse("2021-05-20", df), 2);
		caseC = new CovidCase(3, 333, 1000, null, LocalDate.parse("2022-12-31", df), 3);
	}

	@Test
	public void testTradeType() {
		Assertions.assertEquals(TradeType.IMPORT, TradeType.fromInt(1));
		Assertions.assertEquals(TradeType.EXPORT, TradeType.fromInt(2));
		Assertions.assertThrows(IllegalArgumentException.class, () -> TradeType.fromInt(3));

		Assertions.assertEquals("IMPORT", TradeType.IMPORT.toString());
		Assertions.assertEquals("EXPORT", TradeType.EXPORT.toString());
	}

	@Test
	public void testDF() {
		Assertions.assertEquals(DF.DOMESTIC, DF.valueOf("DOMESTIC"));
		Assertions.assertEquals(DF.FOREIGN, DF.valueOf("FOREIGN"));

	}

	@Test
	public void testSerialize() throws JsonProcessingException, ParseException {
		this.initializeExamples();

		ArrayList<Shipment> shipments = new ArrayList<>();
		shipments.add(shipmentA);
		Assertions.assertEquals(
				"[" + serializeShipment(shipmentA) + "]",
				FreightStatUtils.serializeObjects(shipments)
		);
		shipments.add(shipmentB);
		Assertions.assertEquals(
				"[" + serializeShipment(shipmentA) + "," + serializeShipment(shipmentB) + "]",
				FreightStatUtils.serializeObjects(shipments)
		);
		shipments.add(shipmentC);
		Assertions.assertEquals(
				"[" + serializeShipment(shipmentA) + "," + serializeShipment(shipmentB) + "," + serializeShipment(shipmentC) + "]",
				FreightStatUtils.serializeObjects(shipments)
		);

		ArrayList<CovidCase> cases = new ArrayList<>();
		cases.add(caseA);
		Assertions.assertEquals(
				"[" + serializeCase(caseA) + "]",
				FreightStatUtils.serializeObjects(cases)
		);
		cases.add(caseB);
		Assertions.assertEquals(
				"[" + serializeCase(caseA) + "," + serializeCase(caseB) + "]",
				FreightStatUtils.serializeObjects(cases)
		);
		cases.add(caseC);
		Assertions.assertEquals(
				"[" + serializeCase(caseA) + "," + serializeCase(caseB) + "," + serializeCase(caseC) + "]",
				FreightStatUtils.serializeObjects(cases)
		);
	}

	private String serializeShipment(Shipment s) {
		StringBuilder b = new StringBuilder();
		b.append("{\"id\":");
		b.append(s.getId());
		b.append(",\"tradeType\":\"");
		b.append(s.getTradeType().toString());
		b.append("\",\"commodityId\":");
		b.append(s.getCommodityId());
		b.append(",\"transportMethodId\":");
		b.append(s.getTransportMethodId());
		b.append(",\"source\":");
		b.append(s.getSource());
		b.append(",\"destination\":");
		b.append(s.getDestination());
		b.append(",\"value\":");
		b.append(s.getValue());
		b.append(",\"weight\":");
		b.append(s.getWeight());
		b.append(",\"freightCharges\":");
		b.append(s.getFreightCharges());
		b.append(",\"df\":\"");
		b.append(s.getDF().toString());
		b.append("\",\"containerized\":");
		b.append(s.getContainerized());
		b.append(",\"date\":\"");
		b.append(s.getDate().toString());
		b.append("\"}");
		return b.toString();
	}

	private String serializeCase(CovidCase c) {
		StringBuilder b = new StringBuilder();
		b.append("{\"id\":");
		b.append(c.getId());
		b.append(",\"positiveTests\":");
		b.append(c.getPositiveTests());
		b.append(",\"totalTests\":");
		b.append(c.getTotalTests());
		b.append(",\"testingRate\":");
		if (c.getTestingRate() == null) {
			b.append("null");
		} else {
			b.append(c.getTestingRate().get());
		}
		b.append(",\"date\":\"");
		b.append(c.getDate().toString());
		b.append("\",\"locationStateId\":");
		b.append(c.getLocationStateId());
		b.append("}");
		return b.toString();
	}
}
