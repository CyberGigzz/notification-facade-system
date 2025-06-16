package com.notificationsystem.service;

import com.notificationsystem.domain.Address;
import com.notificationsystem.domain.Customer;
import com.notificationsystem.domain.Preference;
import com.notificationsystem.dto.AddressDTO;
import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.dto.PreferenceDTO;
import com.notificationsystem.repository.AddressRepository;
import com.notificationsystem.repository.CustomerRepository;
import com.notificationsystem.repository.PreferenceRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service 
@RequiredArgsConstructor 
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final PreferenceRepository preferenceRepository;

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
    public Page<CustomerDTO> findAllCustomers(Pageable pageable) {

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        

        return customerPage.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO); 
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

    private PreferenceDTO convertPreferenceToDTO(Preference preference) {
        PreferenceDTO dto = new PreferenceDTO();
        dto.setId(preference.getId());
        dto.setNotificationType(preference.getNotificationType());
        dto.setOptedIn(preference.isOptedIn());
        return dto;
    }

    @Override
    @Transactional
    public void addAddressToCustomer(Long customerId, AddressDTO addressDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Customer not found with id: " + customerId));

        Address newAddress = new Address();
        newAddress.setAddressType(addressDTO.getAddressType());
        newAddress.setValue(addressDTO.getValue());
        newAddress.setCustomer(customer);
        customer.getAddresses().add(newAddress);

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .map(this::convertAddressToDTO); 
    }

    @Override
    @Transactional
    public void updateAddress(Long addressId, AddressDTO addressDTO) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Address not found with id: " + addressId));
        existingAddress.setAddressType(addressDTO.getAddressType());
        existingAddress.setValue(addressDTO.getValue());

        addressRepository.save(existingAddress);
    }

    @Override
    @Transactional
    public void addPreferenceToCustomer(Long customerId, PreferenceDTO preferenceDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Customer not found with id: " + customerId));

        Preference newPreference = new Preference();
        newPreference.setNotificationType(preferenceDTO.getNotificationType());
        newPreference.setOptedIn(preferenceDTO.isOptedIn());
        
        newPreference.setCustomer(customer);
        
        customer.getPreferences().add(newPreference);

        customerRepository.save(customer);
    }


    @Override
    @Transactional
    public void deletePreference(Long preferenceId) {
        preferenceRepository.deleteById(preferenceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PreferenceDTO> findPreferenceById(Long preferenceId) {
        return preferenceRepository.findById(preferenceId)
                .map(this::convertPreferenceToDTO);
    }

    @Override
    @Transactional
    public void updatePreference(Long preferenceId, PreferenceDTO preferenceDTO) {
        Preference existingPreference = preferenceRepository.findById(preferenceId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Preference not found with id: " + preferenceId));

        existingPreference.setOptedIn(preferenceDTO.isOptedIn());

        preferenceRepository.save(existingPreference);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> searchAndFilterCustomers(String keyword, String notificationType, Boolean optedInStatus, Pageable pageable) {
        return customerRepository.findByCriteria(keyword, notificationType, optedInStatus, pageable);
    }
}