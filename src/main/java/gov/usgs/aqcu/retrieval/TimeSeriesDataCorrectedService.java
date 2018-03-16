package gov.usgs.aqcu.retrieval;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataCorrectedServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;

import gov.usgs.aqcu.parameter.DvHydroRequestParameters;

@Component
public class TimeSeriesDataCorrectedService {
	private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesDataCorrectedService.class);

	private AquariusRetrievalService aquariusRetrievalService;

	@Autowired
	public TimeSeriesDataCorrectedService(AquariusRetrievalService aquariusRetrievalService) {
		this.aquariusRetrievalService = aquariusRetrievalService;
	}

	public List<Qualifier> getQualifiers(DvHydroRequestParameters requestParameters) {
		List<Qualifier> qualifiers = new ArrayList<>();
		try {
			TimeSeriesDataServiceResponse timeSeriesResponse = get(requestParameters.getPrimaryTimeseriesIdentifier(),
					requestParameters.getStartInstant(),
					requestParameters.getEndInstant());
			qualifiers = timeSeriesResponse.getQualifiers();
		} catch (Exception e) {
			String msg = "An unexpected error occurred while attempting to fetch TimeSeriesDataCorrectedRequest from Aquarius: ";
			LOG.error(msg, e);
			throw new RuntimeException(msg, e);
		}
		return qualifiers;
	}

	protected TimeSeriesDataServiceResponse get(String primaryTimeseriesIdentifier, Instant startDate, Instant endDate) throws Exception {
		TimeSeriesDataCorrectedServiceRequest request = new TimeSeriesDataCorrectedServiceRequest()
				.setTimeSeriesUniqueId(primaryTimeseriesIdentifier)
				.setQueryFrom(startDate)
				.setQueryTo(endDate);
		TimeSeriesDataServiceResponse timeSeriesResponse  = aquariusRetrievalService.executePublishApiRequest(request);
		return timeSeriesResponse;
	}

}
