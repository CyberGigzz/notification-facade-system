package com.notificationsystem;

import com.notificationsystem.domain.Address;
import com.notificationsystem.domain.Admin;
import com.notificationsystem.domain.Customer;
import com.notificationsystem.domain.Preference;
import com.notificationsystem.domain.enums.AddressType;
import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.repository.AdminRepository;
import com.notificationsystem.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j // Lombok annotation for logging
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository; 

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            createInitialAdminUser();
        }

        // Load customer data if needed
        if (customerRepository.count() == 0) {
            log.info("No customers found. Seeding sample customer data...");
            createSampleCustomers();
            log.info("Sample customer data seeded successfully.");
        } else {
            log.info("Customer data already exists. Skipping seeding.");
        }
    }

    private void createInitialAdminUser() {
        if (adminRepository.count() == 0) {
            log.info("No admins found in the database. Creating initial admin user...");

            // 1. Define the plain text password
            String adminPassword = "password";

            // 2. Hash the password using the PasswordEncoder bean
            String hashedPassword = passwordEncoder.encode(adminPassword);

            // 3. Create the new Admin entity
            Admin initialAdmin = new Admin();
            initialAdmin.setUsername("admin");
            initialAdmin.setPassword(hashedPassword);
            initialAdmin.setRole("ROLE_ADMIN"); // The role must start with "ROLE_"

            // 4. Save the new admin to the database
            adminRepository.save(initialAdmin);

            log.info("Initial admin user 'admin' with password 'password' created successfully.");
            log.warn("!!! IMPORTANT: This is a development-only password. Please change it in a production environment. !!!");
        } else {
            log.info("Admin user(s) already exist in the database. Skipping data loading.");
        }
    }

    private void createSampleCustomers() {
        List<String> firstNames = Arrays.asList("John", "Jane", "Peter", "Mary", "David", "Susan", "Michael", "Linda", "James", "Patricia");
        List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez");

        for (int i = 0; i < 25; i++) {
            String firstName = firstNames.get(i % firstNames.size());
            String lastName = lastNames.get(i % lastNames.size());
            String email = (firstName + "." + lastName).toLowerCase() + i + "@example.com";

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);

            // Add an email address
            Address emailAddress = new Address();
            emailAddress.setAddressType(AddressType.EMAIL);
            emailAddress.setValue(email);
            emailAddress.setCustomer(customer);
            customer.getAddresses().add(emailAddress);

            // Add an SMS address for some customers
            if (i % 3 == 0) {
                Address smsAddress = new Address();
                smsAddress.setAddressType(AddressType.SMS);
                smsAddress.setValue("+155512345" + String.format("%02d", i));
                smsAddress.setCustomer(customer);
                customer.getAddresses().add(smsAddress);
            }

            // Add some preferences
            Preference emailPref = new Preference();
            emailPref.setNotificationType(NotificationType.MARKETING_EMAIL);
            emailPref.setOptedIn(i % 2 == 0); // Alternate opt-in status
            emailPref.setCustomer(customer);
            customer.getPreferences().add(emailPref);

            if (i % 3 == 0) {
                Preference smsPref = new Preference();
                smsPref.setNotificationType(NotificationType.MARKETING_SMS);
                smsPref.setOptedIn(i % 4 == 0);
                smsPref.setCustomer(customer);
                customer.getPreferences().add(smsPref);
            }
            
            customerRepository.save(customer);
        }
    }
}