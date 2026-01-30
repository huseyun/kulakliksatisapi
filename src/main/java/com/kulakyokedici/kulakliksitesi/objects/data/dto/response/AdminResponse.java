package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.Set;

public record AdminResponse(
		String username,
		String email) 
{
	public AdminResponse(
			String username,
			String email)
	{
		this.username = username;
		this.email = email;
	}
}
