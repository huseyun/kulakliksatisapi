package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserTypeResponse;

@Component
public class UserMapper {
	
	public UserResponse toUserResponse(User user) {
		return new UserResponse(
				user.getUsername(),
				user.getEmail(),
				user.getUserTypes().stream()
					.map(userType -> new UserTypeResponse(userType.getName()))
					.collect(java.util.stream.Collectors.toSet()));
	}
}
