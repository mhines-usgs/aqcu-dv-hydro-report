package gov.usgs.aqcu.retrieval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierListServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierListServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierMetadata;

@Component
public class QualifierLookupService {
	private static final Logger LOG = LoggerFactory.getLogger(QualifierLookupService.class);

	private AquariusRetrievalService aquariusRetrievalService;

	@Autowired
	public QualifierLookupService(AquariusRetrievalService aquariusRetrievalService) {
		this.aquariusRetrievalService = aquariusRetrievalService;
	}

	public Map<String, QualifierMetadata> getByQualifierList(List<Qualifier> includeQualifiers) {
		List<QualifierMetadata> qualifierList = new ArrayList<>();
		List<String> qualifierIdentifiers = buildIdentifierList(includeQualifiers);

		try {
			qualifierList = get();
		} catch (Exception e) {
			String msg = "An unexpected error occurred while attempting to fetch QualifierMetadata from Aquarius: ";
			LOG.error(msg, e);
			throw new RuntimeException(msg, e);
		}

		return filterList(qualifierIdentifiers, qualifierList);
	}

	protected List<String> buildIdentifierList(List<Qualifier> includeQualifiers) {
		return includeQualifiers.stream()
				.map(x -> x.getIdentifier())
				.collect(Collectors.toList());
	}

	protected List<QualifierMetadata> get() throws Exception {
		QualifierListServiceRequest request = new QualifierListServiceRequest();
		QualifierListServiceResponse qualifierListResponse = aquariusRetrievalService.executePublishApiRequest(request);
		return qualifierListResponse.getQualifiers();
	}

	protected Map<String, QualifierMetadata> filterList(List<String> includeIdentifiers, List<QualifierMetadata> qualifierList) {
		Map<String, QualifierMetadata> filtered = qualifierList.stream()
				.filter(x -> includeIdentifiers.contains(x.getIdentifier()))
				.collect(Collectors.toMap(x -> x.getIdentifier(), x -> x));

		return filtered;
	}

}
