package gov.usgs.aqcu.builder;

import java.time.ZoneOffset;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.TimeSeriesPoint;

import gov.usgs.aqcu.model.DataGapII;
import gov.usgs.aqcu.util.AqcuTimeUtils;

@Service
public class DataGapListBuilderServiceII {

	public List<DataGapII> buildGapList(List<TimeSeriesPoint> timeSeriesPoints, boolean isDaily, ZoneOffset zoneOffset) {
		List<DataGapII> gapList = new ArrayList<>();

		for(int i = 0; i < timeSeriesPoints.size(); i++) {
			TimeSeriesPoint point = timeSeriesPoints.get(i);

			if(point.getValue().getNumeric() == null) {
				//Gap Marker Found			
				Temporal preTime = (i > 0) ? AqcuTimeUtils.getTemporal(timeSeriesPoints.get(i-1).getTimestamp(), isDaily, zoneOffset) : null;
				Temporal postTime = (i < (timeSeriesPoints.size() -1)) ? AqcuTimeUtils.getTemporal(timeSeriesPoints.get(i+1).getTimestamp(), isDaily, zoneOffset) : null;
				DataGapII gap = new DataGapII();
				gap.setStartTime(preTime);
				gap.setEndTime(postTime);
				gapList.add(gap);
			}
		}

		return gapList;
	}

}
