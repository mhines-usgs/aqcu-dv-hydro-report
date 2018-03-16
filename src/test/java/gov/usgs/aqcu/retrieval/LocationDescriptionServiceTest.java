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

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescriptionListServiceResponse;

import net.servicestack.client.IReturn;

@RunWith(SpringRunner.class)
public class LocationDescriptionServiceTest {

	@MockBean
	private AquariusRetrievalService aquariusService;

	private LocationDescriptionService service;
	LocationDescription locationDescriptionA = new LocationDescription().setIdentifier("a");
	LocationDescription locationDescriptionB = new LocationDescription().setIdentifier("b");

	@Before
	@SuppressWarnings("unchecked")
	public void setup() throws Exception {
		service = new LocationDescriptionService(aquariusService);
		given(aquariusService.executePublishApiRequest(any(IReturn.class))).willReturn(new LocationDescriptionListServiceResponse()
				.setLocationDescriptions(new ArrayList<LocationDescription>(Arrays.asList(locationDescriptionA, locationDescriptionB))));
	}

	@Test
	public void getTest() throws Exception {
		List<LocationDescription> actual = service.get("abc");
		assertEquals(2, actual.size());
		assertThat(actual, containsInAnyOrder(locationDescriptionA, locationDescriptionB));
	}

	@Test
	public void getByQualifierList_happyPathTest() {
		LocationDescription actual = service.getByLocationIdentifier("abc");
		assertEquals(actual, locationDescriptionA);
	}

}
