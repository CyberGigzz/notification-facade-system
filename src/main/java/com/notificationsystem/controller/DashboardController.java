package com.notificationsystem.controller;

import com.notificationsystem.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin/dashboard") 
@RequiredArgsConstructor
public class DashboardController {

    private final ReportingService reportingService;

    @GetMapping("")
    public String showDashboard(Model model) {
        long totalCustomers = reportingService.getTotalCustomerCount();
        Map<Boolean, Long> emailOptInStats = reportingService.getCustomerCountByEmailOptInStatus();
        Map<Boolean, Long> smsOptInStats = reportingService.getCustomerCountBySmsOptInStatus(); 
        Map<String, Long> notificationStatusStats = reportingService.getNotificationCountByStatus(); 

        model.addAttribute("totalCustomers", totalCustomers);
        
        model.addAttribute("emailOptedInCount", emailOptInStats.getOrDefault(true, 0L));
        model.addAttribute("emailOptedOutCount", emailOptInStats.getOrDefault(false, 0L));
        
        model.addAttribute("smsOptedInCount", smsOptInStats.getOrDefault(true, 0L));
        model.addAttribute("smsOptedOutCount", smsOptInStats.getOrDefault(false, 0L));
        
        model.addAttribute("deliveredCount", notificationStatusStats.getOrDefault("DELIVERED", 0L));
        model.addAttribute("failedCount", notificationStatusStats.getOrDefault("FAILED", 0L) + notificationStatusStats.getOrDefault("BOUNCED", 0L)); 


        return "dashboard";
    }
}