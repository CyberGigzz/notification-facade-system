package com.notificationsystem.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName; // Making this optional

    // One customer can have many addresses.
    // mappedBy="customer" tells JPA that the 'customer' field in the Address class owns this relationship.
    // cascade = CascadeType.ALL means if we save/delete a customer, its addresses are also saved/deleted.
    // orphanRemoval = true means if we remove an address from this list, it should be deleted from the database.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    // One customer can have many preferences.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Preference> preferences = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist // This method is called before the entity is first saved
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // This method is called before an existing entity is updated
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}