package gov.usgs.aqcu.parameter;

import gov.usgs.aqcu.validation.StatDerivedIdentifierPresent;

@StatDerivedIdentifierPresent
public class DvHydroRequestParameters extends RequestParameters {

	private String firstStatDerivedIdentifier;
	private String secondStatDerivedIdentifier;
	private String thirdStatDerivedIdentifier;
	private String fourthStatDerivedIdentifier;
	private String firstReferenceIdentifier;
	private String secondReferenceIdentifier;
	private String thirdReferenceIdentifier;
	private String comparisonTimeseriesIdentifier;
	private boolean excludeZeroNegative;
	private boolean excludeDiscrete;
	private boolean excludeMinMax;

	public String getFirstStatDerivedIdentifier() {
		return firstStatDerivedIdentifier;
	}
	public void setFirstStatDerivedIdentifier(String firstStatDerivedIdentifier) {
		this.firstStatDerivedIdentifier = firstStatDerivedIdentifier;
	}
	public String getSecondStatDerivedIdentifier() {
		return secondStatDerivedIdentifier;
	}
	public void setSecondStatDerivedIdentifier(String secondStatDerivedIdentifier) {
		this.secondStatDerivedIdentifier = secondStatDerivedIdentifier;
	}
	public String getThirdStatDerivedIdentifier() {
		return thirdStatDerivedIdentifier;
	}
	public void setThirdStatDerivedIdentifier(String thirdStatDerivedIdentifier) {
		this.thirdStatDerivedIdentifier = thirdStatDerivedIdentifier;
	}
	public String getFourthStatDerivedIdentifier() {
		return fourthStatDerivedIdentifier;
	}
	public void setFourthStatDerivedIdentifier(String fourthStatDerivedIdentifier) {
		this.fourthStatDerivedIdentifier = fourthStatDerivedIdentifier;
	}
	public String getFirstReferenceIdentifier() {
		return firstReferenceIdentifier;
	}
	public void setFirstReferenceIdentifier(String firstReferenceIdentifier) {
		this.firstReferenceIdentifier = firstReferenceIdentifier;
	}
	public String getSecondReferenceIdentifier() {
		return secondReferenceIdentifier;
	}
	public void setSecondReferenceIdentifier(String secondReferenceIdentifier) {
		this.secondReferenceIdentifier = secondReferenceIdentifier;
	}
	public String getThirdReferenceIdentifier() {
		return thirdReferenceIdentifier;
	}
	public void setThirdReferenceIdentifier(String thirdReferenceIdentifier) {
		this.thirdReferenceIdentifier = thirdReferenceIdentifier;
	}
	public String getComparisonTimeseriesIdentifier() {
		return comparisonTimeseriesIdentifier;
	}
	public void setComparisonTimeseriesIdentifier(String comparisonTimeseriesIdentifier) {
		this.comparisonTimeseriesIdentifier = comparisonTimeseriesIdentifier;
	}
	public boolean isExcludeZeroNegative() {
		return excludeZeroNegative;
	}
	public void setExcludeZeroNegative(boolean excludeZeroNegative) {
		this.excludeZeroNegative = excludeZeroNegative;
	}
	public boolean isExcludeDiscrete() {
		return excludeDiscrete;
	}
	public void setExcludeDiscrete(boolean excludeDiscrete) {
		this.excludeDiscrete = excludeDiscrete;
	}
	public boolean isExcludeMinMax() {
		return excludeMinMax;
	}
	public void setExcludeMinMax(boolean excludeMinMax) {
		this.excludeMinMax = excludeMinMax;
	}

}
