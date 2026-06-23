package aitho.ranim.hrms.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProjectDatesValidator.class)
public @interface ValidProjectDates {

    String message() default "The end date must be on or after the start date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
