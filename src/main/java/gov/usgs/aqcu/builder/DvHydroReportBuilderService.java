package gov.usgs.aqcu.builder;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;
import java.time.Instant;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Approval;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierMetadata;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescriptionListByUniqueIdServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescriptionListServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;

import gov.usgs.aqcu.model.*;
import gov.usgs.aqcu.retrieval.*;

@Component
public class DvHydroReportBuilderService {	
	private static final Logger LOG = LoggerFactory.getLogger(DvHydroReportBuilderService.class);
	private final String BASE_URL = "http://temp/report/";
	private Gson gson;
	private TimeSeriesDataCorrectedService timeSeriesDataCorrectedService;
	private TimeSeriesDescriptionListService timeSeriesDescriptionListService;
	private LocationDescriptionListService locationDescriptionListService;
	private QualifierLookupService qualifierLookupService;
	
	
	@Autowired
	public DvHydroReportBuilderService(
		TimeSeriesDataCorrectedService timeSeriesDataCorrectedService,
		TimeSeriesDescriptionListService timeSeriesDescriptionListService,
		LocationDescriptionListService locationDescriptionListService,
		QualifierLookupService qualifierLookupService,
		Gson gson) {
			
		this.timeSeriesDataCorrectedService = timeSeriesDataCorrectedService;
		this.timeSeriesDescriptionListService = timeSeriesDescriptionListService;
		this.locationDescriptionListService = locationDescriptionListService;
		this.qualifierLookupService = qualifierLookupService;
		this.gson = gson;
	}

