package gov.usgs.aqcu.model;

import java.util.ArrayList;
import java.util.List;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Approval;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;

public class DvHydroReport {	
	private DvHydroCorrectedData firstStatDerived;
	private DvHydroCorrectedData secondStatDerived;
	private DvHydroCorrectedData thirdStatDerived;
	private DvHydroCorrectedData fourthStatDerived;
	private DvHydroCorrectedData firstReferenceTimeSeries;
	private DvHydroCorrectedData secondReferenceTimeSeries;
	private DvHydroCorrectedData thirdReferenceTimeSeries;
	private DvHydroCorrectedData comparisonSeries;
	private TimeSeriesDescription primaryTsMetadata;
	private List<Qualifier> primarySeriesQualifiers;
	private List<Approval> primarySeriesApprovals;
	private List<AqcuFieldVisitMeasurement> fieldVisitMeasurements;
	private DvHydroMetadata reportMetadata;
	
	public DvHydroReport() {
		firstStatDerived = new DvHydroCorrectedData();
		secondStatDerived = new DvHydroCorrectedData();
		thirdStatDerived = new DvHydroCorrectedData();
		fourthStatDerived = new DvHydroCorrectedData();
		firstReferenceTimeSeries = new DvHydroCorrectedData();
		secondReferenceTimeSeries = new DvHydroCorrectedData();
		thirdReferenceTimeSeries = new DvHydroCorrectedData();
		comparisonSeries = new DvHydroCorrectedData();
		primaryTsMetadata = new TimeSeriesDescription();
		primarySeriesQualifiers = new ArrayList<>();
		primarySeriesApprovals = new ArrayList<>();
		reportMetadata = new DvHydroMetadata();
	}
	
	public DvHydroMetadata getReportMetadata() {
		return reportMetadata;
	}
	
	public DvHydroCorrectedData getFirstStatDerivedData() {
		return firstStatDerived;
	}
	
	public DvHydroCorrectedData getSecondStatDerivedData() {
		return secondStatDerived;
	}
	
	public DvHydroCorrectedData getThirdStatDerivedData() {
		return thirdStatDerived;
	}
	
	public DvHydroCorrectedData getFourthStatDerivedData() {
		return fourthStatDerived;
	}
	
	public DvHydroCorrectedData getFirstReferenceData() {
		return firstReferenceTimeSeries;
	}
	
	public DvHydroCorrectedData getSecondReferenceData() {
		return secondReferenceTimeSeries;
	}
	
	public DvHydroCorrectedData getThirdReferenceData() {
		return thirdReferenceTimeSeries;
	}
	
	public DvHydroCorrectedData getComparisonSeriesData() {
		return comparisonSeries;
	}
	
	public void setReportMetadata(DvHydroMetadata val) {
		reportMetadata = val;
	}
	
	public void setFirstStatDerivedData(DvHydroCorrectedData val) {
		firstStatDerived = val;
	}
	
	public void setSecondStatDerivedData(DvHydroCorrectedData val) {
		secondStatDerived = val;
	}
	
	public void setThirdStatDerivedData(DvHydroCorrectedData val) {
		thirdStatDerived = val;
	}
	
	public void setFourthStatDerivedData(DvHydroCorrectedData val) {
		fourthStatDerived = val;
	}
	
	public void setFirstReferenceData(DvHydroCorrectedData val) {
		firstReferenceTimeSeries = val;
	}
	
	public void setSecondReferenceData(DvHydroCorrectedData val) {
		secondReferenceTimeSeries = val;
	}
	
	public void setThirdReferenceData(DvHydroCorrectedData val) {
		thirdReferenceTimeSeries = val;
	}
	
	public void setComparisonSeriesData(DvHydroCorrectedData val) {
		comparisonSeries = val;
	}
	
	public void setPrimaryTsMetadata(TimeSeriesDescription val) {
		primaryTsMetadata = val;
	}
	
	public void setQualifier(List<Qualifier> val) {
		primarySeriesQualifiers = val;
	}
	
	public void setApproval(List<Approval> val) {
		primarySeriesApprovals = val;
	}
	
	public void setFieldVisitMeasurements(List<AqcuFieldVisitMeasurement> val) {
		fieldVisitMeasurements = val;
	}
}
	
