package com.notificationsystem.validation;

import com.notificationsystem.dto.AddressDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AddressValidator implements ConstraintValidator<ValidAddress, AddressDTO> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern SMS_PATTERN = Pattern.compile(
            "^\\+?[0-9\\s\\-]{7,20}$"
    );

    @Override
    public boolean isValid(AddressDTO address, ConstraintValidatorContext context) {
        if (address.getAddressType() == null || address.getValue() == null) {
            return true;
        }

        String value = address.getValue();

        switch (address.getAddressType()) {
            case EMAIL:
                return EMAIL_PATTERN.matcher(value).matches();
            case SMS:
                return SMS_PATTERN.matcher(value).matches();
            case POSTAL:
                return !value.trim().isEmpty();
            default:
                return false;
        }
    }
}