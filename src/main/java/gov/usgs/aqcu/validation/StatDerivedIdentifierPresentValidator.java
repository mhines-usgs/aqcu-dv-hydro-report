package gov.usgs.aqcu.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import gov.usgs.aqcu.parameter.DvHydroRequestParameters;

public class StatDerivedIdentifierPresentValidator implements ConstraintValidator<StatDerivedIdentifierPresent, DvHydroRequestParameters> {

	@Override
	public void initialize(StatDerivedIdentifierPresent constraintAnnotation) {
		// Nothing to see here.
	}

	@Override
	public boolean isValid(DvHydroRequestParameters value, ConstraintValidatorContext context) {
		return !(value.getFirstStatDerivedIdentifier() == null 
				&& value.getSecondStatDerivedIdentifier() == null 
				&& value.getThirdStatDerivedIdentifier() == null 
				&& value.getFourthStatDerivedIdentifier() == null);
	}

}
