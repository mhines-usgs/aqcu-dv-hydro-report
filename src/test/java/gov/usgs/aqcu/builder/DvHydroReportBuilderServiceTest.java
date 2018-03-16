package gov.usgs.aqcu.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierMetadata;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;

import gov.usgs.aqcu.model.DvHydroMetadata;
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
		given(locationDescriptionListService.getByLocationIdentifier(anyString())).willReturn(new LocationDescription().setIdentifier("0010010000").setName("monitoringLocation"));
		given(qualifierLookupService.getByQualifierList(anyList())).willReturn(metadataMap);
	}

	@Test
	public void createDvHydroMetadataTest() {
		DvHydroMetadata actual = service.createDvHydroMetadata(buildRequestParameters(), buildTimeSeriesDescriptions(), "testUser");

		assertThat(actual, samePropertyValuesAs(buildExpectedDvHydroMetadata()));
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
		expected.setPrimaryParameter("primaryIdentifier");
		expected.setFirstStatDerivedParameter("firstStatDerived");
		expected.setSecondStatDerivedParameter("secondStatDerived");
		expected.setThirdStatDerivedParameter("thirdStatDerived");
		expected.setFourthStatDerivedParameter("fourthStatDerived");
		expected.setFirstReferenceTimeSeriesParameter("firstRefTS");
		expected.setSecondReferenceTimeSeriesParameter("secondRefTS");
		expected.setThirdReferenceTimeSeriesParameter("thirdRefTS");
		expected.setComparisonSeriesParameter("comparison");
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

}
