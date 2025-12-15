package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypesRepository;

@Service
public class UserService
{
	private UserRepository userRepository;
    private final UserTypesRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, UserTypesRepository userTypesRepository, PasswordEncoder passwordEncoder)
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
	
	public void addUser(User user)
	{
		user.resetId();
		user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<UserType> managedUserTypes = user.getUserTypes().stream()
                .map(ut -> userTypesRepository.findById(ut.getId()).orElse(null))
                .filter(ut -> ut != null)
                .collect(Collectors.toSet());
        
        user.setUserTypes(managedUserTypes);
        
		userRepository.save(user);
	}
	
	public void updateUser(User user)
	{
		userRepository.save(user);
	}
	
}
