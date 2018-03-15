package gov.usgs.aqcu.util;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DoubleWithDisplay;

/**
 *
 * @author kmschoep
 */
public class AqcuValues {
	private static final Logger log = LoggerFactory.getLogger(AqcuValues.class);
	private static final String GAP_MARKER_POINT_VALUE = "EMPTY";
	
	public BigDecimal getRoundedValue(DoubleWithDisplay referenceVal){
		BigDecimal ret;

		if (referenceVal != null) {
			String tmp = referenceVal.getDisplay();
			if (tmp == null || tmp.equals(GAP_MARKER_POINT_VALUE)) {
				ret = BigDecimal.valueOf(referenceVal.getNumeric()); //Should be null but just in case.
			} else {
				ret = new BigDecimal(tmp);
			}
		} else {
			ret = null;
		}
		return ret;
	}
	
}
