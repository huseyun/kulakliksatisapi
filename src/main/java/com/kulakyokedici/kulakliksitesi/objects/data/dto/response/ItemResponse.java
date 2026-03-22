package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.Set;

import com.kulakyokedici.kulakliksitesi.objects.data.Image;

public record ItemResponse(
		String name,
		Double price,
		SellerResponse seller,
		Set<Image> images
		)
{
	public ItemResponse(String name,
			Double price,
			SellerResponse seller,
			Set<Image> images)
	{
		this.name = name;
		this.price = price;
		this.seller = seller;
		this.images = images;
	}
}
