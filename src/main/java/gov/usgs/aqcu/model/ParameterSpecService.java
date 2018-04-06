package gov.usgs.aqcu.model;
//TODO
/**
 * Service that checks the specifications of the parameter type of a given timeseries
 * in order to generalize it to a more general type.
 * 
 * @author thongsav
 */
public class ParameterSpecService {

	/**
	 * Checks whether the provided timeseries' parameter is a Ground Water Parameter
	 * 
	 * @param timeseriesMetaData The timeseries metadata to check the parameter type of
	 * @return Whether or not the parameter is a Ground Water Parameter
	 */
	public static boolean isGwParamater(TimeSeriesMetadata timeseriesMetaData) {
		return GroundWaterParameters.anyDisplayStartsWith(timeseriesMetaData.getParameter());
	}
	
	/**
	 * Checks whether the provided timeseries' parameter is a Discharge Parameter
	 * 
	 * @param timeseriesMetaData The timeseries metadata to check the parameter type of
	 * @return Whether or not the parameter is a Discharge Parameter
	 */
	public static boolean isDischargeParameter(TimeSeriesMetadata timeseriesMetaData) {
		String parameter = timeseriesMetaData.getParameter();
		return (parameter.equals("Discharge"));
	}
	
	/**
	 * Checks whether the provided timeseries' parameter is an Inverted Ground Water Parameter
	 * 
	 * @param timeseriesMetaData The timeseries metadata to check the parameter type of
	 * @return Whether or not the parameter is an Inverted Ground Water Parameter
	 */
	public static boolean isInvertedGwParam(TimeSeriesMetadata timeseriesMetaData) {
		GroundWaterParameters gwParam = GroundWaterParameters.getByDisplayName(timeseriesMetaData.getParameter());
		if(gwParam != null) {
			return gwParam.isInverted();
		} else {
			return false;
		}
	}
}
