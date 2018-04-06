package gov.usgs.aqcu.model;

import java.util.Map;
//TODO
/**
 * The AQCU representation of the metadata associated with a Time Series
 * 
 * @author thongsav
 */
public class TimeSeriesMetadata {
	private String identifier;
	private String parameter;
	private String parameterIdentifier;
	private String nwisName;
	private String nwisPcode;
	private String unit;
	private String computation;
	private String timezone;
	private boolean inverted;
	private boolean groundWater;
	private boolean discharge;
	private String waterLevelType;
	private String seaLevelDatum;
	private String sublocation;
	private String timeSeriesType;
	private String period;
	private boolean publish;
	private boolean primary;
	private String uniqueId;
	private String comment;
	private String description;
	private int utcOffset;
	private Map<String, Object> extendedAttributes;
	
	/**
	 * Constructor that creates an AQCU TimeSeriesMetadata object with all of 
	 * the necessary and relevant  parameters.
	 * 
	 * @param identifier The unique identifier of the associated TimeSeries
	 * @param parameter The parameter of the associated TimeSeries
	 * @param parameterIdentifier The unique identifier code of the parameter
	 * @param nwisName The NWIS name of the Parameter
	 * @param nwisPcode The NWIS PCode of the Parameter
	 * @param unit The unit of the associated TimeSeries
	 * @param computation The computation type of the associated TimeSeries
	 * @param utcOffset The UTC Timezone offset of the associated TimeSeries
	 */
	public TimeSeriesMetadata(String identifier, String parameter, String parameterIdentifier, String nwisName,
			String nwisPcode, String unit, String computation, int utcOffset) {
		this.identifier = identifier;
		this.parameter = parameter;
		this.parameterIdentifier = parameterIdentifier;
		this.nwisName = nwisName;
		this.nwisPcode = nwisPcode;
		this.unit = unit;
		this.computation = computation;
		this.utcOffset = utcOffset;
		this.timezone = "Etc/GMT+" + (utcOffset * -1);
	}

	/**
	 *
	 * @return The identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 *
	 * @return The parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 *
	 * @return The parameter identifier
	 */
	public String getParameterIdentifier() {
		return parameterIdentifier;
	}

	/**
	 *
	 * @return The NWIS name of the parameter
	 */
	public String getNwisName() {
		return nwisName;
	}

	/**
	 *
	 * @return The NWIS PCode of the parameter
	 */
	public String getNwisPcode() {
		return nwisPcode;
	}

	/**
	 *
	 * @return The unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 *
	 * @return The computation
	 */
	public String getComputation() {
		return computation;
	}
	
	/**
	 *
	 * @return Whether or not the associated TimeSeries should be inverted
	 */
	public boolean isInverted() {
		return inverted;
	}

	/**
	 *
	 * @return Whether or not the associated TimeSeries is based on Ground Water
	 */
	public boolean isGroundWater() {
		return groundWater;
	}
	
	/**
	 *
	 * @return Whether or not the associated TimeSeries is based on Discharge
	 */
	public boolean isDischarge() {
		return discharge;
	}

	/**
	 *
	 * @return The water level type associated with the TimeSeries
	 */
	public String getWaterLevelType() {
		return waterLevelType;
	}
	
	/**
	 *
	 * @return The sea level datum associated with the TimeSeries
	 */
	public String getSeaLevelDatum() {
		return seaLevelDatum;
	}

	/**
	 *
	 * @return The Timezone of the associated TimeSeries
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 *
	 * @return The Sublocation of the associated TimeSeries
	 */
	public String getSublocation() {
		return sublocation;
	}

	/**
	 *
	 * @return The Time Series Type of the associated TimeSeries
	 */
	public String getTimeSeriesType() {
		return timeSeriesType;
	}

	/**
	 *
	 * @return The Period of the associated TimeSeries
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 *
	 * @return The Publish status of the associated TimeSeries
	 */
	public boolean getPublish() {
		return publish;
	}

	/**
	 *
	 * @return The Primary status of the associated TimeSeries
	 */
	public boolean getPrimary() {
		return primary;
	}
	
	/**
	 *
	 * @return The UniqueId of the associated TimeSeries
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 *
	 * @param identifier The identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 *
	 * @param parameter The parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 *
	 * @param parameterIdentifier The parameter identifier to set
	 */
	public void setParameterIdentifier(String parameterIdentifier) {
		this.parameterIdentifier = parameterIdentifier;
	}

	/**
	 *
	 * @param nwisName The NWIS name to set
	 */
	public void setNwisName(String nwisName) {
		this.nwisName = nwisName;
	}

	/**
	 *
	 * @param nwisPcode The NWIS PCode to set
	 */
	public void setNwisPcode(String nwisPcode) {
		this.nwisPcode = nwisPcode;
	}

	/**
	 *
	 * @param unit The unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 *
	 * @param computation The computation type to set
	 */
	public void setComputation(String computation) {
		this.computation = computation;
	}

	/**
	 *
	 * @param inverted Sets whether or not the associated TimeSeries is inverted
	 */
	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	/**
	 *
	 * @param groundWater Sets whether the associated TimeSeries is representing Ground Water
	 */
	public void setGroundWater(boolean groundWater) {
		this.groundWater = groundWater;
	}
	
	/**
	 *
	 * @param discharge Sets whether the associated TimeSeries is representing Discharge
	 */
	public void setDischarge(boolean discharge) {
		this.discharge = discharge;
	}

	/**
	 *
	 * @param waterLevelType Sets the Water Level Type
	 */
	public void setWaterLevelType(String waterLevelType) {
		this.waterLevelType = waterLevelType;
	}

	/**
	 *
	 * @param seaLevelDatum Sets the Sea Level Datum 
	 */
	public void setSeaLevelDatum(String seaLevelDatum) {
		this.seaLevelDatum = seaLevelDatum;
	}

	/**
	 *
	 * @param timezone Sets the TimeZone
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 *
	 * @param sublocation Sets the Sublocation
	 */
	public void setSublocation(String sublocation) {
		this.sublocation = sublocation;
	}

	/**
	 *
	 * @param timeSeriesType Sets the TimeSeriesType
	 */
	public void setTimeSeriesType(String timeSeriesType) {
		this.timeSeriesType = timeSeriesType;
	}

	/**
	 *
	 * @param period Sets the Period
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 *
	 * @param publish Sets the Publish
	 */
	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	/**
	 *
	 * @param primary Sets the Primary
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 *
	 * @param uniqueId Sets the UniqueId
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public Map<String, Object> getExtendedAttributes(){
	    return extendedAttributes;
	}
	
	public void setExtendedAttributes(Map<String, Object> extendedAttributes){
	    this.extendedAttributes = extendedAttributes;
	}
	
	public String getComment(){
	    return comment;
	}
	
	public void setComment(String comment){
	    this.comment = comment;
	}
	
	public String getDescription(){
	    return description;
	}
	
	public void setDescription(String description){
	    this.description = description;
	}
	
	public int getUtcOffset(){
	    return utcOffset;
	}
	
	public void setUtcOffset(int utcOffset){
	    this.utcOffset = utcOffset;
	}
}
