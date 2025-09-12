package com.kulakyokedici.kulakliksitesi.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.objects.security.SecurityUser;
import com.kulakyokedici.kulakliksitesi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService 
{

 private UserRepository userRepository;

 @Autowired
 public CustomUserDetailsService(UserRepository userRepository) 
 {
     this.userRepository = userRepository;
 }

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
 {
	 User user = userRepository.findByUsername(username);
	 
	 System.out.println(user.getPassword());
	 System.out.println(user.getUsername());
	 user.getUserTypes().forEach((userTypes) -> System.out.println(userTypes.getName()));
	 
     return new SecurityUser(user);
 	}
}