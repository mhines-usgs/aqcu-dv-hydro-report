package gov.usgs.aqcu.retrieval;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescriptionListServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescriptionListServiceResponse;

@Component
public class LocationDescriptionService {
	private static final Logger LOG = LoggerFactory.getLogger(LocationDescriptionService.class);

	private AquariusRetrievalService aquariusRetrievalService;

	@Autowired
	public LocationDescriptionService(AquariusRetrievalService aquariusRetrievalService) {
		this.aquariusRetrievalService = aquariusRetrievalService;
	}

	public LocationDescription getByLocationIdentifier(String locationIdentifier) {
		LocationDescription locationDescription;
		try {
			List<LocationDescription> locationDescriptions = get(locationIdentifier);
			locationDescription = locationDescriptions.get(0);
		} catch (Exception e) {
			String msg = "An unexpected error occurred while attempting to fetch LocationDescription from Aquarius: ";
			LOG.error(msg, e);
			throw new RuntimeException(msg, e);
		}

		return locationDescription;
	}


	public List<LocationDescription> get(String stationId) throws Exception {
		LocationDescriptionListServiceRequest request = new LocationDescriptionListServiceRequest()
			.setLocationIdentifier(stationId);

		LocationDescriptionListServiceResponse locationResponse = aquariusRetrievalService.executePublishApiRequest(request);
		return locationResponse.getLocationDescriptions();
	}

}
