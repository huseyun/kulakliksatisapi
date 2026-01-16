package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

@Service
public class UserService
{
	private UserRepository userRepository;
    private final UserTypeRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, UserTypeRepository userTypesRepository, PasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
        this.userTypesRepository = userTypesRepository;
		this.passwordEncoder = passwordEncoder;
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
	
	public User provideUserByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	
//	@Transactional
//	public void updateUser(Long userId, UserUpdateRequest newUser)
//	{
//		User existing = userRepository.findUserById(userId);
//		existing.fullUpdate(user);
//	}
	
}
