package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserUpdateRequest;
import com.kulakyokedici.kulakliksitesi.repository.AdminRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminService
{
	private AdminRepository adminRepository;
    private final UserTypeRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public AdminService(AdminRepository adminRepository,
			UserTypeRepository userTypesRepository,
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
	
	public void addAdmin(UserCreateRequest newAdmin)
	{
		Admin admin = new Admin();
		
		admin.setUsername(newAdmin.username());
		admin.setEmail(newAdmin.email());
		admin.setPassword(passwordEncoder.encode(newAdmin.password()));
		
		admin.getUserTypes().add(userTypesRepository.findByName(EUserType.ADMIN));
		
        adminRepository.save(admin);
	}
	
	@Transactional
	public void updateAdmin(Long adminId, UserUpdateRequest newAdmin)
	{
		Admin existing = adminRepository.findAdminById(adminId);
	}
}
