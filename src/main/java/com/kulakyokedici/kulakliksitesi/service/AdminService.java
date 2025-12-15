package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.repository.AdminRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypesRepository;

@Service
public class AdminService
{
	private AdminRepository adminRepository;
    private final UserTypesRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public AdminService(AdminRepository adminRepository,
			UserTypesRepository userTypesRepository,
			PasswordEncoder passwordEncoder)
	{
		this.adminRepository = adminRepository;
		this.userTypesRepository = userTypesRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<Admin> provideAllAdmins()
	{
		return adminRepository.findAll();
	}
	
	public void addAdmin(Admin admin)
	{
		admin.resetId();
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Set<UserType> managedUserTypes = admin.getUserTypes().stream()
                .map(ut -> userTypesRepository.findById(ut.getId()).orElse(null))
                .filter(ut -> ut != null)
                .collect(Collectors.toSet());
        
        admin.setUserTypes(managedUserTypes);
        
        adminRepository.save(admin);
	}
}
