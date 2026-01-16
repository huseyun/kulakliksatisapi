package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import java.util.Set;

public record SellerDetailedResponse(
		String username,
		String email,
		String companyName,
		Set<ItemSummaryResponse> items)
{
	public SellerDetailedResponse(
			String username,
			String email,
			String companyName,
			Set<ItemSummaryResponse> items)
	{
		this.username = username;
		this.email = email;
		this.companyName = companyName;
		this.items = items;
	}
}
