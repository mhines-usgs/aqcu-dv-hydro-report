package gov.usgs.aqcu.builder;

import static gov.usgs.aqcu.util.ScientificArithmetic.add;
import static gov.usgs.aqcu.util.ScientificArithmetic.multiply;
import static gov.usgs.aqcu.util.ScientificArithmetic.subtract;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.ControlConditionType;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DischargeSummary;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DoubleWithDisplay;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesPoint;

import gov.usgs.aqcu.model.AqcuFieldVisit;
import gov.usgs.aqcu.model.AqcuFieldVisitMeasurement;
import gov.usgs.aqcu.model.AqcuPoint;
import gov.usgs.aqcu.model.DvHydroCorrectedData;
import gov.usgs.aqcu.model.DvHydroMetadata;
import gov.usgs.aqcu.model.DvHydroReport;
import gov.usgs.aqcu.model.MeasurementGrade;
import gov.usgs.aqcu.parameter.DvHydroRequestParameters;
import gov.usgs.aqcu.retrieval.FieldVisitDataService;
import gov.usgs.aqcu.retrieval.FieldVisitDescriptionService;
import gov.usgs.aqcu.retrieval.LocationDescriptionService;
import gov.usgs.aqcu.retrieval.QualifierLookupService;
import gov.usgs.aqcu.retrieval.TimeSeriesDataCorrectedService;
import gov.usgs.aqcu.retrieval.TimeSeriesDescriptionService;

@Component
public class DvHydroReportBuilderService {	
	private static final Logger LOG = LoggerFactory.getLogger(DvHydroReportBuilderService.class);
	private final String BASE_URL = "http://temp/report/";
	private TimeSeriesDataCorrectedService timeSeriesDataCorrectedService;
	private TimeSeriesDescriptionService timeSeriesDescriptionListService;
	private LocationDescriptionService locationDescriptionListService;
	private QualifierLookupService qualifierLookupService;
	private FieldVisitDescriptionService fieldVisitDescriptionListService; 
	private FieldVisitDataService fieldVisitDataService;
	private static final String ESTIMATED_QUALIFIER_VALUE = "ESTIMATED";
	public static final String GAP_MARKER_POINT_VALUE = "EMPTY";

	@Value("${sims.base.url}")
	private String simsUrl;
	@Value("${waterdata.base.url}")
	private String waterdataUrl;

	@Autowired
	public DvHydroReportBuilderService(
		TimeSeriesDataCorrectedService timeSeriesDataCorrectedService,
		TimeSeriesDescriptionService timeSeriesDescriptionListService,
		LocationDescriptionService locationDescriptionListService,
		QualifierLookupService qualifierLookupService,
		FieldVisitDescriptionService fieldVisitDescriptionListService,
		FieldVisitDataService fieldVisitDataService) {
		this.timeSeriesDataCorrectedService = timeSeriesDataCorrectedService;
		this.timeSeriesDescriptionListService = timeSeriesDescriptionListService;
		this.locationDescriptionListService = locationDescriptionListService;
		this.qualifierLookupService = qualifierLookupService;
		this.fieldVisitDescriptionListService = fieldVisitDescriptionListService;
		this.fieldVisitDataService = fieldVisitDataService;
	}

