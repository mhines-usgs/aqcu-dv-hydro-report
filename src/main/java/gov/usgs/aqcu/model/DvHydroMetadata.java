package gov.usgs.aqcu.model;

import java.time.Instant;
import java.util.Map;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.QualifierMetadata;

public class DvHydroMetadata {	

	private String requestingUser;
	private String timezone;
	private Instant startDate;
	private Instant endDate;
	private String title;
	private String primarySeriesLabel;
	private String firstStatDerivedLabel;
	private String secondStatDerivedLabel;
	private String thirdStatDerivedLabel;
	private String fourthStatDerivedLabel;
	private String firstReferenceTimeSeriesLabel;
	private String secondReferenceTimeSeriesLabel;
	private String thirdReferenceTimeSeriesLabel;
	private String comparisonSeriesLabel;
	private Map<String, QualifierMetadata> qualifierMetadata;
	private String stationName;
	private String stationId;

	public String getRequestingUser() {
		return requestingUser;
	}

	public String getTimezone() {
		return timezone;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public String getTitle() {
		return title;
	}

	public String getPrimaryParameter() {
		return primarySeriesLabel;
	}

	public String getFirstStatDerivedParameter() {
		return firstStatDerivedLabel;
	}

	public String getSecondStatDerivedParameter() {
		return secondStatDerivedLabel;
	}

	public String getThirdStatDerivedParameter() {
		return thirdStatDerivedLabel;
	}

	public String getFourthStatDerivedParameter() {
		return fourthStatDerivedLabel;
	}

	public String getFirstReferenceTimeSeriesParameter() {
		return firstReferenceTimeSeriesLabel;
	}

	public String getSecondReferenceTimeSeriesParameter() {
		return secondReferenceTimeSeriesLabel;
	}

	public String getThirdReferenceTimeSeriesParameter() {
		return thirdReferenceTimeSeriesLabel;
	}

	public String getComparisonSeriesParameter() {
		return comparisonSeriesLabel;
	}

	public Map<String, QualifierMetadata> getQualifierMetadata() {
		return qualifierMetadata;
	}

	public String getStationName() {
		return stationName;
	}

	public String getStationId() {
		return stationId;
	}

	public void setRequestingUser(String val) {
		requestingUser = val;
	}

	public void setTimezone(String val) {
		timezone = val;
	}

	public void setStartDate(Instant val) {
		startDate = val;
	}

	public void setEndDate(Instant val) {
		endDate = val;
	}

	public void setTitle(String val) {
		title = val;
	}

	public void setPrimaryParameter(String val) {
		primarySeriesLabel = val;
	}

	public void setFirstStatDerivedParameter(String val) {
		firstStatDerivedLabel = val;
	}

	public void setSecondStatDerivedParameter(String val) {
		secondStatDerivedLabel = val;
	}

	public void setThirdStatDerivedParameter(String val) {
		thirdStatDerivedLabel = val;
	}

	public void setFourthStatDerivedParameter(String val) {
		fourthStatDerivedLabel = val;
	}

	public void setFirstReferenceTimeSeriesParameter(String val) {
		firstReferenceTimeSeriesLabel = val;
	}

	public void setSecondReferenceTimeSeriesParameter(String val) {
		secondReferenceTimeSeriesLabel = val;
	}

	public void setThirdReferenceTimeSeriesParameter(String val) {
		thirdReferenceTimeSeriesLabel = val;
	}

	public void setComparisonSeriesParameter(String val) {
		comparisonSeriesLabel = val;
	}

	public void setQualifierMetadata(Map<String, QualifierMetadata> val) {
		qualifierMetadata = val;
	}

	public void setStationName(String val) {
		stationName = val;
	}

	public void setStationId(String val) {
		stationId = val;
	}

}
