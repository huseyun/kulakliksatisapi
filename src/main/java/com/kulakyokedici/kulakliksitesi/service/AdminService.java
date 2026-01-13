package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserInfoRequest;
import com.kulakyokedici.kulakliksitesi.repository.AdminRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypesRepository;

import jakarta.transaction.Transactional;

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
	
	public void addAdmin(UserInfoRequest newAdmin)
	{
		Admin admin = new Admin();
		
		admin.setUsername(newAdmin.getUsername());
		admin.setEmail(newAdmin.getEmail());
		admin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
		
		admin.getUserTypes().add(userTypesRepository.findByName("ADMIN"));
		
        adminRepository.save(admin);
	}
	
	@Transactional
	public void updateAdmin(Admin admin)
	{
		Admin existing = adminRepository.findAdminById(admin.getId());
		existing.fullUpdate(admin);
		
	    if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
	        existing.setPassword(passwordEncoder.encode(admin.getPassword()));
	    }
	}
}
