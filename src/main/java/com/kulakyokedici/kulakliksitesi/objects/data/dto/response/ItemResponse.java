package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.List;

import com.kulakyokedici.kulakliksitesi.objects.data.Image;

public record ItemResponse(
		Long id,
		String title,
		Double price,
		String description,
		SellerResponse seller,
		List<Image> images
		)
{
	public ItemResponse(
			Long id,
			String title,
			Double price,
			String description,
			SellerResponse seller,
			List<Image> images)
	{
		this.id = id;
		this.title = title;
		this.price = price;
		this.description = description;
		this.seller = seller;
		this.images = images;
	}
}
