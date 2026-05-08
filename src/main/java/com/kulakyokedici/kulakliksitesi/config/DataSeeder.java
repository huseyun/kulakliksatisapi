package com.kulakyokedici.kulakliksitesi.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.Category;
import com.kulakyokedici.kulakliksitesi.objects.data.ECategory;
import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;
import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.objects.exception.EErrorCode;
import com.kulakyokedici.kulakliksitesi.objects.exception.ResourceNotFoundException;
import com.kulakyokedici.kulakliksitesi.repository.CategoryRepository;
import com.kulakyokedici.kulakliksitesi.repository.ItemRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

import jakarta.transaction.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    public DataSeeder(UserTypeRepository userTypeRepository, 
                      UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      ItemRepository itemRepository,
                      CategoryRepository categoryRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        
        createRoleIfNotFound(EUserType.ADMIN);
        createRoleIfNotFound(EUserType.SELLER);
        createRoleIfNotFound(EUserType.SHOPPER);
        
        createCategoryIfNotFound(ECategory.IN_EAR);
        createCategoryIfNotFound(ECategory.ON_EAR);
        createCategoryIfNotFound(ECategory.OVER_EAR);

        if (!userRepository.existsByUsername("admin")) 
        {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setEmail("admin@admin.com");

            UserType adminRole = userTypeRepository.findByName(EUserType.ADMIN)
            		.orElseThrow(() -> new ResourceNotFoundException("user type", "user type name", EUserType.ADMIN.name(), EErrorCode.USERTYPE_NOT_FOUND));
            
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
        	
        	UserType shopperRole = userTypeRepository.findByName(EUserType.SHOPPER)
        			.orElseThrow(() -> new ResourceNotFoundException("user type", "user type name", EUserType.SHOPPER.name(), EErrorCode.USERTYPE_NOT_FOUND));
        	
        	shopper.setUserTypes(new HashSet<>(Set.of(shopperRole)));
        	
        	userRepository.save(shopper);
        }
        
        if (!userRepository.existsByUsername("seller"))
        {
        	Seller seller = new Seller();
        	seller.setUsername("seller");
        	seller.setPassword(passwordEncoder.encode("sellerpass"));
        	seller.setEmail("seller@seller.com");
        	seller.setCompanyName("ödemiş ltd şti");
        	
        	UserType sellerRole = userTypeRepository.findByName(EUserType.SELLER)
        			.orElseThrow(() -> new ResourceNotFoundException("user type", "user type name", EUserType.SELLER.name(), EErrorCode.USERTYPE_NOT_FOUND));
        	
        	seller.setUserTypes(new HashSet<>(Set.of(sellerRole)));
        	
        	userRepository.save(seller);
        	
        	
        	Item item = new Item();
        	item.setTitle("logitek g502");
        	item.setPrice(550.0);
        	item.setSeller(seller);
        	item.setDescription("herhangi bir eşya");
        	item.setRecommended(true);
        	
        	Category overEarCategory = categoryRepository.findByCategory(ECategory.OVER_EAR)
        			.orElseThrow(() -> new ResourceNotFoundException("category", "category name", ECategory.OVER_EAR.name(), EErrorCode.CATEGORY_NOT_FOUND));
        	
        	item.setCategory(overEarCategory);
        	
        	itemRepository.save(item);
        	
        	seller.getItems().add(item);
        }
        
    }

    private void createRoleIfNotFound(EUserType roleName) {
        if (userTypeRepository.findByName(roleName).isEmpty()) {
            UserType role = new UserType();
            role.setName(roleName);
            userTypeRepository.save(role);
        }
    }
    
    private void createCategoryIfNotFound(
    		ECategory category) {
        if (categoryRepository.findByCategory(category).isEmpty()) {
            Category newCategory = new Category();
            newCategory.setCategory(category);
            categoryRepository.save(newCategory);
        }
    }
}