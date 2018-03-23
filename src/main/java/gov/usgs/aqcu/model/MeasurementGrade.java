package gov.usgs.aqcu.model;

import java.math.BigDecimal;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.MeasurementGradeType;

/**
 * Represents and Describes the possible measurement grades and their associated values
 */
public enum MeasurementGrade {

	EXCELLENT(new BigDecimal("0.0200")),

	GOOD(new BigDecimal("0.0500")),

	FAIR(new BigDecimal("0.0800")),

	POOR(new BigDecimal("0.100"));

	private final BigDecimal percentageOfError;

	private MeasurementGrade(BigDecimal inPercentageOfError) {
		percentageOfError = inPercentageOfError;
	}

	public BigDecimal getPercentageOfError() {
		return percentageOfError;
	}

	/**
	 * this is a safe operation that will catch the runtime exception if not
	 * valid
	 *
	 * @param name The name of the measurement grade
	 * @see MeasurementGradeType
	 * @return null if not valid otherwise the correct enum for the string
	 * value.
	 */
	public static MeasurementGrade valueFrom(String name) {
		MeasurementGrade ret = null;
		try {
			ret = valueOf(name.toUpperCase());
		} catch (IllegalArgumentException iae) {
			//not a valid measurementGrade
		}
		if (ret == null) {
			ret = POOR; //default missing or other grades to poor
		}
		return ret;
	}

}