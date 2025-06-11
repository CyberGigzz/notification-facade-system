package com.notificationsystem.domain;

import com.notificationsystem.domain.enums.AddressType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many addresses can belong to one customer.
    // This is the owning side of the relationship.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false) // This creates the foreign key column.
    private Customer customer;

    @Enumerated(EnumType.STRING) // Store the enum as a string ("EMAIL") instead of a number (0).
    @Column(nullable = false)
    private AddressType addressType;

    @Column(nullable = false)
    private String value; // e.g., "john.doe@example.com" or "+15551234567"
}