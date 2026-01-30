package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.UserMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserResponse;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

@Service
public class UserService
{
	private UserRepository userRepository;
    private final UserTypeRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
	@Autowired
	public UserService(
			UserRepository userRepository,
			UserTypeRepository userTypesRepository,
			PasswordEncoder passwordEncoder,
			UserMapper userMapper)
	{
		this.userRepository = userRepository;
        this.userTypesRepository = userTypesRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
	}
	
	public List<UserResponse> provideAllUsers()
	{
		List<User> users = userRepository.findAll();
		
		return users.stream()
				.map(user -> userMapper.toUserResponse(user))
				.collect(Collectors.toList());
		
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
