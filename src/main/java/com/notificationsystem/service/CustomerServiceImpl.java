package com.notificationsystem.service;

import com.notificationsystem.domain.Address;
import com.notificationsystem.domain.Customer;
import com.notificationsystem.domain.Preference;
import com.notificationsystem.dto.AddressDTO;
import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.dto.PreferenceDTO;
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
@Transactional
public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
    if (customerDTO.getId() != null) {
        Customer existingCustomer = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Customer not found with id: " + customerDTO.getId()));

        existingCustomer.setFirstName(customerDTO.getFirstName());
        existingCustomer.setLastName(customerDTO.getLastName());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(updatedCustomer);

    } else {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(customerDTO.getFirstName());
        newCustomer.setLastName(customerDTO.getLastName());
        Customer savedCustomer = customerRepository.save(newCustomer);
        return convertToDTO(savedCustomer);
    }
}

    @Override
    @Transactional(readOnly = true) 
    public List<CustomerDTO> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
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

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        if (customer.getAddresses() != null) {
            dto.setAddresses(customer.getAddresses().stream()
                    .map(this::convertAddressToDTO)
                    .collect(Collectors.toList()));
        }

        if (customer.getPreferences() != null) {
            dto.setPreferences(customer.getPreferences().stream()
                    .map(this::convertPreferenceToDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private AddressDTO convertAddressToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setAddressType(address.getAddressType());
        dto.setValue(address.getValue());
        return dto;
    }

    // New helper method for Preference
    private PreferenceDTO convertPreferenceToDTO(Preference preference) {
        PreferenceDTO dto = new PreferenceDTO();
        dto.setId(preference.getId());
        dto.setNotificationType(preference.getNotificationType());
        dto.setOptedIn(preference.isOptedIn());
        return dto;
    }
}