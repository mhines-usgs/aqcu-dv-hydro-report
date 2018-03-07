package gov.usgs.aqcu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Approval;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.Qualifier;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDataServiceResponse;
import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesDescription;

public class DvHydroReport {	
	private TimeSeriesDataServiceResponse primaryTsData;
	private TimeSeriesDescription primaryTsMetadata;
	private List<Qualifier> primarySeriesQualifiers;
	private List<Approval> primarySeriesApprovals;
	private DvHydroMetadata reportMetadata;
	
	public DvHydroReport() {
		primaryTsData = new TimeSeriesDataServiceResponse();
		primaryTsMetadata = new TimeSeriesDescription();
		primarySeriesQualifiers = new ArrayList<>();
		primarySeriesApprovals = new ArrayList<>();
		reportMetadata = new DvHydroMetadata();
	}
	
	public DvHydroMetadata getReportMetadata() {
		return reportMetadata;
	}
	
	public TimeSeriesDataServiceResponse getPrimaryTsData() {
		return primaryTsData;
	}
	
	public TimeSeriesDescription getPrimaryTsMetadata() {
		return primaryTsMetadata;
	}
	
	public void setReportMetadata(DvHydroMetadata val) {
		reportMetadata = val;
	}
	
	public void setPrimaryTsData(TimeSeriesDataServiceResponse val) {
		primaryTsData = val;
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
}
	
