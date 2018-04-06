package gov.usgs.aqcu.retrieval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescriptionListByUniqueIdServiceResponse;

import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;
import net.servicestack.client.IReturn;

@RunWith(SpringRunner.class)
public class TimeSeriesDescriptionServiceTest {

	@MockBean
	private AquariusRetrievalService aquariusService;

	private TimeSeriesDescriptionService service;
	private DvHydrographRequestParameters parameters;

	private TimeSeriesDescription timeSeriesDescriptionA = new TimeSeriesDescription().setUniqueId("a");
	private TimeSeriesDescription timeSeriesDescriptionB = new TimeSeriesDescription().setUniqueId("b");
	private TimeSeriesDescription timeSeriesDescriptionC = new TimeSeriesDescription().setUniqueId("c");

	@Before
	@SuppressWarnings("unchecked")
	public void setup() throws Exception {
		service = new TimeSeriesDescriptionService(aquariusService);
		parameters = new DvHydrographRequestParameters();
		given(aquariusService.executePublishApiRequest(any(IReturn.class))).willReturn(new TimeSeriesDescriptionListByUniqueIdServiceResponse()
				.setTimeSeriesDescriptions(new ArrayList<TimeSeriesDescription>(Arrays.asList(timeSeriesDescriptionA, timeSeriesDescriptionB, timeSeriesDescriptionC))));
	}

	@Test
	public void buildUniqueIdentifierList_nullParametersTest() {
		assertEquals(0, service.buildUniqueIdentifierList(parameters).size());
	}

	@Test
	public void buildUniqueIdentifierList_emptyParametersTest() {
		parameters.setPrimaryTimeseriesIdentifier("");
		parameters.setFirstStatDerivedIdentifier("");
		parameters.setSecondStatDerivedIdentifier("");
		parameters.setThirdStatDerivedIdentifier("");
		parameters.setFourthStatDerivedIdentifier("");
		parameters.setFirstReferenceIdentifier("");
		parameters.setSecondReferenceIdentifier("");
		parameters.setThirdReferenceIdentifier("");
		parameters.setComparisonTimeseriesIdentifier("");

		assertEquals(0, service.buildUniqueIdentifierList(parameters).size());
	}

	@Test
	public void buildUniqueIdentifierList_allParametersTest() {
		parameters.setPrimaryTimeseriesIdentifier("a");
		parameters.setFirstStatDerivedIdentifier("b");
		parameters.setSecondStatDerivedIdentifier("c");
		parameters.setThirdStatDerivedIdentifier("d");
		parameters.setFourthStatDerivedIdentifier("e");
		parameters.setFirstReferenceIdentifier("f");
		parameters.setSecondReferenceIdentifier("g");
		parameters.setThirdReferenceIdentifier("h");
		parameters.setComparisonTimeseriesIdentifier("i");

		ArrayList<String> actual = service.buildUniqueIdentifierList(parameters);
		assertEquals(9, actual.size());
		assertThat(actual, containsInAnyOrder("a", "b", "c", "d", "e", "f", "g", "h", "i"));
	}

	@Test
	public void buildUniqueIdentifierList_someDuplicateParametersTest() {
		parameters.setPrimaryTimeseriesIdentifier("a");
		parameters.setFirstStatDerivedIdentifier("b");
		parameters.setSecondStatDerivedIdentifier("a");
		parameters.setThirdStatDerivedIdentifier("d");
		parameters.setFourthStatDerivedIdentifier("b");
		parameters.setFirstReferenceIdentifier("f");
		parameters.setSecondReferenceIdentifier("g");
		parameters.setThirdReferenceIdentifier("d");
		parameters.setComparisonTimeseriesIdentifier("i");

		ArrayList<String> actual = service.buildUniqueIdentifierList(parameters);
		assertEquals(6, actual.size());
		assertThat(actual, containsInAnyOrder("a", "b", "d", "f", "g", "i"));
	}

	@Test
	public void getTest() throws Exception {
		List<TimeSeriesDescription> descriptionList = service.get(new ArrayList<String>(Arrays.asList("a", "b", "c")));
		assertEquals(3, descriptionList.size());
		assertThat(descriptionList, containsInAnyOrder(timeSeriesDescriptionA, timeSeriesDescriptionB, timeSeriesDescriptionC));
	}

	@Test
	public void buildDescriptionMap_invalidTest() {
		try {
			service.buildDescriptionMap(Arrays.asList("a", "b"), buildDescriptionList());
			fail("Expected an exception and didn't get it.");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("Failed to fetch descriptions for all requested Time Series Identifiers: \nRequested: 2\nGot: 3", e.getMessage());
		}
	}

	@Test
	public void buildDescriptionMap_happyTest() {
		Map<String, TimeSeriesDescription> descriptionList = service.buildDescriptionMap(Arrays.asList("a", "b", "c"), buildDescriptionList());
		assertEquals(3, descriptionList.size());
		assertThat(descriptionList, IsMapContaining.hasEntry("a", timeSeriesDescriptionA));
		assertThat(descriptionList, IsMapContaining.hasEntry("b", timeSeriesDescriptionB));
		assertThat(descriptionList, IsMapContaining.hasEntry("c", timeSeriesDescriptionC));
	}

	@Test
	public void getTimeSeriesDescriptions_happyTest() {
		parameters.setPrimaryTimeseriesIdentifier("a");
		parameters.setFirstStatDerivedIdentifier("b");
		parameters.setSecondStatDerivedIdentifier("c");
		Map<String, TimeSeriesDescription> descriptionList = service.getTimeSeriesDescriptions(parameters);
		assertEquals(3, descriptionList.size());
		assertThat(descriptionList, IsMapContaining.hasEntry("a", timeSeriesDescriptionA));
		assertThat(descriptionList, IsMapContaining.hasEntry("b", timeSeriesDescriptionB));
		assertThat(descriptionList, IsMapContaining.hasEntry("c", timeSeriesDescriptionC));
	}

	protected List<TimeSeriesDescription> buildDescriptionList() {
		List<TimeSeriesDescription> descriptionList = new ArrayList<>();
		descriptionList.add(timeSeriesDescriptionA);
		descriptionList.add(timeSeriesDescriptionB);
		descriptionList.add(timeSeriesDescriptionC);
		return descriptionList;
	}

}
