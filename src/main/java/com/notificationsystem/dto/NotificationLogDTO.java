package com.notificationsystem.dto;

import com.notificationsystem.domain.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationLogDTO {

    private Long id;

    @NotNull(message = "Address ID cannot be null.")
    private Long addressId;

    @NotNull(message = "Sent timestamp cannot be null.")
    private LocalDateTime sentAt;

    @NotNull(message = "Status cannot be null.")
    private NotificationStatus status;

    private String statusDetails;

    private String addressValue;
    private Long customerId;
}