	public DvHydroReport buildReport( 
		String primaryTimeseriesIdentifier,
		String firstStatDerivedIdentifier,
		String secondStatDerivedIdentifier,
		String thirdStatDerivedIdentifier,
		String fourthStatDerivedIdentifier,
		String firstReferenceIdentifier,
		String secondReferenceIdentifier,
		String thirdReferenceIdentifier,
		String comparisonTimeseriesIdentifier,
		Instant startDate,
		Instant endDate,
		String requestingUser) throws Exception {
		
		//Validate identifiers
		if(firstStatDerivedIdentifier == null 
				&& secondStatDerivedIdentifier == null 
				&& thirdStatDerivedIdentifier == null 
				&& fourthStatDerivedIdentifier == null) {
			String errorString = "Need at least one Stat-Derived Time Series Identifier selected.";
			LOG.error(errorString);
			throw new Exception(errorString);
		}

		//Fetch Timeseries Metadata
		Set<String> timeseriesIdentifiers = new LinkedHashSet<>();
		ArrayList<String> uniqueTimeseriesIdentifiers;
		timeseriesIdentifiers.add(primaryTimeseriesIdentifier);
		timeseriesIdentifiers.add(firstStatDerivedIdentifier);
		timeseriesIdentifiers.add(secondStatDerivedIdentifier);
		timeseriesIdentifiers.add(thirdStatDerivedIdentifier);
		timeseriesIdentifiers.add(fourthStatDerivedIdentifier);
		timeseriesIdentifiers.add(firstReferenceIdentifier);
		timeseriesIdentifiers.add(secondReferenceIdentifier);
		timeseriesIdentifiers.add(thirdReferenceIdentifier);
		timeseriesIdentifiers.add(comparisonTimeseriesIdentifier);
		uniqueTimeseriesIdentifiers = new ArrayList<>(timeseriesIdentifiers);
		
		// Remove any nulls
		uniqueTimeseriesIdentifiers.removeIf(Objects::isNull);

		//Parse Descriptions
		TimeSeriesDescription primaryDescription = null;
		TimeSeriesDescription firstStatDerivedDescription = null;
		TimeSeriesDescription secondStatDerivedDescription = null;
		TimeSeriesDescription thirdStatDerivedDescription = null;
		TimeSeriesDescription fourthStatDerivedDescription = null;
		TimeSeriesDescription firstReferenceDescription = null;
		TimeSeriesDescription secondReferenceDescription = null;
		TimeSeriesDescription thirdReferenceDescription = null;
		TimeSeriesDescription comparisonTimeseriesDescription = null;
		TimeSeriesDescriptionListByUniqueIdServiceResponse metadataResponse = timeSeriesDescriptionListService.get(uniqueTimeseriesIdentifiers);
		for(TimeSeriesDescription desc : metadataResponse.getTimeSeriesDescriptions()) {
			if(desc.getUniqueId().equals(primaryTimeseriesIdentifier)) {
				primaryDescription = desc;
			} 

			if(desc.getUniqueId().equals(firstStatDerivedIdentifier)) {
				firstStatDerivedDescription = desc;
			} 

			if(desc.getUniqueId().equals(secondStatDerivedIdentifier)) {
				secondStatDerivedDescription = desc;
			} 
			
			if(desc.getUniqueId().equals(thirdStatDerivedIdentifier)) {
				thirdStatDerivedDescription = desc;
			} 
			
			if(desc.getUniqueId().equals(fourthStatDerivedIdentifier)) {
				fourthStatDerivedDescription = desc;
			} 
			
			if(desc.getUniqueId().equals(firstReferenceIdentifier)) {
				firstReferenceDescription = desc;
			} 
			
			if(desc.getUniqueId().equals(secondReferenceIdentifier)) {
				secondReferenceDescription = desc;
			} 
			
			if(desc.getUniqueId().equals(thirdReferenceIdentifier)) {
				thirdReferenceDescription = desc;
			} 
			
			if(desc.getUniqueId().equals(comparisonTimeseriesIdentifier)) {
				comparisonTimeseriesDescription = desc;
			} 
		}
		
		//Validate descriptions
		if(primaryDescription == null || (firstStatDerivedIdentifier != null && firstStatDerivedDescription == null) || (secondStatDerivedIdentifier != null && secondStatDerivedDescription == null)
				|| (thirdStatDerivedIdentifier != null && thirdStatDerivedDescription == null) || (fourthStatDerivedIdentifier != null && fourthStatDerivedDescription == null)
				|| (firstReferenceIdentifier != null && firstReferenceDescription == null) || (secondReferenceIdentifier != null && secondReferenceDescription == null)
				|| (thirdReferenceIdentifier != null && thirdReferenceDescription == null) || (comparisonTimeseriesIdentifier != null && comparisonTimeseriesDescription == null)) {
			String errorString = "Failed to fetch descriptions for all requested Time Series Identifiers: \nRequested: " + 
				uniqueTimeseriesIdentifiers.size() + "{\nPrimary: " + primaryTimeseriesIdentifier + metadataResponse.getTimeSeriesDescriptions().size();
			LOG.error(errorString);
			//TODO: Change to more specific exception
			throw new Exception(errorString);
		}
		
		//Fetch Location Descriptions
		LocationDescriptionListServiceResponse locationResponse = locationDescriptionListService.get(primaryDescription.getLocationIdentifier());
		LocationDescription locationDescription = locationResponse.getLocationDescriptions().get(0);	
		
		//Fetch Primary Series Data
		TimeSeriesDataServiceResponse primarySeriesDataResponse = timeSeriesDataCorrectedService.get(primaryTimeseriesIdentifier, startDate, endDate);
		
		//Additional Metadata Lookups
		List<QualifierMetadata> qualifierMetadataList = qualifierLookupService.getByQualifierList(primarySeriesDataResponse.getQualifiers());
		
		List<Qualifier> qualifierList = primarySeriesDataResponse.getQualifiers();

		List<Approval> approvalList = primarySeriesDataResponse.getApprovals();
		
		DvHydroReport report = createReport(
			primarySeriesDataResponse, 
			primaryDescription, 
			firstStatDerivedDescription,
			secondStatDerivedDescription,
			thirdStatDerivedDescription,
			fourthStatDerivedDescription,
			firstReferenceDescription,
			secondReferenceDescription,
			thirdReferenceDescription,
			comparisonTimeseriesDescription,
			locationDescription,
			approvalList,
			qualifierList,
			qualifierMetadataList,
			startDate, 
			endDate, 
			requestingUser);
		
		return report;
	}
	
