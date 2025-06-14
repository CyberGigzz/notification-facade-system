package com.notificationsystem.repository;

import com.notificationsystem.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}