package com.notificationsystem.dto;

import com.notificationsystem.domain.enums.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class AddressDTO {

    private Long id;
    
    @NotNull(message = "Address type must be selected.")
    private AddressType addressType;

    @NotBlank(message = "Value cannot be blank.")
    private String value;
}