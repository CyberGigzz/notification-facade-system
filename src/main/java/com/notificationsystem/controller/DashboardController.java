package com.notificationsystem.controller;

import com.notificationsystem.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin/dashboard") // All methods in this controller will be under /admin/dashboard
@RequiredArgsConstructor
public class DashboardController {

    private final ReportingService reportingService;

    @GetMapping("")
    public String showDashboard(Model model) {
        // --- 1. Gather all statistics ---
        long totalCustomers = reportingService.getTotalCustomerCount();
        Map<Boolean, Long> emailOptInStats = reportingService.getCustomerCountByEmailOptInStatus();
        Map<Boolean, Long> smsOptInStats = reportingService.getCustomerCountBySmsOptInStatus(); // New
        Map<String, Long> notificationStatusStats = reportingService.getNotificationCountByStatus(); // New

        // --- 2. Add statistics to the model ---
        model.addAttribute("totalCustomers", totalCustomers);
        
        // Email Stats
        model.addAttribute("emailOptedInCount", emailOptInStats.getOrDefault(true, 0L));
        model.addAttribute("emailOptedOutCount", emailOptInStats.getOrDefault(false, 0L));
        
        // SMS Stats (New)
        model.addAttribute("smsOptedInCount", smsOptInStats.getOrDefault(true, 0L));
        model.addAttribute("smsOptedOutCount", smsOptInStats.getOrDefault(false, 0L));
        
        // Notification Delivery Stats (New)
        model.addAttribute("deliveredCount", notificationStatusStats.getOrDefault("DELIVERED", 0L));
        model.addAttribute("failedCount", notificationStatusStats.getOrDefault("FAILED", 0L) + notificationStatusStats.getOrDefault("BOUNCED", 0L)); // Combine FAILED and BOUNCED

        // --- 3. Return view name ---
        return "dashboard";
    }
}