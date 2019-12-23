package com.zjucsc.application.validator.elec;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ElecValidator.class)
public @interface ElecProtocol {
    String message() default "非电力协议(iec104/dnp3/goose/mms)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
