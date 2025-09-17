package com.kulakyokedici.kulakliksitesi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long>
{
	public Admin findAdminById(Long id);
	
	public Admin findByUsername(String username);
	
	public Admin findByEmail(String email);
	
	public List<Admin> findAll();
}
