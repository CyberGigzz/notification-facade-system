package com.notificationsystem.service;

import com.notificationsystem.domain.Admin;
import com.notificationsystem.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch our Admin entity from the database using the repository.
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with username: " + username));

        // 2. Convert our Admin entity's role into a Spring Security GrantedAuthority.
        //    The role in the database should be stored as "ROLE_ADMIN".
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()));

        // 3. Return a new User object. This is Spring Security's standard UserDetails implementation.
        //    We provide the username, the HASHED password from our database, and the authorities.
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}