package gov.usgs.aqcu.builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.ControlConditionType;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DischargeSummary;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DoubleWithDisplay;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.FieldVisitDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.LocationDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.ParameterMetadata;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesPoint;

import gov.usgs.aqcu.model.DvHydrographPoint;
import gov.usgs.aqcu.model.DvHydrographReport;
import gov.usgs.aqcu.model.DvHydrographReportMetadata;
import gov.usgs.aqcu.model.FieldVisitMeasurement;
import gov.usgs.aqcu.model.InstantRange;
import gov.usgs.aqcu.model.MeasurementGrade;
import gov.usgs.aqcu.model.MinMaxData;
import gov.usgs.aqcu.model.MinMaxPoint;
import gov.usgs.aqcu.model.TimeSeriesCorrectedData;
import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;
import gov.usgs.aqcu.retrieval.FieldVisitDataService;
import gov.usgs.aqcu.retrieval.FieldVisitDescriptionService;
import gov.usgs.aqcu.retrieval.LocationDescriptionService;
import gov.usgs.aqcu.retrieval.ParameterListService;
import gov.usgs.aqcu.retrieval.QualifierLookupService;
import gov.usgs.aqcu.retrieval.TimeSeriesDataCorrectedService;
import gov.usgs.aqcu.retrieval.TimeSeriesDescriptionService;
import gov.usgs.aqcu.util.AqcuTimeUtils;
import gov.usgs.aqcu.util.BigDecimalSummaryStatistics;

@Service
public class ReportBuilderService {
	private static final Logger LOG = LoggerFactory.getLogger(ReportBuilderService.class);

	private static final String ESTIMATED_QUALIFIER_VALUE = "ESTIMATED";
	protected static final String GAP_MARKER_POINT_VALUE = "EMPTY";
	private static final String VOLUMETRIC_FLOW_UNIT_GROUP_VALUE = "Volumetric Flow";

	private DataGapListBuilderServiceII dataGapListBuilderService;
	private FieldVisitDataService fieldVisitDataService;
	private FieldVisitDescriptionService fieldVisitDescriptionService; 
	private LocationDescriptionService locationDescriptionService;
	private ParameterListService parameterListService;
	private QualifierLookupService qualifierLookupService;
	private TimeSeriesDataCorrectedService timeSeriesDataCorrectedService;
	private TimeSeriesDescriptionService timeSeriesDescriptionService;

	@Value("${sims.base.url}")
	private String simsUrl;
	@Value("${waterdata.base.url}")
	private String waterdataUrl;

	@Autowired
	public ReportBuilderService(
			DataGapListBuilderServiceII dataGapListBuilderService,
			FieldVisitDataService fieldVisitDataService,
			FieldVisitDescriptionService fieldVisitDescriptionService,
			LocationDescriptionService locationDescriptionService,
			ParameterListService parameterListService,
			QualifierLookupService qualifierLookupService,
			TimeSeriesDataCorrectedService timeSeriesDataCorrectedService,
			TimeSeriesDescriptionService timeSeriesDescriptionService) {
		this.dataGapListBuilderService = dataGapListBuilderService;
		this.fieldVisitDataService = fieldVisitDataService;
		this.fieldVisitDescriptionService = fieldVisitDescriptionService;
		this.locationDescriptionService = locationDescriptionService;
		this.parameterListService = parameterListService;
		this.qualifierLookupService = qualifierLookupService;
		this.timeSeriesDataCorrectedService = timeSeriesDataCorrectedService;
		this.timeSeriesDescriptionService = timeSeriesDescriptionService;
	}

	public DvHydrographReport buildReport(DvHydrographRequestParameters requestParameters, String requestingUser) {
		DvHydrographReport dvHydroReport = new DvHydrographReport();

		Map<String, TimeSeriesDescription> timeSeriesDescriptions = timeSeriesDescriptionService.getTimeSeriesDescriptions(requestParameters);
		Map<String, ParameterMetadata> parameterMetadata = parameterListService.getParameterMetadata();

		TimeSeriesDataServiceResponse primarySeriesDataResponse = timeSeriesDataCorrectedService.get(
				requestParameters.getPrimaryTimeseriesIdentifier(), requestParameters,
				isDailyTimeSeries(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier())),
				getZoneOffset(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier())));

		dvHydroReport.setReportMetadata(createDvHydroMetadata(requestParameters, timeSeriesDescriptions, primarySeriesDataResponse, requestingUser));

