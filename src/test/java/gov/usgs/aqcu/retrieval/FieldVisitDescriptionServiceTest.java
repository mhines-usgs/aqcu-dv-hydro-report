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

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescriptionListServiceResponse;

import gov.usgs.aqcu.builder.DvHydroReportBuilderServiceTest;
import gov.usgs.aqcu.parameter.DvHydroRequestParameters;
import net.servicestack.client.IReturn;

@RunWith(SpringRunner.class)
public class FieldVisitDescriptionServiceTest {

	@MockBean
	private AquariusRetrievalService aquariusService;

	private FieldVisitDescriptionService service;
	private DvHydroRequestParameters parameters;

	private FieldVisitDescription descriptionA = new FieldVisitDescription().setIdentifier("a");
	private FieldVisitDescription descriptionB = new FieldVisitDescription().setIdentifier("b");
	private FieldVisitDescription descriptionC = new FieldVisitDescription().setIdentifier("c");

	@Before
	@SuppressWarnings("unchecked")
	public void setup() throws Exception {
		service = new FieldVisitDescriptionService(aquariusService);
		parameters = new DvHydroRequestParameters();
		given(aquariusService.executePublishApiRequest(any(IReturn.class))).willReturn(new FieldVisitDescriptionListServiceResponse()
				.setFieldVisitDescriptions(new ArrayList<FieldVisitDescription>(Arrays.asList(descriptionA, descriptionB, descriptionC))));
	}

	@Test
	public void getTest() throws Exception {
		FieldVisitDescriptionListServiceResponse actual = service.get("", null, null);
		assertEquals(3, actual.getFieldVisitDescriptions().size());
		assertThat(actual.getFieldVisitDescriptions(), containsInAnyOrder(descriptionA, descriptionB, descriptionC));
	}

	@Test
	public void getTimeSeriesDescriptions_happyTest() {
		parameters.setStartDate(DvHydroReportBuilderServiceTest.REPORT_START_DATE);
		parameters.setEndDate(DvHydroReportBuilderServiceTest.REPORT_END_DATE);
		List<FieldVisitDescription> actual = service.getDescriptions(parameters);
		assertEquals(3, actual.size());
		assertThat(actual, containsInAnyOrder(descriptionA, descriptionB, descriptionC));
	}

}
