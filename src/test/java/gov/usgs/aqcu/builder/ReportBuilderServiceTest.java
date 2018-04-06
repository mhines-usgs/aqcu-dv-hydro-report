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
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.StatisticalTimeRange;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesPoint;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescription;

import gov.usgs.aqcu.ObjectCompare;
import gov.usgs.aqcu.model.DvHydrographPoint;
import gov.usgs.aqcu.model.DvHydrographReportMetadata;
import gov.usgs.aqcu.model.FieldVisitMeasurement;
import gov.usgs.aqcu.model.MeasurementGrade;
import gov.usgs.aqcu.model.TimeSeriesCorrectedData;
import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;
import gov.usgs.aqcu.retrieval.FieldVisitDataService;
import gov.usgs.aqcu.retrieval.FieldVisitDescriptionService;
import gov.usgs.aqcu.retrieval.LocationDescriptionService;
import gov.usgs.aqcu.retrieval.ParameterListService;
import gov.usgs.aqcu.retrieval.QualifierLookupService;
import gov.usgs.aqcu.retrieval.TimeSeriesDataCorrectedService;
import gov.usgs.aqcu.retrieval.TimeSeriesDescriptionService;

@RunWith(SpringRunner.class)
public class ReportBuilderServiceTest {
	public static final Instant REPORT_END_INSTANT = Instant.parse("2018-03-17T23:59:59.999999999Z");
	public static final Instant REPORT_START_INSTANT = Instant.parse("2018-03-16T00:00:00.00Z");
	public static final LocalDate REPORT_END_DATE = LocalDate.of(2018, 03, 17);
	public static final LocalDate REPORT_START_DATE = LocalDate.of(2018, 03, 16);
	public static final QualifierMetadata QUALIFIER_METADATA_A = new QualifierMetadata().setIdentifier("a");
	public static final QualifierMetadata QUALIFIER_METADATA_B = new QualifierMetadata().setIdentifier("b");
	public static final QualifierMetadata QUALIFIER_METADATA_C = new QualifierMetadata().setIdentifier("c");
	public static final QualifierMetadata QUALIFIER_METADATA_D = new QualifierMetadata().setIdentifier("d");

	@MockBean
	private DataGapListBuilderServiceII dataGapListBuilderService;
	@MockBean
	private FieldVisitDataService fieldVisitDataService;
	@MockBean
	private FieldVisitDescriptionService fieldVisitDescriptionService; 
	@MockBean
	private LocationDescriptionService locationDescriptionService;
	@MockBean
	private ParameterListService parameterListService;
	@MockBean
	private QualifierLookupService qualifierLookupService;
	@MockBean
	private TimeSeriesDataCorrectedService timeSeriesDataCorrectedService;
	@MockBean
	private TimeSeriesDescriptionService timeSeriesDescriptionService;

	private ReportBuilderService service;
	private Map<String, QualifierMetadata> metadataMap;
	private Instant nowInstant;
	private LocalDate nowLocalDate;

	@Before
	@SuppressWarnings("unchecked")
	public void setup() {
		service = new ReportBuilderService(dataGapListBuilderService,
				fieldVisitDataService,
				fieldVisitDescriptionService,
				locationDescriptionService,
				parameterListService,
				qualifierLookupService,
				timeSeriesDataCorrectedService,
				timeSeriesDescriptionService);
		metadataMap = buildQualifierMetadata();
		nowInstant = Instant.now();
		nowLocalDate = LocalDate.now();
		given(locationDescriptionService.getByLocationIdentifier(anyString())).willReturn(new LocationDescription().setIdentifier("0010010000").setName("monitoringLocation"));
		given(qualifierLookupService.getByQualifierList(anyList())).willReturn(metadataMap);
	}

	@Test
	public void getZoneOffsetErrorTest() {
		//TODO
	}

	@Test
	public void getTimezoneMinusTest() {
		assertEquals("Etc/GMT+4", service.getTimezone(Double.parseDouble("-4")));
		assertEquals("Etc/GMT+0", service.getTimezone(Double.parseDouble("0")));
	}

	@Test
	public void getTimezonePlusTest() {
		assertEquals("Etc/GMT-4", service.getTimezone(Double.parseDouble("4")));
	}

	@Test
	@SuppressWarnings("serial")
	public void createDvHydroMetadataTest() {
		TimeSeriesDataServiceResponse primarySeriesDataResponse = new TimeSeriesDataServiceResponse().setQualifiers(new ArrayList<Qualifier>() {});
		DvHydrographReportMetadata actual = service.createDvHydroMetadata(buildRequestParameters(), buildTimeSeriesDescriptions(), primarySeriesDataResponse, "testUser");

		assertThat(actual, samePropertyValuesAs(buildExpectedDvHydroMetadata()));
	}

