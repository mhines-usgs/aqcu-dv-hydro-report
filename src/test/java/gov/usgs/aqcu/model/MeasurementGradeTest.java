package gov.usgs.aqcu.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.MeasurementGradeType;

import gov.usgs.aqcu.model.MeasurementGrade;

public class MeasurementGradeTest {

	@Test
	public void notValidTest() {
		assertEquals(MeasurementGrade.POOR, MeasurementGrade.fromMeasurementGradeType(null));
	}

	@Test
	public void validTest() {
		assertEquals(MeasurementGrade.EXCELLENT, MeasurementGrade.fromMeasurementGradeType(MeasurementGradeType.Excellent));
	}

	@Test
	public void validLowerCaseTest() {
		assertEquals(MeasurementGrade.GOOD, MeasurementGrade.fromMeasurementGradeType(MeasurementGradeType.Good));
	}

	@Test
	public void percentageTest() {
		assertEquals(new BigDecimal("0.0500"), MeasurementGrade.GOOD.getPercentageOfError());
	}

}