//TODO
//		if(isGw = (Boolean) primaryTsMetadata.get("groundWater");) {
//			HashMap<String, String> gwReqParams = new HashMap<>();
//			gwReqParams.putAll(requestParams);
//			gwReqParams.put("gwLevEnt", primaryTsMetadata.get("waterLevelType") != null ? String.valueOf(primaryTsMetadata.get("waterLevelType")) : null);
//			gwReqParams.put("seaLevelDatum", primaryTsMetadata.get("seaLevelDatum") != null ? String.valueOf(primaryTsMetadata.get("seaLevelDatum")) : null);
//			if(StringUtils.isBlank(excludeDiscrete)) {
//				DataRetrievalRequest gwLevelData = new DataRetrievalRequest(requestId, MessageConfiguration.DATA_RETRIEVAL_SERVICE_TAG, DataRetrievalRequest.RequestType.gwlevel, gwReqParams);
//				parameterRequest = gwLevelData;
//				paramOutMap.add(OutputMapUtils.getExtractMap("records", "gwlevel", gwLevelData.getSRI()));
//				paramOutMap.add(OutputMapUtils.getExtractMap("allValid", "reportMetadata.gwlevelAllValid", gwLevelData.getSRI()));
//			}
//		} else
		if (timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getParameter().toString().contains("Discharge")) {
			dvHydroReport.setFieldVisitMeasurements(buildFieldVisitMeasurements(requestParameters,
					dvHydroReport.getReportMetadata().getStationId(),
					getZoneOffset(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()))));
//TODO
//		} else if (isWq = primaryTsMetadata.get("nwisPcode") != null;){
//			HashMap<String, String> qwParams = new HashMap<>();
//			qwParams.putAll(requestParams);
//			qwParams.put("pcode", primaryTsMetadata.get("nwisPcode") != null ? String.valueOf(primaryTsMetadata.get("nwisPcode")) : null);
//			if(StringUtils.isBlank(excludeDiscrete)) {
//				DataRetrievalRequest qwData = new DataRetrievalRequest(requestId, MessageConfiguration.DATA_RETRIEVAL_SERVICE_TAG, DataRetrievalRequest.RequestType.qwdata, qwParams);
//				dataRequestHolder.put("waterQuality", qwData);
//			}
		}

