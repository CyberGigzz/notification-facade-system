package com.notificationsystem.dto;

import com.notificationsystem.domain.enums.AddressType;
import com.notificationsystem.validation.ValidAddress;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ValidAddress(message = "The provided value is not a valid format for the selected address type.") // Apply it here
public class AddressDTO {

    private Long id;
    
    @NotNull(message = "Address type must be selected.")
    private AddressType addressType;

    @NotBlank(message = "Value cannot be blank.")
    private String value;
}