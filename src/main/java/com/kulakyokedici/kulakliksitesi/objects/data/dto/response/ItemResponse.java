package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.Set;

import com.kulakyokedici.kulakliksitesi.objects.data.Image;

public record ItemResponse(
		String itemName,
		Double itemPrice,
		SellerResponse seller,
		Set<Image> images
		)
{
	public ItemResponse(String itemName,
			Double itemPrice,
			SellerResponse seller,
			Set<Image> images)
	{
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.seller = seller;
		this.images = images;
	}
}
