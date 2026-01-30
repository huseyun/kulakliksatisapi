package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

public record SellerResponse(
		String username,
		String email,
		String companyName)
{
	public SellerResponse(
			String username,
			String email,
			String companyName)
	{
		this.username = username;
		this.email = email;
		this.companyName = companyName;
	}
}
