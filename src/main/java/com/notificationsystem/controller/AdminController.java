package com.notificationsystem.controller;

import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller 
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public String showCustomerList(Model model) {
        List<CustomerDTO> customers = customerService.findAllCustomers();

        model.addAttribute("customers", customers);

        return "customers";
    }

    @GetMapping("/customers/new")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());
        
        return "add-customer";
    }

    @PostMapping("/customers")
    public String saveCustomer(@ModelAttribute("customer") CustomerDTO customerDTO) {
        
        customerService.saveCustomer(customerDTO);
        
        return "redirect:/admin/customers";
    }

    @PostMapping("/customers/{id}/delete")
    public String deleteCustomer(@PathVariable Long id) {        
        customerService.deleteCustomer(id);
        
        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/{id}/edit")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        CustomerDTO customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);

        return "edit-customer";
    }

    @PostMapping("/customers/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("customer") CustomerDTO customerDTO) {
        customerDTO.setId(id);        
        customerService.saveCustomer(customerDTO);
        
        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/{id}")
    public String showCustomerDetails(@PathVariable Long id, Model model) {
        // Fetch the customer. The service will now return a DTO
        // fully populated with addresses and preferences.
        CustomerDTO customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        // Add the customer object to the model.
        model.addAttribute("customer", customer);

        // Return the name of our new details template.
        return "customer-details";
    }
}