package gov.usgs.aqcu.model;

import java.util.List;
import java.util.ArrayList;

public class DvHydroCorrectedData {

	//Required Properties
	private List<String> approvals;
	private List<String> gaps;
	private List<String> gapTolerances;
	private List<String> grades;
	private boolean isVolumetricFlow;
	private String name;
	private List<AqcuPoint> points;
	private List<String> qualifiers;
	private String type;
	private String unit;

	//Optional Properties
	private List<DateRange> estimatedPeriods;
	private List<String> methods;
	private List<String> notes;
	private String description;
	private String requestedStartTime;
	private String requestedEndTime;
	private String startTime;
	private String endTime;
	private List<String> interpolationTypes;

//	public DvHydroCorrectedData() {
//	type = new String();
//	estimatedPeriods = new ArrayList<>();
//	points = new ArrayList<>();
//}
	public List<String> getApprovals() {
		return approvals;
	}
	public void setApprovals(List<String> approvals) {
		this.approvals = approvals;
	}
	public List<String> getGaps() {
		return gaps;
	}
	public void setGaps(List<String> gaps) {
		this.gaps = gaps;
	}
	public List<String> getGapTolerances() {
		return gapTolerances;
	}
	public void setGapTolerances(List<String> gapTolerances) {
		this.gapTolerances = gapTolerances;
	}
	public List<String> getGrades() {
		return grades;
	}
	public void setGrades(List<String> grades) {
		this.grades = grades;
	}
	public boolean isVolumetricFlow() {
		return isVolumetricFlow;
	}
	public void setVolumetricFlow(boolean isVolumetricFlow) {
		this.isVolumetricFlow = isVolumetricFlow;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AqcuPoint> getPoints() {
		return points;
	}
	public void setPoints(List<AqcuPoint> points) {
		this.points = points;
	}
	public List<String> getQualifiers() {
		return qualifiers;
	}
	public void setQualifiers(List<String> qualifiers) {
		this.qualifiers = qualifiers;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<DateRange> getEstimatedPeriods() {
		return estimatedPeriods;
	}
	public void setEstimatedPeriods(List<DateRange> estimatedPeriods) {
		this.estimatedPeriods = estimatedPeriods;
	}
	public List<String> getMethods() {
		return methods;
	}
	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	public List<String> getNotes() {
		return notes;
	}
	public void setNotes(List<String> notes) {
		this.notes = notes;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRequestedStartTime() {
		return requestedStartTime;
	}
	public void setRequestedStartTime(String requestedStartTime) {
		this.requestedStartTime = requestedStartTime;
	}
	public String getRequestedEndTime() {
		return requestedEndTime;
	}
	public void setRequestedEndTime(String requestedEndTime) {
		this.requestedEndTime = requestedEndTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public List<String> getInterpolationTypes() {
		return interpolationTypes;
	}
	public void setInterpolationTypes(List<String> interpolationTypes) {
		this.interpolationTypes = interpolationTypes;
	}

}
