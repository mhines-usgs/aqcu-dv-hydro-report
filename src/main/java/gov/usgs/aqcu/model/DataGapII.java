package gov.usgs.aqcu.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.Temporal;

//TODO Test/move to framework
public class DataGapII {
	private Temporal startTime = null;
	private Temporal endTime = null;
	private BigDecimal durationInHours = null;
	private DataGapExtent gapExtent = DataGapExtent.OVER_ALL;

	public DataGapII() {}

	public DataGapII(Temporal startTime, Temporal endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		calculateDurationInHours();
		calculateGapExtent();
	}

	public Temporal getStartTime() {
		return startTime;
	}

	public Temporal getEndTime() {
		return endTime;
	}

	public DataGapExtent getGapExtent() {
		return gapExtent;
	}

	public BigDecimal getDurationInHours() {
		return durationInHours;
	}

	public void setStartTime(Temporal val) {
		startTime= val;
		calculateDurationInHours();
		calculateGapExtent();
	}

	public void setEndTime(Temporal val) {
		endTime = val;
		calculateDurationInHours();
		calculateGapExtent();
	}

	protected void calculateDurationInHours() {
		if(startTime != null && endTime != null) {
			if(startTime instanceof Instant && endTime instanceof Instant) {
				durationInHours = BigDecimal.valueOf(Duration.between(startTime, endTime).getSeconds() / 3600.0);
			} else if(startTime instanceof LocalDate && endTime instanceof LocalDate) {
				durationInHours = BigDecimal.valueOf(Duration.between(((LocalDate) startTime).atStartOfDay(ZoneId.of("Z")), ((LocalDate) endTime).atStartOfDay(ZoneId.of("Z"))).getSeconds() / 3600.0);
			}
		} else {
			durationInHours = null;
		}
	}

	protected void calculateGapExtent() {
		if(startTime != null && endTime != null) {
			gapExtent = DataGapExtent.CONTAINED;
		} else if(startTime == null && endTime != null) {
			gapExtent = DataGapExtent.OVER_START;
		} else if(startTime != null && endTime == null) {
			gapExtent = DataGapExtent.OVER_END;
		} else {
			gapExtent = DataGapExtent.OVER_ALL;
		}
	}
}