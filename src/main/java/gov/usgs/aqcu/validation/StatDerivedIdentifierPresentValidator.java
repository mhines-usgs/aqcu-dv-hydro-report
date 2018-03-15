package gov.usgs.aqcu.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import gov.usgs.aqcu.parameter.TssRequestParameters;

public class StatDerivedIdentifierPresentValidator implements ConstraintValidator<StatDerivedIdentifierPresent, TssRequestParameters> {

	@Override
	public void initialize(StatDerivedIdentifierPresent constraintAnnotation) {
		// Nothing to see here.
	}

	@Override
	public boolean isValid(TssRequestParameters value, ConstraintValidatorContext context) {
		return !(value.getFirstStatDerivedIdentifier() == null 
				&& value.getSecondStatDerivedIdentifier() == null 
				&& value.getThirdStatDerivedIdentifier() == null 
				&& value.getFourthStatDerivedIdentifier() == null);
	}

}
