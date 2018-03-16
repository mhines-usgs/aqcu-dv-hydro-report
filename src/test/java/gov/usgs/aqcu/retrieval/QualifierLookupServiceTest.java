package gov.usgs.aqcu.retrieval;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
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

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierListServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierMetadata;

import net.servicestack.client.IReturn;

@RunWith(SpringRunner.class)
public class QualifierLookupServiceTest {

	@MockBean
	private AquariusRetrievalService aquariusService;

	private QualifierLookupService service;
	private Qualifier qualifierA = new Qualifier().setIdentifier("a");
	private Qualifier qualifierB = new Qualifier().setIdentifier("b");
	private Qualifier qualifierC = new Qualifier().setIdentifier("c");
	private Qualifier qualifierD = new Qualifier().setIdentifier("d");
	public static final QualifierMetadata QUALIFIER_METADATA_A = new QualifierMetadata().setIdentifier("a");
	public static final QualifierMetadata QUALIFIER_METADATA_B = new QualifierMetadata().setIdentifier("b");
	public static final QualifierMetadata QUALIFIER_METADATA_C = new QualifierMetadata().setIdentifier("c");
	public static final QualifierMetadata QUALIFIER_METADATA_D = new QualifierMetadata().setIdentifier("d");

	@Before
	@SuppressWarnings("unchecked")
	public void setup() throws Exception {
		service = new QualifierLookupService(aquariusService);
		given(aquariusService.executePublishApiRequest(any(IReturn.class))).willReturn(new QualifierListServiceResponse()
				.setQualifiers(new ArrayList<QualifierMetadata>(Arrays.asList(QUALIFIER_METADATA_A, QUALIFIER_METADATA_B, QUALIFIER_METADATA_C, QUALIFIER_METADATA_D))));
	}

	@Test
	public void buildItentifierListTest() {
		List<String> actual = service.buildIdentifierList(Arrays.asList(qualifierA, qualifierB, qualifierC, qualifierD));
		assertEquals(4, actual.size());
		assertThat(actual, containsInAnyOrder("a", "b", "c", "d"));
		}

	@Test
	public void filterListTest() {
		Map<String, QualifierMetadata> actual = service.filterList(Arrays.asList("a", "c", "d"), Arrays.asList(QUALIFIER_METADATA_A, QUALIFIER_METADATA_B, QUALIFIER_METADATA_C, QUALIFIER_METADATA_D));
		assertEquals(3, actual.size());
		assertThat(actual, IsMapContaining.hasEntry("a", QUALIFIER_METADATA_A));
		assertThat(actual, IsMapContaining.hasEntry("c", QUALIFIER_METADATA_C));
		assertThat(actual, IsMapContaining.hasEntry("d", QUALIFIER_METADATA_D));
		}

	@Test
	public void getTest() throws Exception {
		List<QualifierMetadata> actual = service.get();
		assertEquals(4, actual.size());
		assertThat(actual, containsInAnyOrder(QUALIFIER_METADATA_A, QUALIFIER_METADATA_B, QUALIFIER_METADATA_C, QUALIFIER_METADATA_D));
	}

	@Test
	public void getByQualifierList_happyPathTest() {
		Map<String, QualifierMetadata> actual = service.getByQualifierList(Arrays.asList(qualifierA, qualifierC, qualifierD));
		assertEquals(3, actual.size());
		assertThat(actual, IsMapContaining.hasEntry("a", QUALIFIER_METADATA_A));
		assertThat(actual, IsMapContaining.hasEntry("c", QUALIFIER_METADATA_C));
		assertThat(actual, IsMapContaining.hasEntry("d", QUALIFIER_METADATA_D));
	}

}