	@Test
	public void buildFieldVisitMeasurements_nullTest() {
		given(fieldVisitDescriptionService.getDescriptions(anyString(), any(ZoneOffset.class), any(DvHydrographRequestParameters.class))).willReturn(null);
		List<FieldVisitMeasurement> actual = service.buildFieldVisitMeasurements(null, null, null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void buildFieldVisitMeasurements_loopTest() {
		List<FieldVisitDescription> visits = new ArrayList<FieldVisitDescription>();
		FieldVisitDescription visitA = new FieldVisitDescription();
		visitA.setIdentifier("a");
		visits.add(visitA);
		FieldVisitDescription visitB = new FieldVisitDescription();
		visitA.setIdentifier("b");
		visits.add(visitB);
		given(fieldVisitDescriptionService.getDescriptions(anyString(), any(ZoneOffset.class), any(DvHydrographRequestParameters.class))).willReturn(visits);
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
		List<FieldVisitMeasurement> actual = service.buildFieldVisitMeasurements(null, null, null);
		assertEquals(4, actual.size());
		verify(fieldVisitDataService, times(2)).get(anyString());
	}

	@Test
	public void createFieldVisitMeasurement_noDischargeActivitiesTest() {
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse());
		FieldVisitDescription visit = new FieldVisitDescription();
		List<FieldVisitMeasurement> actual = service.createFieldVisitMeasurements(visit);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void createFieldVisitMeasurement_noDischargeSummaryTest() {
		ArrayList<DischargeActivity> activities = new ArrayList<>();
		activities.add(new DischargeActivity());
		activities.add(new DischargeActivity());
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse()
				.setDischargeActivities(activities));
		FieldVisitDescription visit = new FieldVisitDescription();
		List<FieldVisitMeasurement> actual = service.createFieldVisitMeasurements(visit);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void createFieldVisitMeasurement_noDischargeTest() {
		ArrayList<DischargeActivity> activities = new ArrayList<>();
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()));
		activities.add(new DischargeActivity().setDischargeSummary(new DischargeSummary()));
		given(fieldVisitDataService.get(anyString())).willReturn(new FieldVisitDataServiceResponse()
				.setDischargeActivities(activities));
		FieldVisitDescription visit = new FieldVisitDescription();
		List<FieldVisitMeasurement> actual = service.createFieldVisitMeasurements(visit);
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
		FieldVisitDescription visit = new FieldVisitDescription();
		List<FieldVisitMeasurement> actual = service.createFieldVisitMeasurements(visit);
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
		FieldVisitMeasurement actual = service.getFieldVisitMeasurement(
				new DischargeSummary()
					.setMeasurementGrade(MeasurementGradeType.Good)
					.setMeasurementId("20.0090")
					.setDischarge((QuantityWithDisplay) new QuantityWithDisplay().setUnit("dischargeUnits").setDisplay("20.0090").setNumeric(Double.valueOf("20.0090")))
					.setMeanGageHeight((QuantityWithDisplay) new QuantityWithDisplay().setUnit("meanGageHeightUnits").setDisplay("2.0090").setNumeric(Double.valueOf("2.0090")))
					.setMeasurementStartTime(nowInstant)
					);
		FieldVisitMeasurement expected = new FieldVisitMeasurement("20.0090", new BigDecimal("20.0090"),
				new BigDecimal("21.00945000"), new BigDecimal("19.00855000"), nowInstant);
		assertThat(actual, samePropertyValuesAs(expected));
	}

	@Test
	public void calculateErrorTest() {
		Instant now = Instant.now();
		FieldVisitMeasurement expected = new FieldVisitMeasurement("20.0090", new BigDecimal("20.0090"),
				new BigDecimal("21.00945000"), new BigDecimal("19.00855000"), now);
		FieldVisitMeasurement actual = service.calculateError(MeasurementGrade.GOOD, "20.0090", new BigDecimal("20.0090"), now);

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
				.setDisplay(ReportBuilderService.GAP_MARKER_POINT_VALUE)
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
		//TODO more testing of start/end boundaries with 24 & 00
		DvHydrographPoint expected1 = new DvHydrographPoint();
		expected1.setTime(nowInstant.minusSeconds(3));
		expected1.setValue(new BigDecimal("654.321"));
		DvHydrographPoint expected2 = new DvHydrographPoint();
		expected2.setTime(nowInstant.minusSeconds(2));
		expected2.setValue(new BigDecimal("321.987"));
		DvHydrographPoint expected3 = new DvHydrographPoint();
		expected3.setTime(nowInstant);
		expected3.setValue(new BigDecimal("987.654"));
		DvHydrographRequestParameters requestParameters = new DvHydrographRequestParameters();
		requestParameters.setStartDate(nowLocalDate);
		requestParameters.setEndDate(nowLocalDate);
		List<DvHydrographPoint> actual = service.createDvHydroPoints(getTimeSeriesPoints(), requestParameters,
				true, ZoneOffset.UTC);
		assertThat(actual, containsInAnyOrder(samePropertyValuesAs(expected1),
				samePropertyValuesAs(expected2),
				samePropertyValuesAs(expected3)));
	}

