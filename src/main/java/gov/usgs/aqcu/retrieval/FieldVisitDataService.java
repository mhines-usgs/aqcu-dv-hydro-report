package gov.usgs.aqcu.retrieval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDataServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDataServiceResponse;

import gov.usgs.aqcu.retrieval.AquariusRetrievalService;

@Component
public class FieldVisitDataService extends AquariusRetrievalService {
	private static final Logger LOG = LoggerFactory.getLogger(FieldVisitDataService.class);
	
	public FieldVisitDataServiceResponse get(String fieldVisitIdentifier) throws Exception {
		FieldVisitDataServiceRequest request = new FieldVisitDataServiceRequest()
				.setFieldVisitIdentifier(fieldVisitIdentifier);
		FieldVisitDataServiceResponse fieldVisitResponse  = executePublishApiRequest(request);
		return fieldVisitResponse;
	}
}
