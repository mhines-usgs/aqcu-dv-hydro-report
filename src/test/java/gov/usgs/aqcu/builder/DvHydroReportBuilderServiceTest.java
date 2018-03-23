package gov.usgs.aqcu.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.ControlConditionActivity;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.ControlConditionType;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DischargeActivity;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DischargeSummary;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DoubleWithDisplay;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.MeasurementGradeType;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierMetadata;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QuantityWithDisplay;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.StatisticalDateTimeOffset;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesPoint;

import gov.usgs.aqcu.model.AqcuFieldVisit;
import gov.usgs.aqcu.model.AqcuFieldVisitMeasurement;
import gov.usgs.aqcu.model.AqcuPoint;
import gov.usgs.aqcu.model.DvHydroCorrectedData;
import gov.usgs.aqcu.model.DvHydroMetadata;
import gov.usgs.aqcu.model.MeasurementGrade;
import gov.usgs.aqcu.parameter.DvHydroRequestParameters;
import gov.usgs.aqcu.retrieval.FieldVisitDataService;
import gov.usgs.aqcu.retrieval.FieldVisitDescriptionService;
import gov.usgs.aqcu.retrieval.LocationDescriptionService;
import gov.usgs.aqcu.retrieval.QualifierLookupService;
import gov.usgs.aqcu.retrieval.QualifierLookupServiceTest;
import gov.usgs.aqcu.retrieval.TimeSeriesDataCorrectedService;
import gov.usgs.aqcu.retrieval.TimeSeriesDescriptionService;

@RunWith(SpringRunner.class)
public class DvHydroReportBuilderServiceTest {
	public static final Instant REPORT_END_INSTANT = Instant.parse("2018-03-17T23:59:59.999999999Z");
	public static final Instant REPORT_START_INSTANT = Instant.parse("2018-03-16T00:00:00.00Z");
	public static final LocalDate REPORT_END_DATE = LocalDate.of(2018, 03, 17);
	public static final LocalDate REPORT_START_DATE = LocalDate.of(2018, 03, 16);

	@MockBean
	private TimeSeriesDataCorrectedService timeSeriesDataCorrectedService;
	@MockBean
	private TimeSeriesDescriptionService timeSeriesDescriptionListService;
	@MockBean
	private LocationDescriptionService locationDescriptionListService;
	@MockBean
	private QualifierLookupService qualifierLookupService;
	@MockBean
	private FieldVisitDescriptionService fieldVisitDescriptionListService; 
	@MockBean
	private FieldVisitDataService fieldVisitDataService;

	private DvHydroReportBuilderService service;
	private Map<String, QualifierMetadata> metadataMap;
	private Instant now;

	@Before
	@SuppressWarnings("unchecked")
	public void setup() {
		service = new DvHydroReportBuilderService(timeSeriesDataCorrectedService,
				timeSeriesDescriptionListService,
				locationDescriptionListService,
				qualifierLookupService,
				fieldVisitDescriptionListService,
				fieldVisitDataService);
		metadataMap = buildQualifierMetadata();
		now = Instant.now();
		given(locationDescriptionListService.getByLocationIdentifier(anyString())).willReturn(new LocationDescription().setIdentifier("0010010000").setName("monitoringLocation"));
		given(qualifierLookupService.getByQualifierList(anyList())).willReturn(metadataMap);
	}

	@Test
	@SuppressWarnings("serial")
	public void createDvHydroMetadataTest() {
		TimeSeriesDataServiceResponse primarySeriesDataResponse = new TimeSeriesDataServiceResponse().setQualifiers(new ArrayList<Qualifier>() {});
		DvHydroMetadata actual = service.createDvHydroMetadata(buildRequestParameters(), buildTimeSeriesDescriptions(), primarySeriesDataResponse, "testUser");

//		assertThat(actual, samePropertyValuesAs(buildExpectedDvHydroMetadata()));
	}