	public DvHydroReport buildReport(DvHydroRequestParameters requestParameters, String requestingUser) {
		DvHydroReport dvHydroReport = new DvHydroReport();

		Map<String, TimeSeriesDescription> timeSeriesDescriptions = timeSeriesDescriptionListService.getTimeSeriesDescriptions(requestParameters);

		TimeSeriesDataServiceResponse primarySeriesDataResponse = timeSeriesDataCorrectedService.get(requestParameters.getPrimaryTimeseriesIdentifier(), requestParameters);

		dvHydroReport.setReportMetadata(createDvHydroMetadata(requestParameters, timeSeriesDescriptions, primarySeriesDataResponse, requestingUser));

		if (timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getParameter().toString().contains("Discharge")) {
			dvHydroReport.setFieldVisitMeasurements(buildFieldVisitMeasurements(requestParameters, dvHydroReport.getReportMetadata().getStationId()));
		}

		dvHydroReport.setPrimarySeriesQualifiers(primarySeriesDataResponse.getQualifiers());
		dvHydroReport.setPrimarySeriesApprovals(primarySeriesDataResponse.getApprovals());

		if (StringUtils.isNotBlank(requestParameters.getFirstStatDerivedIdentifier())) {
			TimeSeriesDataServiceResponse tsdcs = timeSeriesDataCorrectedService.get(requestParameters.getFirstStatDerivedIdentifier(), requestParameters);
			if (tsdcs != null) {
				dvHydroReport.setFirstStatDerived(createDvHydroCorrectedData(tsdcs, requestParameters.getStartInstant(), requestParameters.getEndInstant()));
			}
		}

		if (StringUtils.isNotBlank(requestParameters.getSecondStatDerivedIdentifier())) {
			TimeSeriesDataServiceResponse tsdcs = timeSeriesDataCorrectedService.get(requestParameters.getSecondStatDerivedIdentifier(), requestParameters);
			if (tsdcs != null) {
				dvHydroReport.setSecondStatDerived(createDvHydroCorrectedData(tsdcs, requestParameters.getStartInstant(), requestParameters.getEndInstant()));
			}
		}

		if (StringUtils.isNotBlank(requestParameters.getThirdStatDerivedIdentifier())) {
			TimeSeriesDataServiceResponse tsdcs = timeSeriesDataCorrectedService.get(requestParameters.getThirdStatDerivedIdentifier(), requestParameters);
			if (tsdcs != null) {
				dvHydroReport.setThirdStatDerived(createDvHydroCorrectedData(tsdcs, requestParameters.getStartInstant(), requestParameters.getEndInstant()));
			}
		}

		if (StringUtils.isNotBlank(requestParameters.getFourthStatDerivedIdentifier())) {
			TimeSeriesDataServiceResponse tsdcs = timeSeriesDataCorrectedService.get(requestParameters.getFourthStatDerivedIdentifier(), requestParameters);
			if (tsdcs != null) {
				dvHydroReport.setFourthStatDerived(createDvHydroCorrectedData(tsdcs, requestParameters.getStartInstant(), requestParameters.getEndInstant()));
			}
		}

		dvHydroReport.setSimsUrl(getSimsUrl(dvHydroReport.getReportMetadata().getStationId()));
		dvHydroReport.setWaterdataUrl(getWaterdataUrl(dvHydroReport.getReportMetadata().getStationId()));

		return dvHydroReport;
	}

