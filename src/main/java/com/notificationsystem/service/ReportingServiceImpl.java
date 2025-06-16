package com.notificationsystem.service;

import com.notificationsystem.domain.enums.NotificationStatus;
import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.repository.CustomerRepository;
import com.notificationsystem.repository.NotificationLogRepository;
import com.notificationsystem.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

    private final CustomerRepository customerRepository;
    private final PreferenceRepository preferenceRepository;
    private final NotificationLogRepository notificationLogRepository; 

    @Override
    @Transactional(readOnly = true)
    public long getTotalCustomerCount() {
        return customerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Boolean, Long> getCustomerCountByEmailOptInStatus() {
        List<Object[]> results = preferenceRepository.countByOptInStatusForType(NotificationType.MARKETING_EMAIL);

        return results.stream()
                .collect(Collectors.toMap(
                        result -> (Boolean) result[0], 
                        result -> (Long) result[1]     
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Boolean, Long> getCustomerCountBySmsOptInStatus() {
        List<Object[]> results = preferenceRepository.countByOptInStatusForType(NotificationType.MARKETING_SMS);
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (Boolean) result[0],
                        result -> (Long) result[1]
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getNotificationCountByStatus() {
        List<Object[]> results = notificationLogRepository.countByStatus();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> ((NotificationStatus) result[0]).name(),
                        result -> (Long) result[1]
                ));
    }
}