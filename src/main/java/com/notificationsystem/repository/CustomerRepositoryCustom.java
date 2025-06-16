package com.notificationsystem.repository;

import com.notificationsystem.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {

    Page<CustomerDTO> findByCriteria(String keyword, String notificationType, Boolean optedInStatus, Pageable pageable);

}