package gov.usgs.aqcu.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;

public class StatDerivedIdentifierPresentValidatorTest {

	@Mock
	protected ConstraintValidatorContext context;

	protected StatDerivedIdentifierPresentValidator validator;
	protected DvHydrographRequestParameters params;

	@Before
	public void setup() {
		validator = new StatDerivedIdentifierPresentValidator();
		params = new DvHydrographRequestParameters();
		params.setLastMonths(1);
	}

	@Test
	public void noValuesTest() {
		assertFalse(validator.isValid(params, context));
	}

	@Test
	public void onlyFirstTest() {
		params.setFirstStatDerivedIdentifier("a");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void onlySecondTest() {
		params.setSecondStatDerivedIdentifier("b");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void onlyThirdTest() {
		params.setThirdStatDerivedIdentifier("c");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void onlyFourthTest() {
		params.setFourthStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboOneTest() {
		params.setFirstStatDerivedIdentifier("a");
		params.setSecondStatDerivedIdentifier("b");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboTwoTest() {
		params.setFirstStatDerivedIdentifier("a");
		params.setThirdStatDerivedIdentifier("c");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboThreeTest() {
		params.setFirstStatDerivedIdentifier("a");
		params.setFourthStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboFourTest() {
		params.setFirstStatDerivedIdentifier("a");
		params.setSecondStatDerivedIdentifier("b");
		params.setThirdStatDerivedIdentifier("c");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboFiveTest() {
		params.setFirstStatDerivedIdentifier("a");
		params.setSecondStatDerivedIdentifier("b");
		params.setFourthStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboSixTest() {
		params.setSecondStatDerivedIdentifier("a");
		params.setThirdStatDerivedIdentifier("c");
		params.setFourthStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboSevenTest() {
		params.setSecondStatDerivedIdentifier("b");
		params.setThirdStatDerivedIdentifier("c");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboEightTest() {
		params.setSecondStatDerivedIdentifier("b");
		params.setThirdStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboNineTest() {
		params.setSecondStatDerivedIdentifier("b");
		params.setThirdStatDerivedIdentifier("c");
		params.setThirdStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void comboTenTest() {
		params.setThirdStatDerivedIdentifier("c");
		params.setThirdStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}

	@Test
	public void allTest() {
		params.setFirstStatDerivedIdentifier("a");
		params.setThirdStatDerivedIdentifier("b");
		params.setThirdStatDerivedIdentifier("c");
		params.setFourthStatDerivedIdentifier("d");
		assertTrue(validator.isValid(params, context));
	}
}
