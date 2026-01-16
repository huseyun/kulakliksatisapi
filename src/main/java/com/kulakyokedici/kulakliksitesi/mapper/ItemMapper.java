package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ItemResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.SellerResponse;

@Component
public class ItemMapper
{
	
	@Autowired
	public ItemMapper() {}
	
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
