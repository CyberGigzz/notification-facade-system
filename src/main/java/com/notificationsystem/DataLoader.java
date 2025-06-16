package com.notificationsystem;

import com.notificationsystem.domain.Admin;
import com.notificationsystem.domain.Customer;
import com.notificationsystem.domain.NotificationLog;
import com.notificationsystem.domain.Address;
import com.notificationsystem.domain.Preference;
import com.notificationsystem.domain.enums.AddressType;
import com.notificationsystem.domain.enums.NotificationStatus;
import com.notificationsystem.domain.enums.NotificationType;
import com.notificationsystem.repository.AdminRepository;
import com.notificationsystem.repository.CustomerRepository;
import com.notificationsystem.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    private final NotificationLogRepository notificationLogRepository; 
    private final Random random = new Random(); 

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

        if (notificationLogRepository.count() == 0 && customerRepository.count() > 0) {
            log.info("No notification logs found. Seeding sample log data...");
            createSampleNotificationLogs();
            log.info("Sample notification log data seeded successfully.");
        }
    }

    private void createInitialAdminUser() {
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
        List<String> firstNames = Arrays.asList("John", "Jane", "Peter", "Mary", "David", "Susan", "Michael", "Linda", "James", "Patricia", "Robert", "Jennifer", "William", "Elizabeth", "Richard", "Maria", "Charles", "Nancy", "Joseph", "Karen");
        List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "White");

        for (int i = 0; i < 50; i++) { // Let's create 50 customers
            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            String email = (firstName + "." + lastName).toLowerCase() + i + "@example.com";

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);

            addAddress(customer, AddressType.EMAIL, email);

            if (random.nextBoolean()) {
                String phone = "+1555" + String.format("%07d", random.nextInt(10000000));
                addAddress(customer, AddressType.SMS, phone);
            }

            addPreference(customer, NotificationType.MARKETING_EMAIL, random.nextBoolean());
            addPreference(customer, NotificationType.TRANSACTIONAL_EMAIL, true); 

            if (customer.getAddresses().stream().anyMatch(a -> a.getAddressType() == AddressType.SMS)) {
                addPreference(customer, NotificationType.MARKETING_SMS, random.nextBoolean());
                addPreference(customer, NotificationType.SHIPPING_UPDATES, random.nextBoolean());
            }

            customerRepository.save(customer);
        }
    }

    private void createSampleNotificationLogs() {
        List<Customer> allCustomers = customerRepository.findAllWithAddresses();
        if (allCustomers.isEmpty()) {
            return; 
        }

        for (Customer customer : allCustomers) {
            int logCount = 1 + random.nextInt(5);
            for (int i = 0; i < logCount; i++) {
                List<Address> addresses = customer.getAddresses();
                if (addresses.isEmpty()) {
                    continue;
                }
                Address randomAddress = addresses.get(random.nextInt(addresses.size()));

                NotificationLog log = new NotificationLog();
                log.setAddress(randomAddress);
                
                log.setSentAt(LocalDateTime.now().minusHours(random.nextInt(72)));

                int statusRoll = random.nextInt(100); 
                if (statusRoll < 80) { 
                    log.setStatus(NotificationStatus.DELIVERED);
                    log.setStatusDetails("Successfully delivered by provider.");
                } else if (statusRoll < 95) { 
                    log.setStatus(NotificationStatus.FAILED);
                    log.setStatusDetails("Carrier network error.");
                } else { 
                    log.setStatus(NotificationStatus.BOUNCED);
                    log.setStatusDetails("Mailbox does not exist.");
                }
                
                notificationLogRepository.save(log);
            }
        }
    }

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