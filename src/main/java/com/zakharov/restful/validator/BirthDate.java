package com.zakharov.restful.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
@Documented
public @interface  BirthDate {
    String message() default "Incorrect birth date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
