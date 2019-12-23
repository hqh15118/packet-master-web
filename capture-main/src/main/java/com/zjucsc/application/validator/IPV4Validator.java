package com.zjucsc.application.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public final class IPV4Validator implements ConstraintValidator<IPV4, String> {
    private static final Pattern IP_PATTERN = Pattern.compile("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null){
            return false;
        }
        return IP_PATTERN.matcher(s).matches();
    }
}
