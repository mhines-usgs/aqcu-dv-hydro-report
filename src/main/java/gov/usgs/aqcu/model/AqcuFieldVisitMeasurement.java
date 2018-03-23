package gov.usgs.aqcu.model;

import static gov.usgs.aqcu.util.ScientificArithmetic.subtract;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.Instant;

/**
 * The AQCU representation of a Field Visit Measurement
 * 
 * @author thongsav, kmschoep
 */
public class AqcuFieldVisitMeasurement {

	//shift (input values from discharge
	private BigDecimal shiftInFeet;
	private BigDecimal errorMinShiftInFeet;
	private BigDecimal errorMaxShiftInFeet;

	private String identifier;
	private String controlCondition;
	private Instant measurementStartDate;
	private String ratingModelIdentifier;

	//discharge
	private BigDecimal discharge;
	private String dischargeUnits;
	private BigDecimal errorMinDischarge;
	private BigDecimal errorMaxDischarge;

	private String measurementNumber;
	private MeasurementGrade qualityRating;
	private boolean historic;
	private BigDecimal meanGageHeight;
	private String meanGageHeightUnits;
	private int shiftNumber;

	public AqcuFieldVisitMeasurement(){
	}

	/**
	 * Constructor that creates an AQCU FieldVisitMeasurement with all of the necessary and
	 * relevant  parameters.
	 * 
	 * @param inMeasurementNumber The actual measurement number
	 * @param controlCondition The control conditions related to this measurement
	 * @param inDischargeValue The discharge value associated with this measurement
	 * @param inDischargeUnits The units associated with the discharge value
	 * @param meanGageHeightUnits The units associated with the meanGageHeight value
	 * @param inErrorMaxDischarge The max error in the discharge value
	 * @param inErrorMinDischarge The min error in the discharge value
	 * @param grade The grade of this measurement
	 * @param inMeasurementStartDate The date & time when this measurement was beginning to be taken
	 * @param inIdentifier The unique identifier associated with this field visit measurement
	 */
	public AqcuFieldVisitMeasurement(String inMeasurementNumber, String controlCondition, BigDecimal inDischargeValue, String inDischargeUnits, String meanGageHeightUnits, 
			BigDecimal inErrorMaxDischarge, BigDecimal inErrorMinDischarge, MeasurementGrade grade, 
			Instant inMeasurementStartDate, String inIdentifier) {
		measurementNumber = inMeasurementNumber;
		this.controlCondition = controlCondition;
		this.discharge = inDischargeValue;
		this.dischargeUnits = inDischargeUnits;
		this.meanGageHeightUnits = meanGageHeightUnits;
		this.errorMinDischarge = inErrorMinDischarge;
		this.errorMaxDischarge = inErrorMaxDischarge;
		this.qualityRating = grade;
		this.measurementStartDate = inMeasurementStartDate;
		this.identifier = inIdentifier;
	}

	/**
	 * A utility function that calculates the error shifts in this measurement
	 * based on the provided error values and the existing measurement values.
	 * 
	 * @param inputValues The error values to apply to the measurement values in order to calculate the shifts
	 */
	public void calculateShifts(List<BigDecimal> inputValues) {
		if (inputValues.size() > 0 && inputValues.get(0) != null) {
			errorMaxShiftInFeet = subtract(inputValues.get(0), meanGageHeight);
		}
		if (inputValues.size() > 1 && inputValues.get(1) != null) {
			shiftInFeet = subtract(inputValues.get(1), meanGageHeight);
		}
		if (inputValues.size() > 2 && inputValues.get(2) != null) {
			errorMinShiftInFeet = subtract(inputValues.get(2), meanGageHeight);
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setHistoric(boolean historic) {
		this.historic = historic;
	}

	public BigDecimal getShiftInFeet() {
		return shiftInFeet;
	}

	public BigDecimal getErrorMinShiftInFeet() {
		return errorMinShiftInFeet;
	}

	public BigDecimal getErrorMaxShiftInFeet() {
		return errorMaxShiftInFeet;
	}

	public BigDecimal getDischarge() {
		return discharge;
	}

	public String getDischargeUnits() {
		return dischargeUnits;
	}

	public BigDecimal getErrorMinDischargeInFeet() {
		return errorMinDischarge;
	}

	public BigDecimal getErrorMaxDischargeInFeet() {
		return errorMaxDischarge;
	}

	public String getMeasurementNumber() {
		return measurementNumber;
	}

	public MeasurementGrade getQualityRating() {
		return qualityRating;
	}

	public boolean isHistoric() {
		return historic;
	}

	public void setMeanGageHeight(BigDecimal value) {
		meanGageHeight = value;
	}

	public BigDecimal getMeanGageHeight() {
		return meanGageHeight;
	}

	public String getMeanGageHeightUnits() {
		return meanGageHeightUnits;
	}

	public void setMeanGageHeightUnits(String meanGageHeightUnits) {
		this.meanGageHeightUnits = meanGageHeightUnits;
	}

	public Instant getMeasurementStartDate() {
		return measurementStartDate;
	}

	public BigDecimal[] getOutputValues() {
		return new BigDecimal[]{errorMaxDischarge, discharge, errorMinDischarge};
	}

	public String getRatingModelIdentifier() {
		return ratingModelIdentifier;
	}

	public void setRatingModelIdentifier(String inRatingModelIdentifier) {
		ratingModelIdentifier = inRatingModelIdentifier;
	}

	public void setShiftNumber(int inShiftNumber) {
		shiftNumber = inShiftNumber;
	}

	public int getShiftNumber() {
		return shiftNumber;
	}

	public String getControlCondition() {
		return controlCondition;
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

}
