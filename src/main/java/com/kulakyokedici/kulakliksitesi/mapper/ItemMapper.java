package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerResponse;

@Component
public class ItemMapper
{
	
	public ItemResponse toResponse(Item item)
	{
		return new ItemResponse(item.getItemName(),
				item.getItemPrice(),
				new SellerResponse(
						item.getSeller().getUsername(),
						item.getSeller().getEmail(),
						item.getSeller().getCompanyName()),
				item.getImages());
	}
	
	public ItemSummaryResponse toSummaryResponse(Item item)
	{
		return new ItemSummaryResponse(
				item.getItemName(),
				item.getItemPrice(),
				item.getImages());
	}
}
