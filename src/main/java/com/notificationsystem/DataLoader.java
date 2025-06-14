package com.notificationsystem;

import com.notificationsystem.domain.Admin;
import com.notificationsystem.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j // Lombok annotation for logging
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // We will create an initial admin user only if one doesn't already exist.
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
}