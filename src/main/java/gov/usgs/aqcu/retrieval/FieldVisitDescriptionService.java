package gov.usgs.aqcu.retrieval;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescriptionListServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescriptionListServiceResponse;

import gov.usgs.aqcu.model.AqcuFieldVisit;
import gov.usgs.aqcu.parameter.DvHydroRequestParameters;

@Component
public class FieldVisitDescriptionService {
	private static final Logger LOG = LoggerFactory.getLogger(FieldVisitDescriptionService.class);

	private AquariusRetrievalService aquariusRetrievalService;

	@Autowired
	public FieldVisitDescriptionService(AquariusRetrievalService aquariusRetrievalService) {
		this.aquariusRetrievalService = aquariusRetrievalService;
	}

	public List<AqcuFieldVisit> getDescriptions(String stationId, DvHydroRequestParameters requestParameters) {
		List<FieldVisitDescription> descriptions = new ArrayList<>();
		try {
			FieldVisitDescriptionListServiceResponse fieldVisitResponse = get(stationId,
					requestParameters.getStartInstant(),
					requestParameters.getEndInstant());
			descriptions = fieldVisitResponse.getFieldVisitDescriptions();
		} catch (Exception e) {
			String msg = "An unexpected error occurred while attempting to fetch FieldVisitDescriptionListServiceRequest from Aquarius: ";
			LOG.error(msg, e);
			throw new RuntimeException(msg, e);
		}
		return convertDescriptions(descriptions);
	}

	protected FieldVisitDescriptionListServiceResponse get(String stationId, Instant startDate, Instant endDate) throws Exception {
		FieldVisitDescriptionListServiceRequest request = new FieldVisitDescriptionListServiceRequest()
				.setLocationIdentifier(stationId)
				.setQueryFrom(startDate)
				.setQueryTo(endDate);
		FieldVisitDescriptionListServiceResponse fieldVisitResponse  = aquariusRetrievalService.executePublishApiRequest(request);
		return fieldVisitResponse;
	}

	protected List<AqcuFieldVisit> convertDescriptions(List<FieldVisitDescription> descriptions) {
		List<AqcuFieldVisit> convertedDescriptions = descriptions.stream()
				.map(x -> {AqcuFieldVisit rtn = new AqcuFieldVisit(x.getLocationIdentifier(),
						x.getStartTime(), 
						x.getEndTime(), 
						x.getIdentifier(), 
						x.getIsValid(),
						x.getLastModified(), 
						x.getParty(), 
						x.getRemarks(), 
						x.getWeather());
						return rtn;})
				.collect(Collectors.toList());
		return convertedDescriptions;
	}

}
