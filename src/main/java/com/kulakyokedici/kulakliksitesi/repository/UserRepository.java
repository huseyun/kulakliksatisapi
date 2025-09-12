package com.kulakyokedici.kulakliksitesi.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.User;

public interface UserRepository extends CrudRepository<User, Long>
{
	public User findUserById(Long id);
	
	public User findByUsername(String username);
	
	public List<User> findAll();
}