	@Test
	public void buildFieldVisitMeasurements_nullTest() {
		given(fieldVisitDescriptionListService.getDescriptions(anyString(), any(DvHydroRequestParameters.class))).willReturn(null);
		List<AqcuFieldVisitMeasurement> actual = service.buildFieldVisitMeasurements(null, null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void buildFieldVisitMeasurements_loopTest() {
		ArrayList<AqcuFieldVisit> visits = new ArrayList<AqcuFieldVisit>();
		AqcuFieldVisit visitA = new AqcuFieldVisit();
		visitA.setIdentifier("a");
		visits.add(visitA);
		AqcuFieldVisit visitB = new AqcuFieldVisit();
		visitA.setIdentifier("b");
		visits.add(visitB);
		given(fieldVisitDescriptionListService.getDescriptions(anyString(), any(DvHydroRequestParameters.class))).willReturn(visits);
		ArrayList<DischargeActivity> activities = new ArrayList<>();
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()
				.setMeasurementGrade(MeasurementGradeType.Good)
				.setMeanGageHeight((QuantityWithDisplay) new QuantityWithDisplay().setUnit("meanGageHeightUnits").setDisplay("2.0090").setNumeric(Double.valueOf("2.0090")))
				.setDischarge((QuantityWithDisplay) new QuantityWithDisplay().setUnit("dischargeUnits").setDisplay("20.0090").setNumeric(Double.valueOf("20.0090")))));
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()
				.setMeasurementGrade(MeasurementGradeType.Excellent)
				.setMeanGageHeight((QuantityWithDisplay) new QuantityWithDisplay().setUnit("meanGageHeightUnits").setDisplay("2.0090").setNumeric(Double.valueOf("2.0090")))
				.setDischarge((QuantityWithDisplay) new QuantityWithDisplay().setUnit("dischargeUnits").setDisplay("20.0090").setNumeric(Double.valueOf("20.0090")))));
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse()
				.setDischargeActivities(activities));
		List<AqcuFieldVisitMeasurement> actual = service.buildFieldVisitMeasurements(null, null);
		assertEquals(4, actual.size());
		verify(fieldVisitDataService, times(2)).get(anyString());
	}

	@Test
	public void createFieldVisitMeasurement_noDischargeActivitiesTest() {
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse());
		AqcuFieldVisit visit = new AqcuFieldVisit();
		List<AqcuFieldVisitMeasurement> actual = service.createFieldVisitMeasurement(visit);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void createFieldVisitMeasurement_noDischargeSummaryTest() {
		ArrayList<DischargeActivity> activities = new ArrayList<>();
		activities.add(new DischargeActivity());
		activities.add(new DischargeActivity());
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse()
				.setDischargeActivities(activities));
		AqcuFieldVisit visit = new AqcuFieldVisit();
		List<AqcuFieldVisitMeasurement> actual = service.createFieldVisitMeasurement(visit);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void createFieldVisitMeasurement_noDischargeTest() {
		ArrayList<DischargeActivity> activities = new ArrayList<>();
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()));
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()));
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse()
				.setDischargeActivities(activities));
		AqcuFieldVisit visit = new AqcuFieldVisit();
		List<AqcuFieldVisitMeasurement> actual = service.createFieldVisitMeasurement(visit);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void createFieldVisitMeasurement_happyTest() {
		ArrayList<DischargeActivity> activities = new ArrayList<>();
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()
				.setMeasurementGrade(MeasurementGradeType.Good)
				.setMeanGageHeight((QuantityWithDisplay) new QuantityWithDisplay().setUnit("meanGageHeightUnits").setDisplay("2.0090").setNumeric(Double.valueOf("2.0090")))
				.setDischarge((QuantityWithDisplay) new QuantityWithDisplay().setUnit("dischargeUnits").setDisplay("20.0090").setNumeric(Double.valueOf("20.0090")))));
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()
				.setMeasurementGrade(MeasurementGradeType.Excellent)
				.setMeanGageHeight((QuantityWithDisplay) new QuantityWithDisplay().setUnit("meanGageHeightUnits").setDisplay("2.0090").setNumeric(Double.valueOf("2.0090")))
				.setDischarge((QuantityWithDisplay) new QuantityWithDisplay().setUnit("dischargeUnits").setDisplay("20.0090").setNumeric(Double.valueOf("20.0090")))));
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse()
				.setDischargeActivities(activities));
		AqcuFieldVisit visit = new AqcuFieldVisit();
		List<AqcuFieldVisitMeasurement> actual = service.createFieldVisitMeasurement(visit);
		assertEquals(2, actual.size());
		//Nice To Have - compare the two objects to expected values.
	}

	@Test
	public void getControlCondition_nullActivityTest() {
		FieldVisitDataServiceResponse resp = new FieldVisitDataServiceResponse();
		assertNull(service.getControlCondition(resp));
	}

	@Test
	public void getControlCondition_nullConditionTest() {
		FieldVisitDataServiceResponse resp = new FieldVisitDataServiceResponse()
				.setControlConditionActivity(new ControlConditionActivity());
		assertNull(service.getControlCondition(resp));
	}

	@Test
	public void getControlCondition_happyTest() {
		FieldVisitDataServiceResponse resp = new FieldVisitDataServiceResponse()
				.setControlConditionActivity(new ControlConditionActivity().setControlCondition(ControlConditionType.DebrisHeavy));
		assertEquals("DebrisHeavy", service.getControlCondition(resp));
	}

	@Test
	public void getFieldVisitMeasurementTest() {
		AqcuFieldVisit visit = new AqcuFieldVisit();
		visit.setIdentifier("fieldVisitIdentifier");
		AqcuFieldVisitMeasurement actual = service.getFieldVisitMeasurement(visit, "controlCondition",
				new DischargeSummary()
					.setMeasurementGrade(MeasurementGradeType.Good)
					.setMeasurementId("20.0090")
					.setDischarge((QuantityWithDisplay) new QuantityWithDisplay().setUnit("dischargeUnits").setDisplay("20.0090").setNumeric(Double.valueOf("20.0090")))
					.setMeanGageHeight((QuantityWithDisplay) new QuantityWithDisplay().setUnit("meanGageHeightUnits").setDisplay("2.0090").setNumeric(Double.valueOf("2.0090")))
					.setMeasurementStartTime(now)
					);
		AqcuFieldVisitMeasurement expected = new AqcuFieldVisitMeasurement("20.0090", "controlCondition", new BigDecimal("20.0090"), "dischargeUnits", "meanGageHeightUnits",
				new BigDecimal("21.00945000"), new BigDecimal("19.00855000"), MeasurementGrade.GOOD, now, "fieldVisitIdentifier");
		expected.setMeanGageHeight(new BigDecimal("2.0090"));
		assertThat(actual, samePropertyValuesAs(expected));
	}

	@Test
	public void calculateErrorTest() {
		Instant now = Instant.now();
		AqcuFieldVisitMeasurement expected = new AqcuFieldVisitMeasurement("20.0090", "controlCondition", new BigDecimal("20.0090"), "dischargeUnits", "meanGageHeightUnits",
				new BigDecimal("21.00945000"), new BigDecimal("19.00855000"), MeasurementGrade.GOOD, now, "fieldVisitIdentifier");
		AqcuFieldVisitMeasurement actual = service.calculateError(MeasurementGrade.GOOD, "20.0090", "controlCondition", new BigDecimal("20.0090"), "dischargeUnits", "meanGageHeightUnits", now, "fieldVisitIdentifier");

		assertThat(actual, samePropertyValuesAs(expected));
	}

	@Test
	public void getRoundedValue_NullTest() {
		assertNull(service.getRoundedValue(null));
	}

	@Test
	public void getRoundedValue_NullDisplayAndNumericTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay().setDisplay(null).setNumeric(null);
		assertNull(service.getRoundedValue(dwd));
	}

	@Test
	public void getRoundedValue_NullDisplayTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay().setDisplay(null).setNumeric(Double.valueOf("123.456"));
		assertEquals(new BigDecimal("123.456"), service.getRoundedValue(dwd));
	}

	@Test
	public void getRoundedValue_GapDisplayTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay()
				.setDisplay(DvHydroReportBuilderService.GAP_MARKER_POINT_VALUE)
				.setNumeric(Double.valueOf("123.456"));
		assertEquals(new BigDecimal("123.456"), service.getRoundedValue(dwd));
	}

	@Test
	public void getRoundedValue_DisplayTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay()
				.setDisplay("654.321")
				.setNumeric(Double.valueOf("123.456"));
		assertEquals(new BigDecimal("654.321"), service.getRoundedValue(dwd));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void createDvHydroPointsTest() {
		AqcuPoint expected1 = new AqcuPoint();
		expected1.setTime(now.minusSeconds(3));
		expected1.setValue(new BigDecimal("654.321"));
		AqcuPoint expected2 = new AqcuPoint();
		expected2.setTime(now.minusSeconds(2));
		expected2.setValue(new BigDecimal("321.987"));
		AqcuPoint expected3 = new AqcuPoint();
		expected3.setTime(now);
		expected3.setValue(new BigDecimal("987.654"));
		List<AqcuPoint> actual = service.createDvHydroPoints(getTimeSeriesPoints());
		assertThat(actual, containsInAnyOrder(samePropertyValuesAs(expected1),
				samePropertyValuesAs(expected2),
				samePropertyValuesAs(expected3)));
	}

	@Test
	public void createDvHydroCorrectedDataTest() {
		TimeSeriesDataServiceResponse tsd = new TimeSeriesDataServiceResponse()
				.setUnit("myUnit")
				.setParameter("myParameter")
				.setPoints(getTimeSeriesPoints());
		AqcuPoint expected1 = new AqcuPoint();
		expected1.setTime(now.minusSeconds(3));
		expected1.setValue(new BigDecimal("654.321"));
		AqcuPoint expected2 = new AqcuPoint();
		expected2.setTime(now.minusSeconds(2));
		expected2.setValue(new BigDecimal("321.987"));
		AqcuPoint expected3 = new AqcuPoint();
		expected3.setTime(now);
		expected3.setValue(new BigDecimal("987.654"));
		DvHydroCorrectedData expected = new DvHydroCorrectedData();
		expected.setPoints(Arrays.asList(expected1, expected2, expected3));
		expected.setType("myParameter");
		expected.setUnit("myUnit");
		DvHydroCorrectedData actual = service.createDvHydroCorrectedData(tsd, now, now);
		assertThat(actual, samePropertyValuesAs(expected));
	}

	protected DvHydroRequestParameters buildRequestParameters() {
		DvHydroRequestParameters requestParameters = new DvHydroRequestParameters();
		requestParameters.setPrimaryTimeseriesIdentifier("a");
		requestParameters.setFirstStatDerivedIdentifier("b");
		requestParameters.setSecondStatDerivedIdentifier("c");
		requestParameters.setThirdStatDerivedIdentifier("d");
		requestParameters.setFourthStatDerivedIdentifier("e");
		requestParameters.setFirstReferenceIdentifier("f");
		requestParameters.setSecondReferenceIdentifier("g");
		requestParameters.setThirdReferenceIdentifier("h");
		requestParameters.setComparisonTimeseriesIdentifier("i");
		requestParameters.setStartDate(REPORT_START_DATE);
		requestParameters.setEndDate(REPORT_END_DATE);
		return requestParameters;
	}

	protected Map<String, TimeSeriesDescription> buildTimeSeriesDescriptions() {
		Map<String, TimeSeriesDescription> descriptions = new HashMap<>();
		descriptions.put("a", new TimeSeriesDescription().setIdentifier("primaryIdentifier").setUtcOffset(Double.valueOf(4)));
		descriptions.put("b", new TimeSeriesDescription().setIdentifier("firstStatDerived"));
		descriptions.put("c", new TimeSeriesDescription().setIdentifier("secondStatDerived"));
		descriptions.put("d", new TimeSeriesDescription().setIdentifier("thirdStatDerived"));
		descriptions.put("e", new TimeSeriesDescription().setIdentifier("fourthStatDerived"));
		descriptions.put("f", new TimeSeriesDescription().setIdentifier("firstRefTS"));
		descriptions.put("g", new TimeSeriesDescription().setIdentifier("secondRefTS"));
		descriptions.put("h", new TimeSeriesDescription().setIdentifier("thirdRefTS"));
		descriptions.put("i", new TimeSeriesDescription().setIdentifier("comparison"));
		return descriptions;
	};

	protected DvHydroMetadata buildExpectedDvHydroMetadata() {
		DvHydroMetadata expected = new DvHydroMetadata();
		expected.setRequestingUser("testUser");
		expected.setTimezone("Etc/GMT-4");
		expected.setStartDate(REPORT_START_INSTANT);
		expected.setEndDate(REPORT_END_INSTANT);
		expected.setTitle("DV Hydrograph");
		expected.setPrimarySeriesLabel("primaryIdentifier");
		expected.setFirstStatDerivedLabel("firstStatDerived");
		expected.setSecondStatDerivedLabel("secondStatDerived");
		expected.setThirdStatDerivedLabel("thirdStatDerived");
		expected.setFourthStatDerivedLabel("fourthStatDerived");
		expected.setFirstReferenceTimeSeriesLabel("firstRefTS");
		expected.setSecondReferenceTimeSeriesLabel("secondRefTS");
		expected.setThirdReferenceTimeSeriesLabel("thirdRefTS");
		expected.setComparisonSeriesLabel("comparison");
		expected.setQualifierMetadata(metadataMap);
		expected.setStationName("monitoringLocation");
		expected.setStationId("0010010000");
		return expected;
	}

	protected Map<String, QualifierMetadata> buildQualifierMetadata() {
		Map<String, QualifierMetadata> metadata = new HashMap<>();
		metadata.put("a", QualifierLookupServiceTest.QUALIFIER_METADATA_A);
		metadata.put("b", QualifierLookupServiceTest.QUALIFIER_METADATA_B);
		metadata.put("c", QualifierLookupServiceTest.QUALIFIER_METADATA_C);
		metadata.put("d", QualifierLookupServiceTest.QUALIFIER_METADATA_D);
		return metadata;
	}

	protected ArrayList<TimeSeriesPoint> getTimeSeriesPoints() {
		ArrayList<TimeSeriesPoint> timeSeriesPoints = new ArrayList<>();
		timeSeriesPoints.add(new TimeSeriesPoint()
				.setValue(new DoubleWithDisplay()
						.setDisplay("654.321")
						.setNumeric(Double.valueOf("123.456")))
				.setTimestamp(new StatisticalDateTimeOffset()
						.setDateTimeOffset(now.minusSeconds(3))));
		timeSeriesPoints.add(new TimeSeriesPoint()
				.setValue(new DoubleWithDisplay()
						.setDisplay("321.987")
						.setNumeric(Double.valueOf("789.123")))
				.setTimestamp(new StatisticalDateTimeOffset()
						.setDateTimeOffset(now.minusSeconds(2))));
		timeSeriesPoints.add(new TimeSeriesPoint()
				.setValue(new DoubleWithDisplay()
						.setDisplay("987.654")
						.setNumeric(Double.valueOf("456.789")))
				.setTimestamp(new StatisticalDateTimeOffset()
						.setDateTimeOffset(now)));
		return timeSeriesPoints;
	}

}
