package gov.usgs.aqcu.model;

import java.time.temporal.Temporal;

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

	/**
	 * Default Constructor for FieldVisit
	 */
	public AqcuFieldVisit() {
	}

	/**
	 *
	 * @return The location identifier 
	 */
	public String getLocationIdentifier() {
		return locationIdentifier;
	}

	/**
	 *
	 * @return The start date & time
	 */
	public Temporal getStartTime() {
		return startTime;
	}

	/**
	 *
	 * @return The end date & time
	 */
	public Temporal getEndTime() {
		return endTime;
	}

	/**
	 *
	 * @return The unique identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 *
	 * @return Whether or not this field visit was valid
	 */
	public Boolean getIsValid() {
		return isValid;
	}

	/**
	 *
	 * @return The date & time when the field visit was last modified
	 */
	public Temporal getLastModified() {
		return lastModified;
	}

	/**
	 *
	 * @return The associated party
	 */
	public String getParty() {
		return party;
	}

	/**
	 *
	 * @return The associated remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 *
	 * @return The weather conditions
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 *
	 * @param locationIdentifier Location Identifier to set
	 */
	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}

	/**
	 *
	 * @param startTime Start Date & Time to set
	 */
	public void setStartTime(Temporal startTime) {
		this.startTime = startTime;
	}

	/**
	 *
	 * @param endTime End Date & Time to set
	 */
	public void setEndTime(Temporal endTime) {
		this.endTime = endTime;
	}

	/**
	 *
	 * @param identifier Unique Identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 *
	 * @param isValid Set whether or not this field visit was valid
	 */
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 *
	 * @param lastModified The last modified date & time to set
	 */
	public void setLastModified(Temporal lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 *
	 * @param party The associated party information to set
	 */
	public void setParty(String party) {
		this.party = party;
	}

	/**
	 *
	 * @param remarks The associated remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 *
	 * @param weather The weather conditions to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	
}
