package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import java.util.Set;

public record SellerResponse(
		String username,
		String password,
		String companyName)
{
	public SellerResponse(
			String username,
			String password,
			String companyName)
	{
		this.username = username;
		this.password = password;
		this.companyName = companyName;
	}
}