//		
//		//calculate inverted flag
//		result.setInverted(ParameterSpecService.isInvertedGwParam(result));
//		
//		//calculate ground water flag
//		result.setGroundWater(ParameterSpecService.isGwParamater(result));
//		
//		//calculate discharge flag
//		result.setDischarge(ParameterSpecService.isDischargeParameter(result));
//		
//		//groundwater filters
//		GroundWaterParameters gwParam = GroundWaterParameters.getByDisplayName(result.getParameter());
//		if(gwParam != null) {
//			result.setWaterLevelType(gwParam.getGwLevEnt());
//			result.setSeaLevelDatum(gwParam.getSeaLevDatum());
//		}
//
//			result.setSublocation(descs.get(0).getSubLocationIdentifier());
//
//			result.setTimeSeriesType(descs.get(0).getTimeSeriesType());
//
//			result.setPeriod(descs.get(0).getComputationPeriodIdentifier());
//
//			result.setPublish(descs.get(0).isPublish());
//
//			result.setPrimary(extractPrimaryFlag(descs.get(0)));
//			
//			result.setUniqueId(descs.get(0).getUniqueId());
//			
//			result.setComment(descs.get(0).getComment());
//			
//			result.setDescription(descs.get(0).getDescription());
//			
//			result.setExtendedAttributes(toAqcuExtendedAttributes(descs.get(0).getExtendedAttributes()));


		dvHydroReport.setPrimarySeriesQualifiers(primarySeriesDataResponse.getQualifiers());
		dvHydroReport.setPrimarySeriesApprovals(primarySeriesDataResponse.getApprovals());

		if (StringUtils.isNotBlank(requestParameters.getFirstStatDerivedIdentifier())) {
			dvHydroReport.setFirstStatDerived(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getFirstStatDerivedIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getSecondStatDerivedIdentifier())) {
			dvHydroReport.setSecondStatDerived(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getSecondStatDerivedIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getThirdStatDerivedIdentifier())) {
			dvHydroReport.setThirdStatDerived(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getThirdStatDerivedIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getFourthStatDerivedIdentifier())) {
			dvHydroReport.setFourthStatDerived(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getFourthStatDerivedIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getFirstReferenceIdentifier())) {
			dvHydroReport.setFirstReferenceTimeSeries(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getFirstReferenceIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getSecondReferenceIdentifier())) {
			dvHydroReport.setSecondReferenceTimeSeries(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getSecondReferenceIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getThirdReferenceIdentifier())) {
			dvHydroReport.setThirdReferenceTimeSeries(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getThirdReferenceIdentifier(), requestParameters, parameterMetadata));
		}

		if (StringUtils.isNotBlank(requestParameters.getComparisonTimeseriesIdentifier())) {
			dvHydroReport.setComparisonSeries(buildTimeSeriesCorrectedData(timeSeriesDescriptions, requestParameters.getComparisonTimeseriesIdentifier(), requestParameters, parameterMetadata));
		}

		dvHydroReport.setSimsUrl(getSimsUrl(dvHydroReport.getReportMetadata().getStationId()));
		dvHydroReport.setWaterdataUrl(getWaterdataUrl(dvHydroReport.getReportMetadata().getStationId()));

		dvHydroReport.setMaxMinData(getMinMaxData(primarySeriesDataResponse.getPoints()));

		return dvHydroReport;
	}

	protected boolean isDailyTimeSeries(TimeSeriesDescription timeSeriesDescription) {
		return "Daily".equalsIgnoreCase(timeSeriesDescription.getComputationPeriodIdentifier());
	}

	protected ZoneOffset getZoneOffset(TimeSeriesDescription timeSeriesDescription) {
		//Default to UTC
		ZoneOffset zoneOffset = ZoneOffset.UTC;
		Double utcOffset = timeSeriesDescription.getUtcOffset();

		try {
			Double minutes = utcOffset % 1;
			if (minutes != 0) {
				Double hours = utcOffset - minutes;
				zoneOffset = ZoneOffset.ofHoursMinutes(hours.intValue(), minutes.intValue());
			} else {
				zoneOffset = ZoneOffset.ofHours(utcOffset.intValue());
			}
		} catch (Exception e) {
			LOG.info("Error converting utcOffset({}) to ZoneOffset", utcOffset);
		}

		return zoneOffset;
	}

	protected TimeSeriesCorrectedData buildTimeSeriesCorrectedData(Map<String, TimeSeriesDescription> timeSeriesDescriptions, String timeSeriesIdentifier, DvHydrographRequestParameters requestParameters, Map<String, ParameterMetadata> parameterMetadata) {
		TimeSeriesCorrectedData timeSeriesCorrectedData = null;

		boolean isDaily = isDailyTimeSeries(timeSeriesDescriptions.get(timeSeriesIdentifier));
		ZoneOffset zoneOffset = getZoneOffset(timeSeriesDescriptions.get(timeSeriesIdentifier));
		TimeSeriesDataServiceResponse timeSeriesDataServiceResponse = timeSeriesDataCorrectedService.get(timeSeriesIdentifier, requestParameters, isDaily, zoneOffset);
		if (timeSeriesDataServiceResponse != null) {
			timeSeriesCorrectedData = createDvHydroCorrectedData(timeSeriesDataServiceResponse, requestParameters, isDaily, getVolumetricFlow(parameterMetadata, timeSeriesDataServiceResponse.getParameter()), zoneOffset);
		}

		timeSeriesCorrectedData.setApprovals(timeSeriesDataServiceResponse.getApprovals());
		timeSeriesCorrectedData.setGaps(dataGapListBuilderService.buildGapList(timeSeriesDataServiceResponse.getPoints(), isDaily, zoneOffset));
		timeSeriesCorrectedData.setGapTolerances(timeSeriesDataServiceResponse.getGapTolerances());
		return timeSeriesCorrectedData;
	}

	protected DvHydrographReportMetadata createDvHydroMetadata(DvHydrographRequestParameters requestParameters, Map<String, TimeSeriesDescription> timeSeriesDescriptions, TimeSeriesDataServiceResponse primarySeriesDataResponse, String requestingUser) {
//		boolean inverted = false; GW ONLY!!!??
//		try {
//			inverted = (Boolean) primaryTsMetadata.get("inverted");
//		} catch (Exception e) {
//			log.trace("No inverted flag found");
//		}
		DvHydrographReportMetadata metadata = new DvHydrographReportMetadata();

		metadata.setExcludeDiscrete(requestParameters.isExcludeDiscrete());
		metadata.setExcludeMinMax(requestParameters.isExcludeMinMax());
		metadata.setExcludeZeroNegative(requestParameters.isExcludeZeroNegative());

		metadata.setTimezone("Etc/GMT+" + (int)(-1 * timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getUtcOffset()));
		//Repgen just pulls the date for the headings, so we need to be sure and get the "correct" date - it's internal filtering is potentially slightly skewed by this.
		metadata.setStartDate(requestParameters.getStartInstant(ZoneOffset.UTC));
		metadata.setEndDate(requestParameters.getEndInstant(ZoneOffset.UTC));
		metadata.setTitle("DV Hydrograph");

		metadata.setPrimarySeriesLabel(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getIdentifier());

		if (timeSeriesDescriptions.containsKey(requestParameters.getFirstStatDerivedIdentifier())) {
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

		LocationDescription locationDescription = locationDescriptionService.getByLocationIdentifier(timeSeriesDescriptions.get(requestParameters.getPrimaryTimeseriesIdentifier()).getLocationIdentifier());
		metadata.setStationName(locationDescription.getName());
		metadata.setStationId(locationDescription.getIdentifier());

		return metadata;
	}

	protected String getTimezone(Double offset) {
		StringBuilder timezone = new StringBuilder("Etc/GMT");
		if (offset <= 0) {
			timezone.append("+");
		} else {
			timezone.append("-");
		}
		timezone.append(Math.abs(offset.intValue()));
		return timezone.toString();
	}

	protected List<FieldVisitMeasurement> buildFieldVisitMeasurements(DvHydrographRequestParameters requestParameters, String locationIdentifier, ZoneOffset zoneOffset) {
		List<FieldVisitMeasurement> fieldVisitMeasurements = new ArrayList<>();

		List<FieldVisitDescription> fieldVisitDescriptions = fieldVisitDescriptionService.getDescriptions(locationIdentifier, zoneOffset, requestParameters);

		if (fieldVisitDescriptions != null){
			for (FieldVisitDescription fvd : fieldVisitDescriptions){
				fieldVisitMeasurements.addAll(createFieldVisitMeasurements(fvd));
			}
		}

		return fieldVisitMeasurements;
	}

	protected List<FieldVisitMeasurement> createFieldVisitMeasurements(FieldVisitDescription visit) {
		List<FieldVisitMeasurement> ret = new ArrayList<FieldVisitMeasurement>();

		FieldVisitDataServiceResponse fieldVisitDataServiceResponse = fieldVisitDataService.get(visit.getIdentifier());

		if (fieldVisitDataServiceResponse.getDischargeActivities() != null) {
			ret = fieldVisitDataServiceResponse.getDischargeActivities().stream()
				.filter(x -> x.getDischargeSummary() != null)
				.filter(y -> y.getDischargeSummary().getDischarge() != null)
				.map(z -> {return getFieldVisitMeasurement(z.getDischargeSummary());})
				.collect(Collectors.toList());
		}

		return ret;
	}

	protected String getControlCondition(FieldVisitDataServiceResponse fieldVisitDataServiceResponse) {
		ControlConditionType controlCondition = fieldVisitDataServiceResponse.getControlConditionActivity() != null ? 
				fieldVisitDataServiceResponse.getControlConditionActivity().getControlCondition() : null;

		return controlCondition != null ? controlCondition.toString() : null;
	}

	protected FieldVisitMeasurement getFieldVisitMeasurement(DischargeSummary dischargeSummary) {
		MeasurementGrade grade = MeasurementGrade.fromMeasurementGradeType(dischargeSummary.getMeasurementGrade());

		FieldVisitMeasurement fieldVisitMeasurement = calculateError(grade,
				dischargeSummary.getMeasurementId(),
				getRoundedValue(dischargeSummary.getDischarge()),
				dischargeSummary.getMeasurementStartTime()
				);

		return fieldVisitMeasurement;
	}

	protected FieldVisitMeasurement calculateError(MeasurementGrade grade, String measurementNumber,
			BigDecimal dischargeValue, Instant dateTime) {

		BigDecimal errorAmt = dischargeValue.multiply(grade.getPercentageOfError());
		BigDecimal errorMaxDischargeInFeet = dischargeValue.add(errorAmt);
		BigDecimal errorMinDischargeInFeet = dischargeValue.subtract(errorAmt);

		FieldVisitMeasurement ret = new FieldVisitMeasurement(measurementNumber, dischargeValue, 
				errorMaxDischargeInFeet, errorMinDischargeInFeet, dateTime);

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

	protected TimeSeriesCorrectedData createDvHydroCorrectedData(
			TimeSeriesDataServiceResponse response,
			DvHydrographRequestParameters requestParameters,
			boolean isDaily,
			boolean isVolumetricFlow,
			ZoneOffset zoneOffset) {
		TimeSeriesCorrectedData data = new TimeSeriesCorrectedData();

		data.setStartTime(AqcuTimeUtils.getTemporal(response.getTimeRange().getStartTime(), isDaily, zoneOffset));
		data.setEndTime(AqcuTimeUtils.getTemporal(response.getTimeRange().getEndTime(), isDaily, zoneOffset));
		data.setUnit(response.getUnit());
		data.setType(response.getParameter());
		data.setPoints(createDvHydroPoints(response.getPoints(), requestParameters, isDaily, zoneOffset));

		List<InstantRange> estimatedPeriods = response.getQualifiers().stream()
				.filter(x -> x.getIdentifier().equals(ESTIMATED_QUALIFIER_VALUE))
				.map(x -> {InstantRange dateRange = new InstantRange(x.getStartTime(), x.getEndTime());
				return dateRange;})
				.collect(Collectors.toList());
		if (!estimatedPeriods.isEmpty()) {
			data.setEstimatedPeriods(estimatedPeriods);
		}

		data.setVolumetricFlow(isVolumetricFlow);

		return data;
	}

	protected List<DvHydrographPoint> createDvHydroPoints(List<TimeSeriesPoint> timeSeriesPoints, DvHydrographRequestParameters requestParameters, boolean isDaily, ZoneOffset zoneOffset){
		List<DvHydrographPoint> dvPoints = timeSeriesPoints.parallelStream()
				.filter(x -> x.getValue().getNumeric() != null)
				.map(x -> {DvHydrographPoint dvPoint = new DvHydrographPoint();
					dvPoint.setTime(AqcuTimeUtils.getTemporal(x.getTimestamp(), isDaily, zoneOffset));
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

	protected MinMaxData getMinMaxData(ArrayList<TimeSeriesPoint> points) {
		BigDecimalSummaryStatistics stats = points.parallelStream()
				.map(x -> new BigDecimal(x.getValue().getDisplay()))
				.collect(BigDecimalSummaryStatistics::new,
						BigDecimalSummaryStatistics::accept,
						BigDecimalSummaryStatistics::combine);

		Map<BigDecimal, List<MinMaxPoint>> wow = points.parallelStream()
				.filter(x -> new BigDecimal(x.getValue().getDisplay()).equals(stats.getMin())
						|| new BigDecimal(x.getValue().getDisplay()).equals(stats.getMax()))
				.map(x -> {MinMaxPoint point = new MinMaxPoint(x.getTimestamp().getDateTimeOffset(), new BigDecimal(x.getValue().getDisplay()));
					return point;})
				.collect(Collectors.groupingByConcurrent(MinMaxPoint::getValue));

		MinMaxData minMax = new MinMaxData(stats.getMin(), stats.getMax(), wow);

		return minMax;
	}

	protected Boolean getVolumetricFlow(Map<String, ParameterMetadata> parameterMetadata, String parameter) {
		if (parameterMetadata.containsKey(parameter) && parameterMetadata.get(parameter).getUnitGroupIdentifier().equalsIgnoreCase(VOLUMETRIC_FLOW_UNIT_GROUP_VALUE)) {
			return true;
		} else {
			return false;
		}
	}





























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
