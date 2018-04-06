package gov.usgs.aqcu.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalSummaryStatisticsTest {

	private BigDecimal nintyNine = new BigDecimal(99);

	@Test
	public void initTest() {
		BigDecimalSummaryStatistics sum = new BigDecimalSummaryStatistics();
		assertEquals(new BigDecimal(Integer.MAX_VALUE), sum.getMin());
		assertEquals(new BigDecimal(Integer.MIN_VALUE), sum.getMax());
	}

	@Test
	public void acceptTest() {
		BigDecimalSummaryStatistics sum = new BigDecimalSummaryStatistics();
		sum.accept(BigDecimal.TEN);
		assertEquals(BigDecimal.TEN, sum.getMin());
		assertEquals(BigDecimal.TEN, sum.getMax());

		sum.accept(BigDecimal.ONE);
		assertEquals(BigDecimal.ONE, sum.getMin());
		assertEquals(BigDecimal.TEN, sum.getMax());

		sum.accept(nintyNine);
		assertEquals(BigDecimal.ONE, sum.getMin());
		assertEquals(nintyNine, sum.getMax());
	}

	@Test
	public void combineTest() {
		BigDecimalSummaryStatistics sum = new BigDecimalSummaryStatistics();
		sum.accept(BigDecimal.TEN);

		BigDecimalSummaryStatistics one = new BigDecimalSummaryStatistics();
		one.accept(BigDecimal.ONE);
		sum.combine(one);
		assertEquals(BigDecimal.ONE, sum.getMin());
		assertEquals(BigDecimal.TEN, sum.getMax());

		BigDecimalSummaryStatistics nineNine = new BigDecimalSummaryStatistics();
		nineNine.accept(nintyNine);
		sum.combine(nineNine);
		assertEquals(BigDecimal.ONE, sum.getMin());
		assertEquals(nintyNine, sum.getMax());
	}
}
