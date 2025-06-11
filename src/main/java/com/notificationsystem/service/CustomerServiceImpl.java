package com.notificationsystem.service;

import com.notificationsystem.domain.Customer;
import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service 
@RequiredArgsConstructor 
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional // Ensures the entire method runs in a single database transaction
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        // In a real scenario, you'd also map addresses and preferences.

        Customer savedCustomer = customerRepository.save(customer);

        // We should return a DTO, not the entity.
        customerDTO.setId(savedCustomer.getId());
        return customerDTO;
    }

    @Override
    @Transactional(readOnly = true) // A read-only transaction is more efficient for find operations
    public List<CustomerDTO> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO) // Convert each customer entity to a DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO); // If customer exists, convert it to DTO
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // Helper method to convert an Entity to a DTO
    // In a real project, you might use a library like MapStruct for this.
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        // Note: For simplicity, we are not mapping the lists of addresses/preferences yet.
        // We will add this complexity later when we build the full update/create logic.
        return dto;
    }
}