	protected DvHydroMetadata createDvHydroMetadata(DvHydroRequestParameters requestParameters, Map<String, TimeSeriesDescription> timeSeriesDescriptions, TimeSeriesDataServiceResponse primarySeriesDataResponse, String requestingUser) {
//		boolean inverted = false; GW ONLY!!!??
//		try {
//			inverted = (Boolean) primaryTsMetadata.get("inverted");
//		} catch (Exception e) {
//			log.trace("No inverted flag found");
//		}
		DvHydroMetadata metadata = new DvHydroMetadata();

		metadata.setExcludeDiscrete(requestParameters.isExcludeDiscrete());
		metadata.setExcludeMinMax(requestParameters.isExcludeMinMax());
		metadata.setExcludeZeroNegative(requestParameters.isExcludeZeroNegative());
		metadata.setRequestingUser(requestingUser);
		metadata.setTimezone("Etc/GMT+" + (int)(-1 * timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getUtcOffset()));
		metadata.setStartDate(requestParameters.getStartInstant());
		metadata.setEndDate(requestParameters.getEndInstant());
		metadata.setTitle("DV Hydrograph");
		metadata.setPrimarySeriesLabel(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getIdentifier());

		if (timeSeriesDescriptions.containsKey(requestParameters.getFirstStatDerivedIdentifier())) {
			metadata.setFirstStatDerived(requestParameters.getFirstStatDerivedIdentifier());
			metadata.setFirstStatDerivedLabel(timeSeriesDescriptions.get(requestParameters.getFirstStatDerivedIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getSecondStatDerivedIdentifier())) {
			metadata.setSecondStatDerivedLabel(timeSeriesDescriptions.get(requestParameters.getSecondStatDerivedIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getThirdStatDerivedIdentifier())) {
			metadata.setThirdStatDerivedLabel(timeSeriesDescriptions.get(requestParameters.getThirdStatDerivedIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getFourthStatDerivedIdentifier())) {
			metadata.setFourthStatDerivedLabel(timeSeriesDescriptions.get(requestParameters.getFourthStatDerivedIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getFirstReferenceIdentifier())) {
			metadata.setFirstReferenceTimeSeriesLabel(timeSeriesDescriptions.get(requestParameters.getFirstReferenceIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getSecondReferenceIdentifier())) {
			metadata.setSecondReferenceTimeSeriesLabel(timeSeriesDescriptions.get(requestParameters.getSecondReferenceIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getThirdReferenceIdentifier())) {
			metadata.setThirdReferenceTimeSeriesLabel(timeSeriesDescriptions.get(requestParameters.getThirdReferenceIdentifier()).getIdentifier());
		}
		if (timeSeriesDescriptions.containsKey(requestParameters.getComparisonTimeseriesIdentifier())) {
			metadata.setComparisonSeriesLabel(timeSeriesDescriptions.get(requestParameters.getComparisonTimeseriesIdentifier()).getIdentifier());
		}

		metadata.setQualifierMetadata(qualifierLookupService.getByQualifierList(primarySeriesDataResponse.getQualifiers()));

		LocationDescription locationDescription = locationDescriptionListService.getByLocationIdentifier(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getLocationIdentifier());
		metadata.setStationName(locationDescription.getName());
		metadata.setStationId(locationDescription.getIdentifier());

		return metadata;
	}

	protected List<AqcuFieldVisitMeasurement> buildFieldVisitMeasurements(DvHydroRequestParameters requestParameters, String locationIdentifier) {
		List<AqcuFieldVisitMeasurement> aqcuFieldVisitMeasurements = new ArrayList<>();

		List<AqcuFieldVisit> fieldVisitDescriptions = fieldVisitDescriptionListService.getDescriptions(locationIdentifier, requestParameters);

		if (fieldVisitDescriptions != null){
			for (AqcuFieldVisit fvd : fieldVisitDescriptions){
				aqcuFieldVisitMeasurements.addAll(createFieldVisitMeasurement(fvd));
			}
		}

		return aqcuFieldVisitMeasurements;
	}

	protected List<AqcuFieldVisitMeasurement> createFieldVisitMeasurement(AqcuFieldVisit visit) {
		List<AqcuFieldVisitMeasurement> ret = new ArrayList<AqcuFieldVisitMeasurement>();

		FieldVisitDataServiceResponse fieldVisitDataServiceResponse = fieldVisitDataService.get(visit.getIdentifier());

		if (fieldVisitDataServiceResponse.getDischargeActivities() != null) {
			ret = fieldVisitDataServiceResponse.getDischargeActivities().stream()
				.filter(x -> x.getDischargeSummary() != null)
				.filter(y -> y.getDischargeSummary().getDischarge() != null)
				.map(z -> {return getFieldVisitMeasurement(visit, getControlCondition(fieldVisitDataServiceResponse), z.getDischargeSummary());})
				.collect(Collectors.toList());
		}

		return ret;
	}

	protected String getControlCondition(FieldVisitDataServiceResponse fieldVisitDataServiceResponse) {
		ControlConditionType controlCondition = fieldVisitDataServiceResponse.getControlConditionActivity() != null ? 
				fieldVisitDataServiceResponse.getControlConditionActivity().getControlCondition() : null;

		return controlCondition != null ? controlCondition.toString() : null;
	}

	protected AqcuFieldVisitMeasurement getFieldVisitMeasurement(AqcuFieldVisit visit, String controlCondition, DischargeSummary dischargeSummary) {
		MeasurementGrade grade = MeasurementGrade.valueFrom(dischargeSummary.getMeasurementGrade().name());

		AqcuFieldVisitMeasurement fieldVisitMeasurement = calculateError(grade,
				dischargeSummary.getMeasurementId(),
				controlCondition,
				getRoundedValue(dischargeSummary.getDischarge()),
				dischargeSummary.getDischarge().getUnit(),
				dischargeSummary.getMeanGageHeight().getUnit(),
				dischargeSummary.getMeasurementStartTime(), 
				visit.getIdentifier()
				);

		fieldVisitMeasurement.setMeanGageHeight(getRoundedValue(dischargeSummary.getMeanGageHeight()));
		return fieldVisitMeasurement;
	}

	/**
	 * Calculates the error of the associated field visit measurement
	 * 
	 * @param measurementNumber The number of the actual measurement
	 * @param controlCondition The associated control condition
	 * @param dischargeValue The associated discharge value
	 * @param dischargeUnits The units associated with the discharge value
	 * @param meanGageHeightUnits The units associated with the gage height
	 * @param dateTime The date and time of the measurement
	 * @param fieldVisitIdentifier The unique identifier of the associated field visit
	 * @return
	 */
	protected AqcuFieldVisitMeasurement calculateError(MeasurementGrade grade, String measurementNumber, String controlCondition, BigDecimal dischargeValue, 
			String dischargeUnits, String meanGageHeightUnits, Instant dateTime, String fieldVisitIdentifier) {

		BigDecimal errorMaxDischargeInFeet = add(dischargeValue, multiply(dischargeValue, grade.getPercentageOfError()));
		BigDecimal errorMinDischargeInFeet = subtract(dischargeValue, multiply(dischargeValue, grade.getPercentageOfError()));

		AqcuFieldVisitMeasurement ret = new AqcuFieldVisitMeasurement(measurementNumber, controlCondition, dischargeValue, dischargeUnits, meanGageHeightUnits,
				errorMaxDischargeInFeet, errorMinDischargeInFeet, grade, dateTime, fieldVisitIdentifier);

		return ret;
	}

	protected BigDecimal getRoundedValue(DoubleWithDisplay referenceVal){
		BigDecimal ret;

		if (referenceVal != null) {
			String tmp = referenceVal.getDisplay();
			if (tmp == null || tmp.equals(GAP_MARKER_POINT_VALUE)) {
				//Should be null but just in case.
				ret = referenceVal.getNumeric() == null ? null : BigDecimal.valueOf(referenceVal.getNumeric());
			} else {
				ret = new BigDecimal(tmp);
			}
		} else {
			ret = null;
		}
		return ret;
	}












	protected DvHydroCorrectedData createDvHydroCorrectedData(TimeSeriesDataServiceResponse response, Instant startDate, Instant endDate) {
		DvHydroCorrectedData data = new DvHydroCorrectedData();

		/*  Getting date errors, don't feel like dealing with it yet
		List<DateRange> estimatedPeriods = new ArrayList<>();
		for(Qualifier q : response.getQualifiers()) {
			if(q.getIdentifier().equals(ESTIMATED_QUALIFIER_VALUE)) {
				estimatedPeriods.add(new DateRange(q.getStartTime(), q.getEndTime()));
			}
		}
		*/
		//Copy Desired Fields
		data.setUnit(response.getUnit());
		data.setType(response.getParameter());
		data.setPoints(createDvHydroPoints(response.getPoints()));
		
		/*
		if(estimatedPeriods.size() > 0) {
			data.setEstimatedPeriods(estimatedPeriods);
		}
		*/

		return data;
	}

	protected List<AqcuPoint> createDvHydroPoints(List<TimeSeriesPoint> timeSeriesPoints){
		Drop first point? 
		List<AqcuPoint> dvPoints = timeSeriesPoints.parallelStream()
				.map(x -> {AqcuPoint dvPoint = new AqcuPoint();
					dvPoint.setTime(x.getTimestamp().getDateTimeOffset());
					dvPoint.setValue(getRoundedValue(x.getValue()));
					return dvPoint;})
				.collect(Collectors.toList());
		return dvPoints;
	}

	protected String getSimsUrl(String stationId) {
		String url = null;
		if (simsUrl != null) {
			url = simsUrl + "?site_no=" + stationId;
		}
		return url;
	}

	protected String getWaterdataUrl(String stationId) {
		String url = null;
		if( waterdataUrl != null) {
			url = waterdataUrl + "?site_no=" + stationId;
		}
		return url;
	}














//	private void temp() {
//			TimeSeriesDataServiceResponse firstStatDerivedDataResponse = null;
//			TimeSeriesDataServiceResponse secondStatDerivedDataResponse = null;
//			TimeSeriesDataServiceResponse thirdStatDerivedDataResponse = null;
//			TimeSeriesDataServiceResponse fourthStatDerivedDataResponse = null;
//			TimeSeriesDataServiceResponse firstReferenceDataResponse = null;
//			TimeSeriesDataServiceResponse secondReferenceDataResponse = null;
//			TimeSeriesDataServiceResponse thirdReferenceDataResponse = null;
//			TimeSeriesDataServiceResponse comparisonSeriesDataResponse = null;
//			FieldVisitDescriptionListServiceResponse fieldVisitDescriptions = null;
//
//			boolean isDischarge = false;
//			try {
//				isDischarge = (Boolean) primaryDescription.getParameter().toString().contains("Discharge");
//			} catch (Exception e) {
//				LOG.trace("No discharge involved");
//			}
//
//		//Fetch First Reference Series Data, if exists
//		if (firstReferenceIdentifier != null) {
//			firstReferenceDataResponse = timeSeriesDataCorrectedService.get(firstReferenceIdentifier, startDate, endDate);
//		}
//
//		//Fetch Second Reference Series Data, if exists
//		if (secondReferenceIdentifier != null) {
//			secondReferenceDataResponse = timeSeriesDataCorrectedService.get(secondReferenceIdentifier, startDate, endDate);
//		}
//
//		//Fetch Third Reference Series Data, if exists
//		if (thirdReferenceIdentifier != null) {
//			thirdReferenceDataResponse = timeSeriesDataCorrectedService.get(thirdReferenceIdentifier, startDate, endDate);
//		}
//
//		//Fetch Comparison Series Data, if exists
//		if (comparisonTimeseriesIdentifier != null) {
//			comparisonSeriesDataResponse = timeSeriesDataCorrectedService.get(comparisonTimeseriesIdentifier, startDate, endDate);
//		}
//
//		DvHydroReport report = createReport(
//			primarySeriesDataResponse, 
//			firstStatDerivedDataResponse,
//			secondStatDerivedDataResponse,
//			thirdStatDerivedDataResponse,
//			fourthStatDerivedDataResponse,
//			firstReferenceDataResponse,
//			secondReferenceDataResponse,
//			thirdReferenceDataResponse,
//			comparisonSeriesDataResponse,
//			firstStatDerivedDescription,
//			secondStatDerivedDescription,
//			thirdStatDerivedDescription,
//			fourthStatDerivedDescription,
//			firstReferenceDescription,
//			secondReferenceDescription,
//			thirdReferenceDescription,
//			comparisonTimeseriesDescription,
//			locationDescription,
//			fieldVisitDescriptions,
//			startDate, 
//			endDate, 
//			requestingUser);
//
//		return report;
//	}
















	/**
	 * Returns whether or not the supplied field visit had ice cover when it was performed
	 * @param fieldVisitDataResponse The Aquarius field visit service response to check
	 * @return TRUE - Ice Cover was present | FALSE - Ice Cover was not present
	 */
//	public boolean isIceCover(FieldVisitDataServiceResponse fieldVisitDataResponse) {
//		boolean isIceCover = false;
//		ControlConditionActivity c = fieldVisitDataResponse.getControlConditionActivity();
//
//		if(c != null) {
//			for(ControlConditionType iceCoverType : ICE_COVER_CONTROL_CONDITIONS) {
//				if(c.getControlCondition().equals(iceCoverType)) {
//					isIceCover = true;
//					break;
//				}
//			}
//		}
//
//		return isIceCover;
//	}



//	private static final List<ControlConditionType> ICE_COVER_CONTROL_CONDITIONS = Arrays.asList(new ControlConditionType[] {
//			ControlConditionType.IceCover,
//			ControlConditionType.IceShore,
//			ControlConditionType.IceAnchor
//	});

//	private String createReportURL(String reportType, Map<String, String> parameters) {
//		String reportUrl = BASE_URL + reportType + "?";
//		
//		for(Map.Entry<String, String> entry : parameters.entrySet()) {
//			if(entry.getKey() != null && entry.getKey().length() > 0) {
//				reportUrl += entry.getKey() + "=" + entry.getValue() + "&";
//			}
//		}
//		
//		return reportUrl.substring(0, reportUrl.length()-1);
//	}

}
