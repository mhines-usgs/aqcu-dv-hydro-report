package gov.usgs.aqcu.retrieval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;

import gov.usgs.aqcu.builder.DvHydroReportBuilderServiceTest;
import gov.usgs.aqcu.parameter.DvHydroRequestParameters;
import net.servicestack.client.IReturn;

@RunWith(SpringRunner.class)
public class TimeSeriesDataCorrectedServiceTest {

	@MockBean
	private AquariusRetrievalService aquariusService;

	private TimeSeriesDataCorrectedService service;
	private DvHydroRequestParameters parameters;

	private Qualifier qualifierA = new Qualifier().setIdentifier("a");
	private Qualifier qualifierB = new Qualifier().setIdentifier("b");
	private Qualifier qualifierC = new Qualifier().setIdentifier("c");

	@Before
	@SuppressWarnings("unchecked")
	public void setup() throws Exception {
		service = new TimeSeriesDataCorrectedService(aquariusService);
		parameters = new DvHydroRequestParameters();
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
	public void getTimeSeriesDescriptions_happyTest() {
		parameters.setStartDate(DvHydroReportBuilderServiceTest.REPORT_START_DATE);
		parameters.setEndDate(DvHydroReportBuilderServiceTest.REPORT_END_DATE);
		List<Qualifier> actual = service.getQualifiers(parameters);
		assertEquals(3, actual.size());
		assertThat(actual, containsInAnyOrder(qualifierA, qualifierB, qualifierC));
	}

}
