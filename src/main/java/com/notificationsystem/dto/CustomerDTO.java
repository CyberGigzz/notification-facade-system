package com.notificationsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private List<AddressDTO> addresses;
    private List<PreferenceDTO> preferences;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}