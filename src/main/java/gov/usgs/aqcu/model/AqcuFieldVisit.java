package gov.usgs.aqcu.model;

import java.time.temporal.Temporal;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * The AQCU representation of a Field Visit.
 * 
 * @author thongsav
 */
public class AqcuFieldVisit {
	private String locationIdentifier;

	private Temporal startTime;
	private Temporal endTime;

	private String identifier;

	private Boolean isValid;
	private Temporal lastModified;

	private String party;
	private String remarks;
	private String weather;

	/**
	 * Constructor that creates an AQCU FieldVisit with all of the necessary and
	 * relevant  parameters.
	 * 
	 * @param locationIdentifier The location identifier associated with the location where this field visit occurred.
	 * @param startTime The date & time that this field visit began
	 * @param endTime The date & time that this field visit ended
	 * @param identifier The unique identifier for this field visit
	 * @param isValid Whether or not this is a valid field visit
	 * @param lastModified The last date & time that this field visit entry was modified
	 * @param party The associated party information describing the participants in this field visit
	 * @param remarks Any additional comments and remarks made about this field visit
	 * @param weather The weather conditions at the time of this field visit
	 */
	public AqcuFieldVisit(String locationIdentifier, Temporal startTime, Temporal endTime, String identifier,
			Boolean isValid, Temporal lastModified, String party, String remarks, String weather) {
		this.locationIdentifier = locationIdentifier;
		this.startTime = startTime;
		this.endTime = endTime;
		this.identifier = identifier;
		this.isValid = isValid;
		this.lastModified = lastModified;
		this.party = party;
		this.remarks = remarks;
		this.weather = weather;
	}

	public AqcuFieldVisit() {
	}

	public String getLocationIdentifier() {
		return locationIdentifier;
	}

	public Temporal getStartTime() {
		return startTime;
	}

	public Temporal getEndTime() {
		return endTime;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public Temporal getLastModified() {
		return lastModified;
	}

	public String getParty() {
		return party;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getWeather() {
		return weather;
	}

	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}

	public void setStartTime(Temporal startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Temporal endTime) {
		this.endTime = endTime;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public void setLastModified(Temporal lastModified) {
		this.lastModified = lastModified;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

}
