package com.notificationsystem.controller;

import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers") // Base path for all endpoints in this controller
@RequiredArgsConstructor // For clean constructor injection of the service
public class CustomerController {

    private final CustomerService customerService;

    // Endpoint to GET all customers
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(customers); // Returns 200 OK with the list of customers
    }

    // Endpoint to CREATE a new customer
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);
        // Return 201 Created status with the created customer in the body
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    // Endpoint to GET a single customer by their ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id)
                .map(ResponseEntity::ok) // If customer is found, wrap it in a 200 OK response
                .orElse(ResponseEntity.notFound().build()); // If not found, return a 404 Not Found
    }

    // Endpoint to UPDATE an existing customer
    // Note: A full implementation would need more logic in the service layer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        // First, check if the customer exists
        return customerService.findCustomerById(id)
                .map(existingCustomer -> {
                    // Set the ID from the path to ensure we update the correct resource
                    customerDTO.setId(id);
                    CustomerDTO updatedCustomer = customerService.saveCustomer(customerDTO);
                    return ResponseEntity.ok(updatedCustomer);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to DELETE a customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        // We might want to check if the customer exists first, but for now this is direct.
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content, the standard for a successful delete
    }
}