package com.notificationsystem;

import com.notificationsystem.domain.Admin;
import com.notificationsystem.domain.Customer;
import com.notificationsystem.domain.Address;
import com.notificationsystem.domain.Preference;
import com.notificationsystem.domain.enums.AddressType;
import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.repository.AdminRepository;
import com.notificationsystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final Random random = new Random(); // Create a single Random instance

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            createInitialAdminUser();
        }

        if (customerRepository.count() == 0) {
            log.info("No customers found. Seeding sample customer data...");
            createSampleCustomers();
            log.info("Sample customer data seeded successfully.");
        } else {
            log.info("Customer data already exists. Skipping seeding.");
        }
    }

    private void createInitialAdminUser() {
        // ... same admin creation logic ...
        log.info("No admins found in the database. Creating initial admin user...");
        String adminPassword = "password";
        String hashedPassword = passwordEncoder.encode(adminPassword);
        Admin initialAdmin = new Admin();
        initialAdmin.setUsername("admin");
        initialAdmin.setPassword(hashedPassword);
        initialAdmin.setRole("ROLE_ADMIN");
        adminRepository.save(initialAdmin);
        log.info("Initial admin user 'admin' with password 'password' created successfully.");
        log.warn("!!! IMPORTANT: This is a development-only password. Please change it in a production environment. !!!");
    }

    private void createSampleCustomers() {
        // Expanded lists for more variety
        List<String> firstNames = Arrays.asList("John", "Jane", "Peter", "Mary", "David", "Susan", "Michael", "Linda", "James", "Patricia", "Robert", "Jennifer", "William", "Elizabeth", "Richard", "Maria", "Charles", "Nancy", "Joseph", "Karen");
        List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "White");

        for (int i = 0; i < 50; i++) { // Let's create 50 customers
            // Use Random to pick names, creating more unique combinations
            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            String email = (firstName + "." + lastName).toLowerCase() + i + "@example.com";

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);

            // Add an email address for every customer
            addAddress(customer, AddressType.EMAIL, email);

            // Add an SMS address for roughly half of the customers
            if (random.nextBoolean()) {
                String phone = "+1555" + String.format("%07d", random.nextInt(10000000));
                addAddress(customer, AddressType.SMS, phone);
            }

            // Add some preferences with random opt-in status
            addPreference(customer, NotificationType.MARKETING_EMAIL, random.nextBoolean());
            addPreference(customer, NotificationType.TRANSACTIONAL_EMAIL, true); // Transactional are usually always on

            if (customer.getAddresses().stream().anyMatch(a -> a.getAddressType() == AddressType.SMS)) {
                addPreference(customer, NotificationType.MARKETING_SMS, random.nextBoolean());
                addPreference(customer, NotificationType.SHIPPING_UPDATES, random.nextBoolean());
            }

            customerRepository.save(customer);
        }
    }
    
    // Helper methods to reduce code duplication
    private void addAddress(Customer customer, AddressType type, String value) {
        Address address = new Address();
        address.setAddressType(type);
        address.setValue(value);
        address.setCustomer(customer);
        customer.getAddresses().add(address);
    }

    private void addPreference(Customer customer, NotificationType type, boolean optedIn) {
        Preference preference = new Preference();
        preference.setNotificationType(type);
        preference.setOptedIn(optedIn);
        preference.setCustomer(customer);
        customer.getPreferences().add(preference);
    }
}