package com.notificationsystem.validation;

import com.notificationsystem.dto.AddressDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AddressValidator implements ConstraintValidator<ValidAddress, AddressDTO> {

    // Regex for a simple email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    // Regex for a simple phone number validation (allows +, digits, spaces, hyphens)
    private static final Pattern SMS_PATTERN = Pattern.compile(
            "^\\+?[0-9\\s\\-]{7,20}$"
    );

    @Override
    public boolean isValid(AddressDTO address, ConstraintValidatorContext context) {
        // If either the type or value is null, our @NotNull/@NotBlank annotations will catch it first.
        // This validator should only run if both are present.
        if (address.getAddressType() == null || address.getValue() == null) {
            return true; // Let other validators handle this.
        }

        String value = address.getValue();

        switch (address.getAddressType()) {
            case EMAIL:
                return EMAIL_PATTERN.matcher(value).matches();
            case SMS:
                return SMS_PATTERN.matcher(value).matches();
            case POSTAL:
                // For postal, we just check that it's not empty after trimming whitespace.
                return !value.trim().isEmpty();
            default:
                // Should not happen if the enum is exhaustive
                return false;
        }
    }
}