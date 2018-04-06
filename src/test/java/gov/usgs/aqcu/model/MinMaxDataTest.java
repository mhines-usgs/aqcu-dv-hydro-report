package gov.usgs.aqcu.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MinMaxDataTest {

	private Map<BigDecimal, List<MinMaxPoint>> minMaxPoints;
	private Instant time1 = Instant.parse("2000-01-01T16:00:00.00Z");
	private Instant time2 = Instant.parse("2010-01-01T18:00:00.00Z");
	private Instant time3 = Instant.parse("2010-01-01T18:45:00.00Z");
	private Instant time4 = Instant.parse("2018-01-01T18:45:00.00Z");


	@Before
	public void setup() {
		minMaxPoints = new HashMap<>();
	}

	@Test
	public void noDataTest() {
		MinMaxData mmd = new MinMaxData(null, null, null);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());

		mmd = new MinMaxData(BigDecimal.ONE, null, null);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());

		mmd = new MinMaxData(BigDecimal.ONE, BigDecimal.ONE, null);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());

		mmd = new MinMaxData(null, BigDecimal.ONE, null);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());

		minMaxPoints.put(BigDecimal.ONE, null);
		mmd = new MinMaxData(null, null, minMaxPoints);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());

		mmd = new MinMaxData(BigDecimal.ONE, null, minMaxPoints);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());

		mmd = new MinMaxData(null, BigDecimal.ONE, minMaxPoints);
		assertTrue(mmd.getMax().isEmpty());
		assertTrue(mmd.getMin().isEmpty());
	}

	@Test
	public void minMaxSameTest() {
		minMaxPoints.put(BigDecimal.ONE, buildPointList(BigDecimal.ONE));
		MinMaxData mmd = new MinMaxData(BigDecimal.ONE, BigDecimal.ONE, minMaxPoints);
		assertEquals(4, mmd.getMax().size());
		assertEquals(4, mmd.getMin().size());
		assertEquals(mmd.getMax(), mmd.getMin());
		assertEquals(BigDecimal.ONE, mmd.getMax().get(0).getValue());
		assertEquals(BigDecimal.ONE, mmd.getMax().get(1).getValue());
		assertEquals(BigDecimal.ONE, mmd.getMax().get(2).getValue());
		assertEquals(BigDecimal.ONE, mmd.getMax().get(3).getValue());
		assertEquals(time1, mmd.getMax().get(0).getTime());
		assertEquals(time2, mmd.getMax().get(1).getTime());
		assertEquals(time3, mmd.getMax().get(2).getTime());
		assertEquals(time4, mmd.getMax().get(3).getTime());
	}

	@Test
	public void minMaxTest() {
		minMaxPoints.put(BigDecimal.ONE, buildPointList(BigDecimal.ONE));
		minMaxPoints.put(BigDecimal.TEN, buildPointList(BigDecimal.TEN));
		MinMaxData mmd = new MinMaxData(BigDecimal.ONE, BigDecimal.TEN, minMaxPoints);
		assertEquals(4, mmd.getMin().size());
		assertEquals(BigDecimal.ONE, mmd.getMin().get(0).getValue());
		assertEquals(BigDecimal.ONE, mmd.getMin().get(1).getValue());
		assertEquals(BigDecimal.ONE, mmd.getMin().get(2).getValue());
		assertEquals(BigDecimal.ONE, mmd.getMin().get(3).getValue());
		assertEquals(time1, mmd.getMin().get(0).getTime());
		assertEquals(time2, mmd.getMin().get(1).getTime());
		assertEquals(time3, mmd.getMin().get(2).getTime());
		assertEquals(time4, mmd.getMin().get(3).getTime());

		assertEquals(4, mmd.getMax().size());
		assertEquals(BigDecimal.TEN, mmd.getMax().get(0).getValue());
		assertEquals(BigDecimal.TEN, mmd.getMax().get(1).getValue());
		assertEquals(BigDecimal.TEN, mmd.getMax().get(2).getValue());
		assertEquals(BigDecimal.TEN, mmd.getMax().get(3).getValue());
		assertEquals(time1, mmd.getMax().get(0).getTime());
		assertEquals(time2, mmd.getMax().get(1).getTime());
		assertEquals(time3, mmd.getMax().get(2).getTime());
		assertEquals(time4, mmd.getMax().get(3).getTime());
	}

	protected List<MinMaxPoint> buildPointList(BigDecimal value) {
		List<MinMaxPoint> points = new ArrayList<>();
		//Note that we are not in time order here.
		points.add(new MinMaxPoint(time3, value));
		points.add(new MinMaxPoint(time2, value));
		points.add(new MinMaxPoint(time4, value));
		points.add(new MinMaxPoint(time1, value));
		return points;
	}
}
