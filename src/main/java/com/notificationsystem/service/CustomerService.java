package com.notificationsystem.service;

import com.notificationsystem.dto.AddressDTO;
import com.notificationsystem.dto.CustomerDTO;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> findAllCustomers();

    Optional<CustomerDTO> findCustomerById(Long id);

    void deleteCustomer(Long id);

    void addAddressToCustomer(Long customerId, AddressDTO addressDTO);
}