package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;

@Service
public class UserService
{
	private UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	public List<User> provideAllUsers()
	{
		return userRepository.findAll();
	}
	
	public User provideUserByUsername(String username)
	{
		return userRepository.findByUsername(username);
	}
	
	public User provideUserById(long id)
	{
		return userRepository.findUserById(id);
	}
	
	public void addUser(User user)
	{
		user.resetId(); // ensure id is not set
		userRepository.save(user);
	}
	
	public void updateUser(User user)
	{
		userRepository.save(user);
	}
	
}
