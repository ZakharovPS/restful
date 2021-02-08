package com.zakharov.restful.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator  implements ConstraintValidator<BirthDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        return value.isBefore(LocalDate.now()) && value.isAfter(LocalDate.of(1900, 1, 1));

    }
}