	private DvHydroReport createReport (
		TimeSeriesDataServiceResponse primaryDataResponse,
		TimeSeriesDescription primaryDescription,
		TimeSeriesDescription firstStatDerivedDescription,
		TimeSeriesDescription secondStatDerivedDescription,
		TimeSeriesDescription thirdStatDerivedDescription,
		TimeSeriesDescription fourthStatDerivedDescription,
		TimeSeriesDescription firstReferenceDescription,
		TimeSeriesDescription secondReferenceDescription,
		TimeSeriesDescription thirdReferenceDescription,
		TimeSeriesDescription comparisonTimeseriesDescription,
		LocationDescription locationDescription,
		List<Approval> approvalList,
		List<Qualifier> qualifierList,
		List<QualifierMetadata> qualifierMetadataList,
		Instant startDate,
		Instant endDate,
		String requestingUser) {			
			DvHydroReport report = new DvHydroReport();
			
			//Add Report Metadata
			report.setReportMetadata(createDvHydroMetadata(
				primaryDescription, 
				firstStatDerivedDescription,
				secondStatDerivedDescription,
				thirdStatDerivedDescription,
				fourthStatDerivedDescription,
				firstReferenceDescription,
				secondReferenceDescription,
				thirdReferenceDescription,
				comparisonTimeseriesDescription,
				locationDescription,
				qualifierMetadataList,
				startDate, 
				endDate, 
				requestingUser));
			
			//Add Primary TS Metadata
			report.setPrimaryTsMetadata(primaryDescription);
			
			report.setQualifier(qualifierList);
			
			report.setApproval(approvalList);
			
			return report;
	}
	
	private DvHydroMetadata createDvHydroMetadata(
		TimeSeriesDescription primaryDescription,
		TimeSeriesDescription firstStatDerivedDescription,
		TimeSeriesDescription secondStatDerivedDescription,
		TimeSeriesDescription thirdStatDerivedDescription,
		TimeSeriesDescription fourthStatDerivedDescription,
		TimeSeriesDescription firstReferenceDescription,
		TimeSeriesDescription secondReferenceDescription,
		TimeSeriesDescription thirdReferenceDescription,
		TimeSeriesDescription comparisonTimeseriesDescription,
		LocationDescription locationDescription,
		List<QualifierMetadata> qualifierMetadataList,
		Instant startDate,
		Instant endDate,
		String requestingUser) {
		DvHydroMetadata metadata = new DvHydroMetadata();
		
		metadata.setRequestingUser(requestingUser);
		metadata.setTimezone("Etc/GMT+" + (int)(-1 * primaryDescription.getUtcOffset()));
		metadata.setStartDate(startDate);
		metadata.setEndDate(endDate);
		metadata.setTitle("DV Hydrograph");
		metadata.setPrimaryParameter(primaryDescription.getIdentifier());
		if (firstStatDerivedDescription != null){
			metadata.setFirstStatDerivedParameter(firstStatDerivedDescription.getIdentifier());
		}
		if (secondStatDerivedDescription != null){
			metadata.setSecondStatDerivedParameter(secondStatDerivedDescription.getIdentifier());
		}
		if (thirdStatDerivedDescription != null){
			metadata.setThirdStatDerivedParameter(thirdStatDerivedDescription.getIdentifier());
		}
		if (fourthStatDerivedDescription != null){
			metadata.setFourthStatDerivedParameter(fourthStatDerivedDescription.getIdentifier());
		}
		if (firstReferenceDescription != null){
			metadata.setFirstReferenceTimeSeriesParameter(firstReferenceDescription.getIdentifier());
		}
		if (secondReferenceDescription != null){
			metadata.setSecondReferenceTimeSeriesParameter(secondReferenceDescription.getIdentifier());
		}
		if (thirdReferenceDescription != null){
			metadata.setThirdReferenceTimeSeriesParameter(thirdReferenceDescription.getIdentifier());
		}
		if (comparisonTimeseriesDescription != null){
			metadata.setComparisonSeriesParameter(comparisonTimeseriesDescription.getIdentifier());
		}
		metadata.setQualifierMetadata(qualifierMetadataList);
		metadata.setStationName(locationDescription.getName());
		metadata.setStationId(locationDescription.getIdentifier());
		
		return metadata;
	}

	private String createReportURL(String reportType, Map<String, String> parameters) {
		String reportUrl = BASE_URL + reportType + "?";
		
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			if(entry.getKey() != null && entry.getKey().length() > 0) {
				reportUrl += entry.getKey() + "=" + entry.getValue() + "&";
			}
		}
		
		return reportUrl.substring(0, reportUrl.length()-1);
	}
}
	
