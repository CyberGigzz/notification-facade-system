package com.notificationsystem.controller;

import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.dto.PreferenceDTO;
import com.notificationsystem.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
        
        return "preferences/form";
    }

    @PostMapping("/create")
    public String savePreference(@PathVariable Long customerId,
                                 @Valid @ModelAttribute("preference") PreferenceDTO preferenceDTO,
                                 BindingResult bindingResult,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerId", customerId);
            model.addAttribute("notificationTypes", NotificationType.values());
            return "preferences/form";
        }
        customerService.addPreferenceToCustomer(customerId, preferenceDTO);
        redirectAttributes.addFlashAttribute("message", "Preference added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/admin/customers/" + customerId;
    }

    @PostMapping("/{preferenceId}/delete")
    public String deletePreference(@PathVariable Long customerId, @PathVariable Long preferenceId, RedirectAttributes redirectAttributes) {
        customerService.deletePreference(preferenceId);
        redirectAttributes.addFlashAttribute("message", "Preference deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        return "redirect:/admin/customers/" + customerId;
    }

    @GetMapping("/{preferenceId}/edit")
    public String showEditPreferenceForm(@PathVariable Long customerId, @PathVariable Long preferenceId, Model model) {
        
        PreferenceDTO preference = customerService.findPreferenceById(preferenceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid preference Id:" + preferenceId));

        model.addAttribute("preference", preference);
        model.addAttribute("customerId", customerId);
        model.addAttribute("notificationTypes", NotificationType.values());
        
        return "preferences/form";
    }

    @PostMapping("/{preferenceId}") 
    public String updatePreference(@PathVariable Long customerId, @PathVariable Long preferenceId,
                                   @Valid @ModelAttribute("preference") PreferenceDTO preferenceDTO,
                                   BindingResult bindingResult,
                                   Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerId", customerId);
            model.addAttribute("notificationTypes", NotificationType.values());
            return "preferences/form";
        }
        customerService.updatePreference(preferenceId, preferenceDTO);
        redirectAttributes.addFlashAttribute("message", "Preference updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/admin/customers/" + customerId;
    }
}
