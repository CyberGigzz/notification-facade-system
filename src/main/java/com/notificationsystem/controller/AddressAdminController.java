package com.notificationsystem.controller;

import com.notificationsystem.domain.enums.AddressType;
import com.notificationsystem.dto.AddressDTO;
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
@RequestMapping("/admin/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class AddressAdminController {

    private final CustomerService customerService;

    @GetMapping("/new")
    public String showAddAddressForm(@PathVariable Long customerId, Model model) {
        model.addAttribute("customerId", customerId);        
        model.addAttribute("address", new AddressDTO());
        model.addAttribute("addressTypes", AddressType.values());
        
        return "addresses/form";
    }



    @PostMapping("/{addressId}/delete")
    public String deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId, RedirectAttributes redirectAttributes) {
        customerService.deleteAddress(addressId);
        redirectAttributes.addFlashAttribute("message", "Address deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        return "redirect:/admin/customers/" + customerId;
    }

    @GetMapping("/{addressId}/edit")
    public String showEditAddressForm(@PathVariable Long customerId, 
                                    @PathVariable Long addressId, Model model) {
        
        AddressDTO address = customerService.findAddressById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid address Id:" + addressId));

        model.addAttribute("address", address);
        model.addAttribute("customerId", customerId);
        model.addAttribute("addressTypes", AddressType.values());
        
        return "addresses/form";
    }

    @PostMapping("/create") // Explicit URL for creating
    public String saveAddress(@PathVariable Long customerId,
                              @Valid @ModelAttribute("address") AddressDTO addressDTO,
                              BindingResult bindingResult,
                              Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerId", customerId);
            model.addAttribute("addressTypes", AddressType.values());
            return "addresses/form";
        }
        customerService.addAddressToCustomer(customerId, addressDTO);
        redirectAttributes.addFlashAttribute("message", "Address added successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/admin/customers/" + customerId;
    }

    @PostMapping("/{addressId}") // URL for updating
    public String updateAddress(@PathVariable Long customerId, @PathVariable Long addressId,
                                @Valid @ModelAttribute("address") AddressDTO addressDTO,
                                BindingResult bindingResult,
                                Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerId", customerId);
            model.addAttribute("addressTypes", AddressType.values());
            return "addresses/form";
        }
        customerService.updateAddress(addressId, addressDTO);
        redirectAttributes.addFlashAttribute("message", "Address updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/admin/customers/" + customerId;
    }
    
}
