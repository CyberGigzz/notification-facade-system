package com.notificationsystem.controller;

import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller 
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerAdminController {
    
    private final CustomerService customerService;

    @GetMapping("")
    public String showCustomerList(Model model) {
        List<CustomerDTO> customers = customerService.findAllCustomers();

        model.addAttribute("customers", customers);

        return "customers/list";
    }

    @GetMapping("/new")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());
        
        return "customers/form";
    }

    @PostMapping
    public String saveCustomer(@ModelAttribute("customer") CustomerDTO customerDTO,
                            RedirectAttributes redirectAttributes) {
        customerService.saveCustomer(customerDTO);
        redirectAttributes.addFlashAttribute("message", "Customer saved successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
        
        return "redirect:/admin/customers";
    }

    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {        
        customerService.deleteCustomer(id);

        redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        
        return "redirect:/admin/customers";
    }

    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("customer") CustomerDTO customerDTO) {
        customerDTO.setId(id);        
        customerService.saveCustomer(customerDTO);
        
        return "redirect:/admin/customers";
    }

    @GetMapping("/{id}/edit")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        CustomerDTO customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);

        return "customers/form";
    }

    @GetMapping("/{id}")
    public String showCustomerDetails(@PathVariable Long id, Model model) {
        CustomerDTO customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);

        return "customers/details";
    }

}