	@Test
	public void createDvHydroCorrectedDataTest() {
		TimeSeriesDataServiceResponse tsd = new TimeSeriesDataServiceResponse()
				.setTimeRange(new StatisticalTimeRange()
						.setStartTime(new StatisticalDateTimeOffset().setDateTimeOffset(nowInstant).setRepresentsEndOfTimePeriod(true))
						.setEndTime(new StatisticalDateTimeOffset().setDateTimeOffset(nowInstant).setRepresentsEndOfTimePeriod(true))
						)
				.setUnit("myUnit")
				.setParameter("myParameter")
				.setPoints(getTimeSeriesPoints())
				.setQualifiers(new ArrayList<Qualifier>()); //TODO add qualifiers
		DvHydrographPoint expected1 = new DvHydrographPoint();
		expected1.setTime(nowInstant.minusSeconds(3));
		expected1.setValue(new BigDecimal("654.321"));
		DvHydrographPoint expected2 = new DvHydrographPoint();
		expected2.setTime(nowInstant.minusSeconds(2));
		expected2.setValue(new BigDecimal("321.987"));
		DvHydrographPoint expected3 = new DvHydrographPoint();
		expected3.setTime(nowInstant);
		expected3.setValue(new BigDecimal("987.654"));
		TimeSeriesCorrectedData expected = new TimeSeriesCorrectedData();
		expected.setPoints(Arrays.asList(expected1, expected2, expected3));
		expected.setType("myParameter");
		expected.setUnit("myUnit");
		expected.setEndTime(nowLocalDate.minusDays(1));
		expected.setStartTime(nowLocalDate.minusDays(1));
		expected.setVolumetricFlow(true);
		DvHydrographRequestParameters requestParameters = new DvHydrographRequestParameters();
		requestParameters.setStartDate(nowLocalDate);
		requestParameters.setEndDate(nowLocalDate);
		TimeSeriesCorrectedData actual = service.createDvHydroCorrectedData(tsd, requestParameters, true, true, ZoneOffset.UTC);
		ObjectCompare.assertDaoTestResults(TimeSeriesCorrectedData.class, expected, actual);
	}

	protected DvHydrographRequestParameters buildRequestParameters() {
		DvHydrographRequestParameters requestParameters = new DvHydrographRequestParameters();
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
		descriptions.put("a", new TimeSeriesDescription().setIdentifier("primaryIdentifier").setUtcOffset(Double.valueOf(-4)));
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

	protected DvHydrographReportMetadata buildExpectedDvHydroMetadata() {
		DvHydrographReportMetadata expected = new DvHydrographReportMetadata();
		expected.setPrimarySeriesLabel("primaryIdentifier");
		expected.setTimezone("Etc/GMT+4");
		expected.setStartDate(REPORT_START_INSTANT);
		expected.setEndDate(REPORT_END_INSTANT);
		expected.setTitle("DV Hydrograph");
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
		metadata.put("a", QUALIFIER_METADATA_A);
		metadata.put("b", QUALIFIER_METADATA_B);
		metadata.put("c", QUALIFIER_METADATA_C);
		metadata.put("d", QUALIFIER_METADATA_D);
		return metadata;
	}

	protected ArrayList<TimeSeriesPoint> getTimeSeriesPoints() {
		ArrayList<TimeSeriesPoint> timeSeriesPoints = new ArrayList<>();
		timeSeriesPoints.add(new TimeSeriesPoint()
				.setValue(new DoubleWithDisplay()
						.setDisplay("654.321")
						.setNumeric(Double.valueOf("123.456")))
				.setTimestamp(new StatisticalDateTimeOffset()
						.setDateTimeOffset(nowInstant.minusSeconds(3))
						.setRepresentsEndOfTimePeriod(false)));
		timeSeriesPoints.add(new TimeSeriesPoint()
				.setValue(new DoubleWithDisplay()
						.setDisplay("321.987")
						.setNumeric(Double.valueOf("789.123")))
				.setTimestamp(new StatisticalDateTimeOffset()
						.setDateTimeOffset(nowInstant.minusSeconds(2))
						.setRepresentsEndOfTimePeriod(false)));
		timeSeriesPoints.add(new TimeSeriesPoint()
				.setValue(new DoubleWithDisplay()
						.setDisplay("987.654")
						.setNumeric(Double.valueOf("456.789")))
				.setTimestamp(new StatisticalDateTimeOffset()
						.setDateTimeOffset(nowInstant)
						.setRepresentsEndOfTimePeriod(false)));
		return timeSeriesPoints;
	}

}
