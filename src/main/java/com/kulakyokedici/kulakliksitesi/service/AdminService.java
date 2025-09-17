package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.repository.AdminRepository;

@Service
public class AdminService
{
	private AdminRepository adminRepository;
	
	@Autowired
	public AdminService(AdminRepository adminRepository)
	{
		this.adminRepository = adminRepository;
	}
	
	public List<Admin> provideAllAdmins()
	{
		return adminRepository.findAll();
	}
}
