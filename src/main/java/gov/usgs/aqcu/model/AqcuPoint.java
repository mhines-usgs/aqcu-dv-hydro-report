package gov.usgs.aqcu.model;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.util.Objects;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.Temporal;

/**
 * The AQCU representation of a single data point of a Time Series
 * 
 * @author thongsav
 */
public class AqcuPoint {
	private Temporal time;
	
	private BigDecimal value;

	/**
	 *
	 * @return The date & time
	 */
	public Temporal getTime() {
		return time;
	}

	/**
	 *
	 * @param time The date & time to set
	 * @return The TimeSeriesPoint object
	 */
	public AqcuPoint setTime(Temporal time) {
		this.time = time;
		return this;
	}

	/**
	 *
	 * @return The value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 *
	 * @param value The value to set
	 * @return The TimeSeriesPoint object
	 */
	public AqcuPoint setValue(BigDecimal value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((getClass() != obj.getClass())
				&& (!AqcuPoint.class.isAssignableFrom(obj.getClass()))) {
			return false;
		}
		final AqcuPoint rhs = (AqcuPoint) obj;
		
		int compareTime = -1;
		if(this.getTime() instanceof LocalDate) {
			compareTime = ((LocalDate) this.getTime()).compareTo((LocalDate) rhs.getTime());
		} else if(this.getTime() instanceof OffsetDateTime) {
			compareTime = ((OffsetDateTime) this.getTime()).compareTo((OffsetDateTime) rhs.getTime());
		}
			
		return (Objects.equals(this.getTime(), rhs.getTime())
					|| (compareTime == 0))
				&& Objects.equals(this.getValue(), rhs.getValue());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getTime(), this.getValue());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("time", this.getTime())
				.add("value", this.getValue())
				.toString();
	}
}
