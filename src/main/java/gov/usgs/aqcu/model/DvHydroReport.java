package gov.usgs.aqcu.model;

import java.util.ArrayList;
import java.util.List;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Approval;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;

public class DvHydroReport {	
	private DvHydroCorrectedData firstStatDerived;
	private DvHydroCorrectedData secondStatDerived;
	private DvHydroCorrectedData thirdStatDerived;
	private DvHydroCorrectedData fourthStatDerived;
	private DvHydroCorrectedData firstReferenceTimeSeries;
	private DvHydroCorrectedData secondReferenceTimeSeries;
	private DvHydroCorrectedData thirdReferenceTimeSeries;
	private DvHydroCorrectedData comparisonSeries;
	private ArrayList<Qualifier> primarySeriesQualifiers;
	private ArrayList<Approval> primarySeriesApprovals;
	private List<AqcuFieldVisitMeasurement> fieldVisitMeasurements;
	private DvHydroMetadata reportMetadata;
	private String simsUrl;
	private String waterdataUrl;
	private String maxMinData;
	private List<String> readings;
	public DvHydroCorrectedData getFirstStatDerived() {
		return firstStatDerived;
	}
	public void setFirstStatDerived(DvHydroCorrectedData firstStatDerived) {
		this.firstStatDerived = firstStatDerived;
	}
	public DvHydroCorrectedData getSecondStatDerived() {
		return secondStatDerived;
	}
	public void setSecondStatDerived(DvHydroCorrectedData secondStatDerived) {
		this.secondStatDerived = secondStatDerived;
	}
	public DvHydroCorrectedData getThirdStatDerived() {
		return thirdStatDerived;
	}
	public void setThirdStatDerived(DvHydroCorrectedData thirdStatDerived) {
		this.thirdStatDerived = thirdStatDerived;
	}
	public DvHydroCorrectedData getFourthStatDerived() {
		return fourthStatDerived;
	}
	public void setFourthStatDerived(DvHydroCorrectedData fourthStatDerived) {
		this.fourthStatDerived = fourthStatDerived;
	}
	public DvHydroCorrectedData getFirstReferenceTimeSeries() {
		return firstReferenceTimeSeries;
	}
	public void setFirstReferenceTimeSeries(DvHydroCorrectedData firstReferenceTimeSeries) {
		this.firstReferenceTimeSeries = firstReferenceTimeSeries;
	}
	public DvHydroCorrectedData getSecondReferenceTimeSeries() {
		return secondReferenceTimeSeries;
	}
	public void setSecondReferenceTimeSeries(DvHydroCorrectedData secondReferenceTimeSeries) {
		this.secondReferenceTimeSeries = secondReferenceTimeSeries;
	}
	public DvHydroCorrectedData getThirdReferenceTimeSeries() {
		return thirdReferenceTimeSeries;
	}
	public void setThirdReferenceTimeSeries(DvHydroCorrectedData thirdReferenceTimeSeries) {
		this.thirdReferenceTimeSeries = thirdReferenceTimeSeries;
	}
	public DvHydroCorrectedData getComparisonSeries() {
		return comparisonSeries;
	}
	public void setComparisonSeries(DvHydroCorrectedData comparisonSeries) {
		this.comparisonSeries = comparisonSeries;
	}
	public ArrayList<Qualifier> getPrimarySeriesQualifiers() {
		return primarySeriesQualifiers;
	}
	public void setPrimarySeriesQualifiers(ArrayList<Qualifier> primarySeriesQualifiers) {
		this.primarySeriesQualifiers = primarySeriesQualifiers;
	}
	public ArrayList<Approval> getPrimarySeriesApprovals() {
		return primarySeriesApprovals;
	}
	public void setPrimarySeriesApprovals(ArrayList<Approval> primarySeriesApprovals) {
		this.primarySeriesApprovals = primarySeriesApprovals;
	}
	public List<AqcuFieldVisitMeasurement> getFieldVisitMeasurements() {
		return fieldVisitMeasurements;
	}
	public void setFieldVisitMeasurements(List<AqcuFieldVisitMeasurement> fieldVisitMeasurements) {
		this.fieldVisitMeasurements = fieldVisitMeasurements;
	}
	public DvHydroMetadata getReportMetadata() {
		return reportMetadata;
	}
	public void setReportMetadata(DvHydroMetadata reportMetadata) {
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
	public String getMaxMinData() {
		return maxMinData;
	}
	public void setMaxMinData(String maxMinData) {
		this.maxMinData = maxMinData;
	}
	public List<String> getReadings() {
		return readings;
	}
	public void setReadings(List<String> readings) {
		this.readings = readings;
	}
	
//	public DvHydroReport() {
//		firstStatDerived = new DvHydroCorrectedData();
//		secondStatDerived = new DvHydroCorrectedData();
//		thirdStatDerived = new DvHydroCorrectedData();
//		fourthStatDerived = new DvHydroCorrectedData();
//		firstReferenceTimeSeries = new DvHydroCorrectedData();
//		secondReferenceTimeSeries = new DvHydroCorrectedData();
//		thirdReferenceTimeSeries = new DvHydroCorrectedData();
//		comparisonSeries = new DvHydroCorrectedData();
////		primaryTsMetadata = new TimeSeriesDescription();
//		primarySeriesQualifiers = new ArrayList<>();
//		primarySeriesApprovals = new ArrayList<>();
//		reportMetadata = new DvHydroMetadata();
//	}

}
