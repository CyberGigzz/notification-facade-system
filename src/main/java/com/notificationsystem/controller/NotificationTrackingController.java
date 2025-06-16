package com.notificationsystem.controller;

import com.notificationsystem.dto.NotificationLogDTO;
import com.notificationsystem.service.NotificationTrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationTrackingController {

    private final NotificationTrackingService notificationTrackingService;

    /**
     * Endpoint for external systems to report the status of a sent notification.
     * @param logDTO The details of the notification log.
     * @return A response indicating success.
     */
    @PostMapping("/track")
    public ResponseEntity<Void> trackNotificationStatus(@Valid @RequestBody NotificationLogDTO logDTO) {
        notificationTrackingService.logNotification(logDTO);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}