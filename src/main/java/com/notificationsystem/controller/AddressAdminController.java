package com.notificationsystem.controller;

import com.notificationsystem.domain.enums.AddressType;
import com.notificationsystem.dto.AddressDTO;
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
@RequestMapping("/admin/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class AddressAdminController {

    private final CustomerService customerService;

    @GetMapping("/new")
    public String showAddAddressForm(@PathVariable Long customerId, Model model) {
        model.addAttribute("customerId", customerId);        
        model.addAttribute("address", new AddressDTO());
        model.addAttribute("addressTypes", AddressType.values());
        
        return "addresses/add-form";
    }

    @PostMapping("")
    public String saveAddress(@PathVariable Long customerId, @ModelAttribute("address") AddressDTO addressDTO) {
        customerService.addAddressToCustomer(customerId, addressDTO);
        
        return "redirect:/admin/customers/" + customerId;
    }

    @PostMapping("/{addressId}/delete")
    public String deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        customerService.deleteAddress(addressId);
        
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
        
        return "addresses/edit-form";
    }

    @PostMapping("/{addressId}")
    public String updateAddress(@PathVariable Long customerId,
                                @PathVariable Long addressId,
                                @ModelAttribute("address") AddressDTO addressDTO) {    
        customerService.updateAddress(addressId, addressDTO);
        
        return "redirect:/admin/customers/" + customerId;
    }
    
}
