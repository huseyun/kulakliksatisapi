package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.Set;

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
