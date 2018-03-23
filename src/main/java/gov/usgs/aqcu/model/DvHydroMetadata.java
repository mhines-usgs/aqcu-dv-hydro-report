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
	private String firstStatDerived;
	private String firstStatDerivedLabel;
	private String secondStatDerivedLabel;
	private String thirdStatDerivedLabel;
	private String fourthStatDerivedLabel;
	private String firstReferenceTimeSeriesLabel;
	private String secondReferenceTimeSeriesLabel;
	private String thirdReferenceTimeSeriesLabel;
	private String comparisonSeriesLabel;
	private Map<String, QualifierMetadata> qualifierMetadata;
//TODO???	private Map<String, GradeMetadata> gradeMetadata;
	private String stationName;
	private String stationId;
	private boolean excludeZeroNegative;
	private boolean excludeMinMax;
	private boolean excludeDiscrete;
	private boolean isInverted;

	public String getRequestingUser() {
		return requestingUser;
	}
	public void setRequestingUser(String requestingUser) {
		this.requestingUser = requestingUser;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public Instant getStartDate() {
		return startDate;
	}
	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}
	public Instant getEndDate() {
		return endDate;
	}
	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrimarySeriesLabel() {
		return primarySeriesLabel;
	}
	public void setPrimarySeriesLabel(String primarySeriesLabel) {
		this.primarySeriesLabel = primarySeriesLabel;
	}
	public String getFirstStatDerived() {
		return firstStatDerived;
	}
	public void setFirstStatDerived(String firstStatDerived) {
		this.firstStatDerived = firstStatDerived;
	}
	public String getFirstStatDerivedLabel() {
		return firstStatDerivedLabel;
	}
	public void setFirstStatDerivedLabel(String firstStatDerivedLabel) {
		this.firstStatDerivedLabel = firstStatDerivedLabel;
	}
	public String getSecondStatDerivedLabel() {
		return secondStatDerivedLabel;
	}
	public void setSecondStatDerivedLabel(String secondStatDerivedLabel) {
		this.secondStatDerivedLabel = secondStatDerivedLabel;
	}
	public String getThirdStatDerivedLabel() {
		return thirdStatDerivedLabel;
	}
	public void setThirdStatDerivedLabel(String thirdStatDerivedLabel) {
		this.thirdStatDerivedLabel = thirdStatDerivedLabel;
	}
	public String getFourthStatDerivedLabel() {
		return fourthStatDerivedLabel;
	}
	public void setFourthStatDerivedLabel(String fourthStatDerivedLabel) {
		this.fourthStatDerivedLabel = fourthStatDerivedLabel;
	}
	public String getFirstReferenceTimeSeriesLabel() {
		return firstReferenceTimeSeriesLabel;
	}
	public void setFirstReferenceTimeSeriesLabel(String firstReferenceTimeSeriesLabel) {
		this.firstReferenceTimeSeriesLabel = firstReferenceTimeSeriesLabel;
	}
	public String getSecondReferenceTimeSeriesLabel() {
		return secondReferenceTimeSeriesLabel;
	}
	public void setSecondReferenceTimeSeriesLabel(String secondReferenceTimeSeriesLabel) {
		this.secondReferenceTimeSeriesLabel = secondReferenceTimeSeriesLabel;
	}
	public String getThirdReferenceTimeSeriesLabel() {
		return thirdReferenceTimeSeriesLabel;
	}
	public void setThirdReferenceTimeSeriesLabel(String thirdReferenceTimeSeriesLabel) {
		this.thirdReferenceTimeSeriesLabel = thirdReferenceTimeSeriesLabel;
	}
	public String getComparisonSeriesLabel() {
		return comparisonSeriesLabel;
	}
	public void setComparisonSeriesLabel(String comparisonSeriesLabel) {
		this.comparisonSeriesLabel = comparisonSeriesLabel;
	}
	public Map<String, QualifierMetadata> getQualifierMetadata() {
		return qualifierMetadata;
	}
	public void setQualifierMetadata(Map<String, QualifierMetadata> qualifierMetadata) {
		this.qualifierMetadata = qualifierMetadata;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public boolean getExcludeZeroNegative() {
		return excludeZeroNegative;
	}
	public void setExcludeZeroNegative(boolean excludeZeroNegative) {
		this.excludeZeroNegative = excludeZeroNegative;
	}
	public boolean getExcludeMinMax() {
		return excludeMinMax;
	}
	public void setExcludeMinMax(boolean excludeMinMax) {
		this.excludeMinMax = excludeMinMax;
	}
	public boolean getExcludeDiscrete() {
		return excludeDiscrete;
	}
	public void setExcludeDiscrete(boolean excludeDiscrete) {
		this.excludeDiscrete = excludeDiscrete;
	}
	public boolean isInverted() {
		return isInverted;
	}
	public void setInverted(boolean isInverted) {
		this.isInverted = isInverted;
	}

}
