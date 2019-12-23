package com.zjucsc.application.validator.elec;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

public final class ElecValidator  implements ConstraintValidator<ElecProtocol, String> {

    private static final HashSet<String> ELEC_PROTOCOL = new HashSet<String>(3){
        {
            add("iec104");
            add("dnp3");
            add("goose");
            add("mms");
        }
    };

    @Override
    public boolean isValid(String protocol, ConstraintValidatorContext constraintValidatorContext) {
        return ELEC_PROTOCOL.contains(protocol);
    }
}
