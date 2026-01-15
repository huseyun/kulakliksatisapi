package com.kulakyokedici.kulakliksitesi.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;
import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

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

        if (!userRepository.existsByUsername("admin")) 
        {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setEmail("admin@admin.com");

            UserType adminRole = userTypeRepository.findByName(EUserType.ADMIN);
            
            admin.setUserTypes(new HashSet<>(Set.of(adminRole))); 

            userRepository.save(admin);
        }
        
        if (!userRepository.existsByUsername("shopper"))
        {
        	Shopper shopper = new Shopper();
        	shopper.setUsername("shopper");
        	shopper.setPassword(passwordEncoder.encode("shopperpass"));
        	shopper.setEmail("shopper@shopper.com");
        	shopper.setFirstName("ali");
        	shopper.setLastName("fazaoglu");
        	
        	UserType shopperRole = userTypeRepository.findByName(EUserType.SHOPPER);
        	
        	shopper.setUserTypes(new HashSet<>(Set.of(shopperRole)));
        	
        	userRepository.save(shopper);
        }
        
        if (!userRepository.existsByUsername("seller"))
        {
        	Seller seller = new Seller();
        	seller.setUsername("seller");
        	seller.setPassword(passwordEncoder.encode("shopperpass"));
        	seller.setEmail("seller@seller.com");
        	seller.setCompanyName("ödemiş ltd şti");
        	
        	UserType sellerRole = userTypeRepository.findByName(EUserType.SELLER);
        	
        	seller.setUserTypes(new HashSet<>(Set.of(sellerRole)));
        	
        	userRepository.save(seller);
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