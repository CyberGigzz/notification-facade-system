package com.notificationsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.notificationsystem.dto.NotificationLogDTO;

public interface NotificationTrackingService {

    /**
     * Logs the status of a sent notification.
     * @param logDTO The DTO containing the notification status details.
     */
    void logNotification(NotificationLogDTO logDTO);

    Page<NotificationLogDTO> findLogsByCustomerId(Long customerId, Pageable pageable);

}