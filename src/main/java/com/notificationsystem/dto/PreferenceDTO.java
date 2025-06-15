package com.notificationsystem.dto;

import com.notificationsystem.domain.enums.NotificationType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreferenceDTO {
    private Long id;

    @NotNull(message = "Notification type must be selected.")
    private NotificationType notificationType;
    
    private boolean isOptedIn;
}