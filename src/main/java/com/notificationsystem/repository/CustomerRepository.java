package com.notificationsystem.repository;

import com.notificationsystem.domain.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository 
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.addresses")
    List<Customer> findAllWithAddresses();
}