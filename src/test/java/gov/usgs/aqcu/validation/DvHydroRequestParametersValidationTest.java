package gov.usgs.aqcu.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.usgs.aqcu.parameter.RequestParameters;
import gov.usgs.aqcu.parameter.DvHydroRequestParameters;

public class DvHydroRequestParametersValidationTest {

	protected static ValidatorFactory validatorFactory;
	protected static Validator validator;

	protected DvHydroRequestParameters params;

	@BeforeClass
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Before
	public void setup() {
		params = new DvHydroRequestParameters();
	}

	@AfterClass
	public static void destroyValidator() {
		validatorFactory.close();
	}

	@Test
	public void emptyRequestParameters() {
		Set<ConstraintViolation<RequestParameters>> validationErrors = validator.validate(params);
		assertEquals(RequestParametersValidationTest.BASE_EMPTY_PARAMETER_ERROR_COUNT + 1, validationErrors.size());

		assertValidationResults(validationErrors,
				":Need at least one Stat-Derived Time Series Identifier selected."
		);
	}

	@Test
	public void goodParameters() {
		params.setFirstStatDerivedIdentifier("a");
		Set<ConstraintViolation<RequestParameters>> validationErrors = validator.validate(params);
		assertEquals(RequestParametersValidationTest.BASE_EMPTY_PARAMETER_ERROR_COUNT, validationErrors.size());
	}

	public void assertValidationResults(Set<ConstraintViolation<RequestParameters>> actual, String expected) {
		List<String> actualStrings = actual
				.stream()
				.map(x -> String.join(":", x.getPropertyPath().toString(), x.getMessage()))
				.collect(Collectors.toList());
		assertThat(expected, isIn(actualStrings));
	}

}
