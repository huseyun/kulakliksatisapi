package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import java.util.Set;

import com.kulakyokedici.kulakliksitesi.objects.data.Image;
import com.kulakyokedici.kulakliksitesi.objects.data.Seller;

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
