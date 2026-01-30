package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import java.util.Set;

import com.kulakyokedici.kulakliksitesi.objects.data.UserType;

public record UserResponse(
		String username,
		String email,
		Set<UserTypeResponse> userType) 
{
	public UserResponse(
			String username,
			String email,
			Set<UserTypeResponse> userType)
	{
		this.username = username;
		this.email = email;
		this.userType = userType;
	}
}
