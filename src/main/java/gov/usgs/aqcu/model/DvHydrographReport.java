package gov.usgs.aqcu.model;

import java.util.List;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Approval;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;

public class DvHydrographReport {	

	//Required Properties
	private TimeSeriesCorrectedData firstStatDerived;
	private MinMaxData maxMinData;
	private DvHydrographReportMetadata reportMetadata;

	//Optional Properties
	private TimeSeriesCorrectedData comparisonSeries;
	private List<FieldVisitMeasurement> fieldVisitMeasurements;
	private TimeSeriesCorrectedData firstReferenceTimeSeries;
	private TimeSeriesCorrectedData fourthStatDerived;
	private List<Qualifier> primarySeriesQualifiers;
	private List<Approval> primarySeriesApprovals;
	private TimeSeriesCorrectedData secondReferenceTimeSeries;
	private TimeSeriesCorrectedData secondStatDerived;
	private String simsUrl;
	private TimeSeriesCorrectedData thirdReferenceTimeSeries;
	private TimeSeriesCorrectedData thirdStatDerived;
	private String waterdataUrl;

	public TimeSeriesCorrectedData getFirstStatDerived() {
		return firstStatDerived;
	}
	public void setFirstStatDerived(TimeSeriesCorrectedData firstStatDerived) {
		this.firstStatDerived = firstStatDerived;
	}
	public TimeSeriesCorrectedData getSecondStatDerived() {
		return secondStatDerived;
	}
	public void setSecondStatDerived(TimeSeriesCorrectedData secondStatDerived) {
		this.secondStatDerived = secondStatDerived;
	}
	public TimeSeriesCorrectedData getThirdStatDerived() {
		return thirdStatDerived;
	}
	public void setThirdStatDerived(TimeSeriesCorrectedData thirdStatDerived) {
		this.thirdStatDerived = thirdStatDerived;
	}
	public TimeSeriesCorrectedData getFourthStatDerived() {
		return fourthStatDerived;
	}
	public void setFourthStatDerived(TimeSeriesCorrectedData fourthStatDerived) {
		this.fourthStatDerived = fourthStatDerived;
	}
	public TimeSeriesCorrectedData getFirstReferenceTimeSeries() {
		return firstReferenceTimeSeries;
	}
	public void setFirstReferenceTimeSeries(TimeSeriesCorrectedData firstReferenceTimeSeries) {
		this.firstReferenceTimeSeries = firstReferenceTimeSeries;
	}
	public TimeSeriesCorrectedData getSecondReferenceTimeSeries() {
		return secondReferenceTimeSeries;
	}
	public void setSecondReferenceTimeSeries(TimeSeriesCorrectedData secondReferenceTimeSeries) {
		this.secondReferenceTimeSeries = secondReferenceTimeSeries;
	}
	public TimeSeriesCorrectedData getThirdReferenceTimeSeries() {
		return thirdReferenceTimeSeries;
	}
	public void setThirdReferenceTimeSeries(TimeSeriesCorrectedData thirdReferenceTimeSeries) {
		this.thirdReferenceTimeSeries = thirdReferenceTimeSeries;
	}
	public TimeSeriesCorrectedData getComparisonSeries() {
		return comparisonSeries;
	}
	public void setComparisonSeries(TimeSeriesCorrectedData comparisonSeries) {
		this.comparisonSeries = comparisonSeries;
	}
	public List<Qualifier> getPrimarySeriesQualifiers() {
		return primarySeriesQualifiers;
	}
	public void setPrimarySeriesQualifiers(List<Qualifier> primarySeriesQualifiers) {
		this.primarySeriesQualifiers = primarySeriesQualifiers;
	}
	public List<Approval> getPrimarySeriesApprovals() {
		return primarySeriesApprovals;
	}
	public void setPrimarySeriesApprovals(List<Approval> primarySeriesApprovals) {
		this.primarySeriesApprovals = primarySeriesApprovals;
	}
	public List<FieldVisitMeasurement> getFieldVisitMeasurements() {
		return fieldVisitMeasurements;
	}
	public void setFieldVisitMeasurements(List<FieldVisitMeasurement> fieldVisitMeasurements) {
		this.fieldVisitMeasurements = fieldVisitMeasurements;
	}
	public DvHydrographReportMetadata getReportMetadata() {
		return reportMetadata;
	}
	public void setReportMetadata(DvHydrographReportMetadata reportMetadata) {
		this.reportMetadata = reportMetadata;
	}
	public String getSimsUrl() {
		return simsUrl;
	}
	public void setSimsUrl(String simsUrl) {
		this.simsUrl = simsUrl;
	}
	public String getWaterdataUrl() {
		return waterdataUrl;
	}
	public void setWaterdataUrl(String waterdataUrl) {
		this.waterdataUrl = waterdataUrl;
	}
	public MinMaxData getMaxMinData() {
		return maxMinData;
	}
	public void setMaxMinData(MinMaxData maxMinData) {
		this.maxMinData = maxMinData;
	}

}
