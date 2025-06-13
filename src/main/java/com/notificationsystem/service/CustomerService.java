package com.notificationsystem.service;

import com.notificationsystem.dto.AddressDTO;
import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.dto.PreferenceDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> findAllCustomers();

    Optional<CustomerDTO> findCustomerById(Long id);

    void deleteCustomer(Long id);

    void addAddressToCustomer(Long customerId, AddressDTO addressDTO);

    void deleteAddress(Long addressId);

    Optional<AddressDTO> findAddressById(Long addressId);

    void updateAddress(Long addressId, AddressDTO addressDTO);

    void addPreferenceToCustomer(Long customerId, PreferenceDTO preferenceDTO);

    void deletePreference(Long preferenceId);

    Optional<PreferenceDTO> findPreferenceById(Long preferenceId);

    void updatePreference(Long preferenceId, PreferenceDTO preferenceDTO);
}