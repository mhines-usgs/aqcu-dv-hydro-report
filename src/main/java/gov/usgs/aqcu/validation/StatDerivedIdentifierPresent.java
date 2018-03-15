package gov.usgs.aqcu.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { StatDerivedIdentifierPresentValidator.class })
public @interface StatDerivedIdentifierPresent {

	String message() default "Need at least one Stat-Derived Time Series Identifier selected.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
