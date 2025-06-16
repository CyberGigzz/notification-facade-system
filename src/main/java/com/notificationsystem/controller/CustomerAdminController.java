package com.notificationsystem.controller;

import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.dto.NotificationLogDTO;
import com.notificationsystem.service.CustomerService;
import com.notificationsystem.service.NotificationTrackingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller 
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerAdminController {
    
    private final CustomerService customerService;
    private final NotificationTrackingService notificationTrackingService;

    @GetMapping("")
    public String showCustomerList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String notificationType,
            @RequestParam(required = false) String optedInStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Boolean optedIn = (optedInStatus == null || optedInStatus.isEmpty()) ? null : Boolean.parseBoolean(optedInStatus);

        Page<CustomerDTO> customerPage = customerService.searchAndFilterCustomers(keyword, notificationType, optedIn, pageable);

        model.addAttribute("customerPage", customerPage);
        
        model.addAttribute("keyword", keyword);
        model.addAttribute("notificationType", notificationType);
        model.addAttribute("optedInStatus", optedInStatus);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        
        model.addAttribute("allNotificationTypes", NotificationType.values());

        return "customers/list";
    }

    @GetMapping("/new")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());
        
        return "customers/form";
    }

    @PostMapping("")
    public String saveCustomer(@Valid @ModelAttribute("customer") CustomerDTO customerDTO,
                            BindingResult bindingResult, 
                            RedirectAttributes redirectAttributes
                            ) {

        if (bindingResult.hasErrors()) {
            return "customers/form";
        }

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
    public String updateCustomer(@PathVariable Long id,
                                @Valid @ModelAttribute("customer") CustomerDTO customerDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {
            return "customers/form";
        }

        customerDTO.setId(id);
        customerService.saveCustomer(customerDTO);
        redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
        redirectAttributes.addFlashAttribute("messageType", "success");
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
    public String showCustomerDetails(@PathVariable Long id,
            @RequestParam(defaultValue = "1") int page, 
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        CustomerDTO customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NotificationLogDTO> logPage = notificationTrackingService.findLogsByCustomerId(id, pageable);

        model.addAttribute("customer", customer);
        model.addAttribute("logPage", logPage);

        return "customers/details";
    }

}
