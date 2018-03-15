package gov.usgs.aqcu.retrieval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescriptionListServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescriptionListServiceResponse;
import java.time.Instant;

@Component
public class FieldVisitDescriptionListService extends AquariusRetrievalService {
	private static final Logger LOG = LoggerFactory.getLogger(FieldVisitDescriptionListService.class);
	
	public FieldVisitDescriptionListServiceResponse get(String stationId, Instant startDate, Instant endDate) throws Exception {
		FieldVisitDescriptionListServiceRequest request = new FieldVisitDescriptionListServiceRequest()
				.setLocationIdentifier(stationId)
				.setQueryFrom(startDate)
				.setQueryTo(endDate);
		FieldVisitDescriptionListServiceResponse fieldVisitResponse  = executePublishApiRequest(request);
		return fieldVisitResponse;
	}
}
