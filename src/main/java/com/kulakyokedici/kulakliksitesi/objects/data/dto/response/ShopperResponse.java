package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

public record ShopperResponse(
		String username,
		String email,
		String firstName,
		String lastName) 
{
	public ShopperResponse(
		String username,
		String email,
		String firstName,
		String lastName)
	{
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
}
