package gov.usgs.aqcu.retrieval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataCorrectedServiceRequest;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;
import java.time.Instant;

@Component
public class TimeSeriesDataCorrectedService extends AquariusRetrievalService {
	private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesDataCorrectedService.class);
	
	public TimeSeriesDataServiceResponse get(String primaryTimeseriesIdentifier, Instant startDate, Instant endDate) throws Exception {
		TimeSeriesDataCorrectedServiceRequest request = new TimeSeriesDataCorrectedServiceRequest()
				.setTimeSeriesUniqueId(primaryTimeseriesIdentifier)
				.setQueryFrom(startDate)
				.setQueryTo(endDate);
		TimeSeriesDataServiceResponse timeSeriesResponse  = executePublishApiRequest(request);
		return timeSeriesResponse;
	}
}
