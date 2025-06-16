package com.notificationsystem.repository;

import com.notificationsystem.domain.NotificationLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    @Query("SELECT log FROM NotificationLog log WHERE log.address.customer.id = :customerId ORDER BY log.sentAt DESC")
    Page<NotificationLog> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}