package com.notificationsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class CustomerDTO {
    private Long id;

    @NotEmpty(message = "First name cannot be empty.") // The validation rule
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;
    private List<AddressDTO> addresses;
    private List<PreferenceDTO> preferences;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}