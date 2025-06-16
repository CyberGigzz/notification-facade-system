package com.notificationsystem.service;

import java.util.Map;

public interface ReportingService {

    /**
     * @return The total number of customers in the system.
     */
    long getTotalCustomerCount();

    /**
     * @return A map where the key is the opt-in status (true/false) and the value is the count of customers.
     */
    Map<Boolean, Long> getCustomerCountByEmailOptInStatus();

    /**
     * @return A map of customer counts by opt-in status for MARKETING_SMS.
     */
    Map<Boolean, Long> getCustomerCountBySmsOptInStatus();

    /**
     * @return A map of notification counts by their final status (e.g., DELIVERED, FAILED).
     */
    Map<String, Long> getNotificationCountByStatus();

}