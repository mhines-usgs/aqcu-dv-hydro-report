package gov.usgs.aqcu.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.aquaticinformatics.aquarius.sdk.timeseries.servicemodels.Publish.DoubleWithDisplay;

public class DoubleWithDisplayUtilTest {

	@Test
	public void getRoundedValue_NullTest() {
		assertNull(DoubleWithDisplayUtil.getRoundedValue(null));
	}

	@Test
	public void getRoundedValue_NullDisplayAndNumericTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay().setDisplay(null).setNumeric(null);
		assertNull(DoubleWithDisplayUtil.getRoundedValue(dwd));
	}

	@Test
	public void getRoundedValue_NullDisplayTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay().setDisplay(null).setNumeric(Double.valueOf("123.456"));
		assertEquals(new BigDecimal("123.456"), DoubleWithDisplayUtil.getRoundedValue(dwd));
	}

	@Test
	public void getRoundedValue_GapDisplayTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay().setDisplay(DoubleWithDisplayUtil.GAP_MARKER_POINT_VALUE)
				.setNumeric(Double.valueOf("123.456"));
		assertEquals(new BigDecimal("123.456"), DoubleWithDisplayUtil.getRoundedValue(dwd));
	}

	@Test
	public void getRoundedValue_DisplayTest() {
		DoubleWithDisplay dwd = new DoubleWithDisplay().setDisplay("654.321").setNumeric(Double.valueOf("123.456"));
		assertEquals(new BigDecimal("654.321"), DoubleWithDisplayUtil.getRoundedValue(dwd));
	}
}
