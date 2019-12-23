package com.zjucsc.application.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPV4Validator.class)
public @interface IPV4 {
    String message() default "IPV4格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
