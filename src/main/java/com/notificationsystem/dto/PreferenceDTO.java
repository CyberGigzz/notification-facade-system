package com.notificationsystem.dto;

import com.notificationsystem.domain.enums.NotificationType;
import lombok.Data;

@Data
public class PreferenceDTO {
    private Long id;
    private NotificationType notificationType;
    private boolean isOptedIn;
}