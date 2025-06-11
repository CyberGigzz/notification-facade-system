package com.notificationsystem.dto;

import com.notificationsystem.domain.enums.AddressType;
import lombok.Data;

@Data // Lombok's @Data is perfect for DTOs
public class AddressDTO {
    private Long id;
    private AddressType addressType;
    private String value;
}