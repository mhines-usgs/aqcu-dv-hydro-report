package gov.usgs.aqcu.model;

import java.util.List;
import java.util.ArrayList;

public class DvHydroCorrectedData {
	private String unit;
	private String type;
	private List<DateRange> estimatedPeriods;
	private List<AqcuPoint> points;
	
	public DvHydroCorrectedData() {
		type = new String();
		estimatedPeriods = new ArrayList<>();
		points = new ArrayList<>();
	}
	
	public String getUnit (){
		return unit;
	}
	
	public String getType (){
		return type;
	}
	
	public List<DateRange> getEstimatedPeriods(){
		return estimatedPeriods;
	}
	
	public List<AqcuPoint> getPoints(){
		return points;		
	}
	
	public void setUnit(String val) {
		unit = val;
	}
	
	public void setType(String val) {
		type = val;
	}
	
	public void setEstimatedPeriods(List<DateRange> val) {
		estimatedPeriods = val;
	}
	
	public void setPoints(List<AqcuPoint> val) {
		points = val;
	}
}
