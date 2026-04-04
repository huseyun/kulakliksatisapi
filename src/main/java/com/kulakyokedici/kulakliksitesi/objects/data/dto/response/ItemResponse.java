package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.Set;

import com.kulakyokedici.kulakliksitesi.objects.data.Image;

public record ItemResponse(
		String title,
		Double price,
		String description,
		SellerResponse seller,
		Set<Image> images
		)
{
	public ItemResponse(String title,
			Double price,
			String description,
			SellerResponse seller,
			Set<Image> images)
	{
		this.title = title;
		this.price = price;
		this.description = description;
		this.seller = seller;
		this.images = images;
	}
}
