package com.notificationsystem.controller;

import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}