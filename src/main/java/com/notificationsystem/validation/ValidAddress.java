package com.notificationsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AddressValidator.class) // Specifies the class that contains the validation logic
@Target({ ElementType.TYPE }) // This annotation can only be applied to a class/type
@Retention(RetentionPolicy.RUNTIME) // The annotation needs to be available at runtime
public @interface ValidAddress {

    // Default error message
    String message() default "Invalid address value for the selected type.";

    // Boilerplate for validation annotations
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}