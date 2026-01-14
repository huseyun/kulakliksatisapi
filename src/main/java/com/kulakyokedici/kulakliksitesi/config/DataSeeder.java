// AI

package com.kulakyokedici.kulakliksitesi.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

import jakarta.transaction.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserTypeRepository userTypeRepository, 
                      UserRepository userRepository, 
                      PasswordEncoder passwordEncoder) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        createRoleIfNotFound(EUserType.ADMIN);
        createRoleIfNotFound(EUserType.SELLER);
        createRoleIfNotFound(EUserType.SHOPPER);

        if (!userRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));

            UserType adminRole = userTypeRepository.findByName(EUserType.ADMIN);
            
            admin.setUserTypes(new HashSet<>(Set.of(adminRole))); 

            userRepository.save(admin);
        }
    }

    private void createRoleIfNotFound(EUserType roleName) {
        if (userTypeRepository.findByName(roleName) == null) {
            UserType role = new UserType();
            role.setName(roleName);
            userTypeRepository.save(role);
        }
    }
}