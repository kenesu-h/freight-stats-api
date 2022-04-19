package com.github.kenesu_h.freight_stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kenesu_h.freight_stats.common.CovidCase;
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

	private CovidCase caseA;
	private String serializedCaseA;

	private CovidCase caseB;
	private String serializedCaseB;

	private void initializeExamples() throws ParseException {
		controller = new FreightStatsController();
		df = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;

		caseA = new CovidCase(1, 2, 3, null, LocalDate.parse("2020-04-19", df), 1);
		serializedCaseA = "{\"id\":1,\"positiveTests\":2,\"totalTests\":3,\"testingRate\":null,\"date\":\"2020-04-19\",\"locationStateId\":1}";

		caseB = new CovidCase(2, 100, 200, Optional.of(5), LocalDate.parse("2020-05-20", df), 2);
		serializedCaseB = "{\"id\":2,\"positiveTests\":100,\"totalTests\":200,\"testingRate\":5,\"date\":\"2020-05-20\",\"locationStateId\":2}";
	}

	@Test
	public void testSerialize() throws JsonProcessingException, ParseException {
		this.initializeExamples();

		ArrayList<CovidCase> cases = new ArrayList<>();
		cases.add(caseA);
		Assertions.assertEquals(
				"[" + serializedCaseA + "]",
				controller.serializeObjects(cases)
		);
		cases.add(caseB);
		Assertions.assertEquals(
				"[" + serializedCaseA + "," + serializedCaseB + "]",
				controller.serializeObjects(cases)
		);
	}

}
