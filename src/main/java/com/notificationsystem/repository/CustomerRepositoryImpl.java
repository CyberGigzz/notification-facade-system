package com.notificationsystem.repository;

import com.notificationsystem.domain.Customer;
import com.notificationsystem.dto.AddressDTO;
import com.notificationsystem.dto.CustomerDTO;
import com.notificationsystem.dto.PreferenceDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CustomerDTO> findByCriteria(String keyword, String notificationType, Boolean optedInStatus, Pageable pageable) {
        
        // --- 1. Build the WHERE clause and parameters dynamically ---
        StringBuilder whereClause = new StringBuilder("WHERE 1=1 ");
        Map<String, Object> parameters = new HashMap<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            whereClause.append("AND (LOWER(c.firstName) LIKE LOWER(:keyword) OR LOWER(c.lastName) LIKE LOWER(:keyword) OR EXISTS (SELECT 1 FROM Address a WHERE a.customer = c AND LOWER(a.value) LIKE LOWER(:keyword))) ");
            parameters.put("keyword", "%" + keyword.trim() + "%");
        }

        // We need a subquery for preference filtering to handle complex cases
        if (notificationType != null && !notificationType.isEmpty() || optedInStatus != null) {
            whereClause.append("AND c.id IN (SELECT p.customer.id FROM Preference p WHERE 1=1 ");
            StringBuilder subQueryWhere = new StringBuilder();
            if (notificationType != null && !notificationType.isEmpty()) {
                subQueryWhere.append("AND p.notificationType = :notificationType ");
                parameters.put("notificationType", com.notificationsystem.domain.enums.NotificationType.valueOf(notificationType));
            }
            if (optedInStatus != null) {
                subQueryWhere.append("AND p.isOptedIn = :optedInStatus ");
                parameters.put("optedInStatus", optedInStatus);
            }
            whereClause.append(subQueryWhere).append(") ");
        }

        // --- 2. Build the main data query and the count query ---
        String commonQueryPart = "FROM Customer c " + whereClause;
        String dataJpql = "SELECT c " + commonQueryPart;
        String countJpql = "SELECT COUNT(c.id) " + commonQueryPart;

        // --- 3. Build the ORDER BY clause safely ---
        StringBuilder orderByClause = new StringBuilder();
        if (pageable.getSort().isSorted()) {
            orderByClause.append(" ORDER BY ");
            List<String> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                orders.add("c." + order.getProperty() + " " + order.getDirection().name());
            }
            orderByClause.append(String.join(", ", orders));
        } else {
            orderByClause.append(" ORDER BY c.id asc"); // Default sort
        }

        // --- 4. Create and execute the count query ---
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        parameters.forEach(countQuery::setParameter);
        long total = countQuery.getSingleResult();
        
        // --- 5. Create and execute the data query with pagination and sorting ---
        TypedQuery<Customer> query = entityManager.createQuery(dataJpql + orderByClause, Customer.class);
        parameters.forEach(query::setParameter);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Customer> customers = query.getResultList();

        // --- 6. Convert to DTO and return the Page object ---
        List<CustomerDTO> dtos = customers.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, total);
    }

    // A complete DTO converter. The previous one was incomplete.
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        if (customer.getAddresses() != null) {
            dto.setAddresses(customer.getAddresses().stream().map(address -> {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setId(address.getId());
                addressDTO.setAddressType(address.getAddressType());
                addressDTO.setValue(address.getValue());
                return addressDTO;
            }).collect(Collectors.toList()));
        }

        if (customer.getPreferences() != null) {
            dto.setPreferences(customer.getPreferences().stream().map(preference -> {
                PreferenceDTO preferenceDTO = new PreferenceDTO();
                preferenceDTO.setId(preference.getId());
                preferenceDTO.setNotificationType(preference.getNotificationType());
                preferenceDTO.setOptedIn(preference.isOptedIn());
                return preferenceDTO;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}