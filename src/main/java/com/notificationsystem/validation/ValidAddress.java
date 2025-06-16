package com.notificationsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AddressValidator.class) 
@Target({ ElementType.TYPE }) 
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAddress {

    String message() default "Invalid address value for the selected type.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}