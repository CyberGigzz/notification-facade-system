package com.notificationsystem.controller;

import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.dto.PreferenceDTO;
import com.notificationsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller 
@RequestMapping("/admin/customers/{customerId}/preferences")
@RequiredArgsConstructor
public class PreferenceAdminController {
    
    private final CustomerService customerService;


    @GetMapping("/new")
    public String showAddPreferenceForm(@PathVariable Long customerId, Model model) {
        model.addAttribute("customerId", customerId);
        model.addAttribute("preference", new PreferenceDTO());
        model.addAttribute("notificationTypes", NotificationType.values());
        
        return "preferences/add-form";
    }

    @PostMapping("")
    public String savePreference(@PathVariable Long customerId, @ModelAttribute("preference") PreferenceDTO preferenceDTO) {
                                    
        customerService.addPreferenceToCustomer(customerId, preferenceDTO);
        
        return "redirect:/admin/customers/" + customerId;
    }

    @PostMapping("/{preferenceId}/delete")
    public String deletePreference(@PathVariable Long customerId, @PathVariable Long preferenceId) {
        customerService.deletePreference(preferenceId);
        return "redirect:/admin/customers/" + customerId;
    }

    @GetMapping("/{preferenceId}/edit")
    public String showEditPreferenceForm(@PathVariable Long customerId, @PathVariable Long preferenceId, Model model) {
        
        PreferenceDTO preference = customerService.findPreferenceById(preferenceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid preference Id:" + preferenceId));

        model.addAttribute("preference", preference);
        model.addAttribute("customerId", customerId);
        model.addAttribute("notificationTypes", NotificationType.values());
        
        return "preferences/edit-form";
    }

    @PostMapping("/{preferenceId}")
    public String updatePreference(@PathVariable Long customerId, @PathVariable Long preferenceId, @ModelAttribute("preference") PreferenceDTO preferenceDTO) {
                                    
        customerService.updatePreference(preferenceId, preferenceDTO);
        
        return "redirect:/admin/customers/" + customerId;
    }
}
