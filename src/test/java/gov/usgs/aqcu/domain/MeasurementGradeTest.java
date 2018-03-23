package gov.usgs.aqcu.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import gov.usgs.aqcu.model.MeasurementGrade;

public class MeasurementGradeTest {

	@Test
	public void notValidTest() {
		assertEquals(MeasurementGrade.POOR, MeasurementGrade.valueFrom("wrong"));
	}

	@Test
	public void validTest() {
		assertEquals(MeasurementGrade.EXCELLENT, MeasurementGrade.valueFrom("EXCELLENT"));
	}

	@Test
	public void validLowerCaseTest() {
		assertEquals(MeasurementGrade.GOOD, MeasurementGrade.valueFrom("good"));
	}

	@Test
	public void percentageTest() {
		assertEquals(new BigDecimal("0.0500"), MeasurementGrade.GOOD.getPercentageOfError());
	}

}
