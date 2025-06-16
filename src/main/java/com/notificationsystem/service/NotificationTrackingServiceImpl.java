package com.notificationsystem.service;

import com.notificationsystem.domain.Address;
import com.notificationsystem.domain.NotificationLog;
import com.notificationsystem.dto.NotificationLogDTO;
import com.notificationsystem.repository.AddressRepository;
import com.notificationsystem.repository.NotificationLogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationTrackingServiceImpl implements NotificationTrackingService {

    private final NotificationLogRepository notificationLogRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public void logNotification(NotificationLogDTO logDTO) {
        Address address = addressRepository.findById(logDTO.getAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + logDTO.getAddressId()));

        NotificationLog log = new NotificationLog();
        log.setAddress(address); 
        log.setSentAt(logDTO.getSentAt());
        log.setStatus(logDTO.getStatus());
        log.setStatusDetails(logDTO.getStatusDetails());

        notificationLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationLogDTO> findLogsByCustomerId(Long customerId, Pageable pageable) {
        Page<NotificationLog> logPage = notificationLogRepository.findByCustomerId(customerId, pageable);
        return logPage.map(this::convertToDTO);
    }

    private NotificationLogDTO convertToDTO(NotificationLog log) {
        NotificationLogDTO dto = new NotificationLogDTO();
        dto.setId(log.getId());
        dto.setSentAt(log.getSentAt());
        dto.setStatus(log.getStatus());
        dto.setStatusDetails(log.getStatusDetails());
        
        if (log.getAddress() != null) {
            dto.setAddressId(log.getAddress().getId());
            dto.setAddressValue(log.getAddress().getValue());
            if (log.getAddress().getCustomer() != null) {
                dto.setCustomerId(log.getAddress().getCustomer().getId());
            }
        }
        
        return dto;
    }
}