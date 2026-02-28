package com.kulakyokedici.kulakliksitesi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long>
{
	public Optional<Admin> findAdminById(Long id);
	
	public Optional<Admin> findAdminByUsername(String username);
	
	public Optional<Admin> findAdminByEmail(String email);
	
	public Admin findByEmail(String email);
	
	public List<Admin> findAll();
}
