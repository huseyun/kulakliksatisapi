package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.AdminMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.UserCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.UserUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.AdminResponse;
import com.kulakyokedici.kulakliksitesi.objects.exception.ResourceNotFoundException;
import com.kulakyokedici.kulakliksitesi.repository.AdminRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminService
{
	private AdminRepository adminRepository;
    private final UserTypeRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	private final AdminMapper adminMapper;
	
	@Autowired
	public AdminService(AdminRepository adminRepository,
			UserTypeRepository userTypesRepository,
			PasswordEncoder passwordEncoder,
			AdminMapper adminMapper)
	{
		this.adminRepository = adminRepository;
		this.userTypesRepository = userTypesRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminMapper = adminMapper;
	}
	
	public List<AdminResponse> getAllAdmins()
	{
		List<Admin> admins = adminRepository.findAll();
		
		return admins.stream()
				.map(admin -> adminMapper.toResponse(admin))
				.collect(Collectors.toList());
	}
	
	public AdminResponse getAdminById(Long id)
	{
		Admin admin = adminRepository.findAdminById(id)
				.orElseThrow(() -> new ResourceNotFoundException("admin", "id", id));
		
		return adminMapper.toResponse(admin);
	}
	
	public AdminResponse getAdminByUsername(String username)
	{
		Admin admin = adminRepository.findAdminByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("admin", "username", username));
		
		return adminMapper.toResponse(admin);
	}
	
	public AdminResponse getAdminByEmail(String email)
	{
		Admin admin = adminRepository.findAdminByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("admin", "email", email));
		
		return adminMapper.toResponse(admin);
	}
	
	public void addAdmin(UserCreateRequest newAdmin)
	{
		Admin admin = new Admin();
		
		admin.setUsername(newAdmin.username());
		admin.setEmail(newAdmin.email());
		admin.setPassword(passwordEncoder.encode(newAdmin.password()));
		
		admin.getUserTypes().add(userTypesRepository.findByName(EUserType.ADMIN)
				.orElseThrow(() -> new ResourceNotFoundException("user type", "user type name", EUserType.ADMIN.name())));
		
        adminRepository.save(admin);
	}
	
	@Transactional
	public void updateAdmin(Long adminId, UserUpdateRequest newAdmin)
	{
		Admin existing = adminRepository.findAdminById(adminId)
				.orElseThrow(() -> new ResourceNotFoundException("admin", "id", adminId)); 
	}
}
