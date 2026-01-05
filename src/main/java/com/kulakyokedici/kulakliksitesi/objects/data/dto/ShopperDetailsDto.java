package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import jakarta.validation.constraints.NotNull;

public class ShopperDetailsDto {
	@NotNull(message = "Shopper ID cannot be null")
	private Long id;
	
	private String firstName;
	private String lastName;
	
	public Long getId()
	{
		return id;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
}
