package com.notificationsystem.repository;

import com.notificationsystem.domain.Preference;
import com.notificationsystem.domain.enums.NotificationType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    @Query("SELECT p.isOptedIn, COUNT(p.customer.id) FROM Preference p WHERE p.notificationType = :type GROUP BY p.isOptedIn")
    List<Object[]> countByOptInStatusForType(@Param("type") NotificationType type);
}