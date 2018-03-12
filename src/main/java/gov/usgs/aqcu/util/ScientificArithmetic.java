package gov.usgs.aqcu.util;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class ScientificArithmetic {
	private static final Logger log = LoggerFactory.getLogger(ScientificArithmetic.class);
	
	/**
	 * Performs {@link java.math.BigDecimal#add(java.math.BigDecimal) add} while
	 * taking into account the application's significant figure and rounding rules
	 * @param a
	 * @param b
	 * @return 
	 * @see java.math.BigDecimal#add(java.math.BigDecimal) add
	 */
	public static BigDecimal add(BigDecimal a, BigDecimal b) {
		BigDecimal result = null;
		result = a.add(b);
		return result;
	}
	
	/**
	 * Performs {@link java.math.BigDecimal#subtract(java.math.BigDecimal) subtract} while
	 * taking into account the application's significant figure and rounding rules
	 * @param a
	 * @param b
	 * @return 
	 * @see java.math.BigDecimal#subtract(java.math.BigDecimal) subtract
	 */
	public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
		BigDecimal result = null;
		result = a.subtract(b);
		return result;
	}
	
	/**
	 * Performs {@link java.math.BigDecimal#multiply(java.math.BigDecimal) multiply} while
	 * taking into account the application's significant figure and rounding rules
	 * @param a
	 * @param b
	 * @return 
	 * @see java.math.BigDecimal#multiply(java.math.BigDecimal) multiply
	 */
	public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
		BigDecimal result = null;
		result = a.multiply(b);
		return result;
	}
	
	/**
	 * Performs {@link java.math.BigDecimal#divide(java.math.BigDecimal) divide} while
	 * taking into account the application's significant figure and rounding rules
	 * @param a
	 * @param b
	 * @return 
	 * @see java.math.BigDecimal#divide(java.math.BigDecimal) divide
	 */
	public static BigDecimal divide(BigDecimal a, BigDecimal b) {
		BigDecimal result = null;
		result = a.divide(b);
		return result;
	}
	
}
