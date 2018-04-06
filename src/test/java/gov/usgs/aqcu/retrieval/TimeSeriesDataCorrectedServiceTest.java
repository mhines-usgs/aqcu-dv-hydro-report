package gov.usgs.aqcu.retrieval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;

import gov.usgs.aqcu.builder.ReportBuilderServiceTest;
import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;
import net.servicestack.client.IReturn;

@RunWith(SpringRunner.class)
public class TimeSeriesDataCorrectedServiceTest {

	@MockBean
	private AquariusRetrievalService aquariusService;

	private TimeSeriesDataCorrectedService service;
	private DvHydrographRequestParameters parameters;

	private Qualifier qualifierA = new Qualifier().setIdentifier("a");
	private Qualifier qualifierB = new Qualifier().setIdentifier("b");
	private Qualifier qualifierC = new Qualifier().setIdentifier("c");

	private int secondsInDay = 60 * 60 * 24;

	@Before
	@SuppressWarnings("unchecked")
	public void setup() throws Exception {
		service = new TimeSeriesDataCorrectedService(aquariusService);
		parameters = new DvHydrographRequestParameters();
		given(aquariusService.executePublishApiRequest(any(IReturn.class))).willReturn(new TimeSeriesDataServiceResponse()
				.setQualifiers(new ArrayList<Qualifier>(Arrays.asList(qualifierA, qualifierB, qualifierC))));
	}

	@Test
	public void getTest() throws Exception {
		TimeSeriesDataServiceResponse actual = service.get("", null, null);
		assertEquals(3, actual.getQualifiers().size());
		assertThat(actual.getQualifiers(), containsInAnyOrder(qualifierA, qualifierB, qualifierC));
	}

	@Test
	public void get_Test() throws Exception {
		parameters.setStartDate(ReportBuilderServiceTest.REPORT_START_DATE);
		parameters.setEndDate(ReportBuilderServiceTest.REPORT_END_DATE);
		TimeSeriesDataServiceResponse actual = service.get(parameters.getPrimaryTimeseriesIdentifier(), parameters, true, ZoneOffset.UTC);
		assertEquals(3, actual.getQualifiers().size());
		assertThat(actual.getQualifiers(), containsInAnyOrder(qualifierA, qualifierB, qualifierC));
	}

	@Test
	public void adjustIfDVTest () {
		Instant now = Instant.now();
		Instant tomorrow = now.plusSeconds(secondsInDay);
		assertEquals(now, service.adjustIfDv(now, false));
		assertEquals(tomorrow, service.adjustIfDv(now, true));
	}
}
