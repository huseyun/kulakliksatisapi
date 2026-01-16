package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ItemResponse;

@Component
public class ItemMapper
{
	private final SellerMapper sellerMapper;
	
	@Autowired
	public ItemMapper(SellerMapper sellerMapper)
	{
		this.sellerMapper = sellerMapper;
	}
	
	public ItemResponse toResponse(Item item)
	{
		return new ItemResponse(item.getItemName(),
				item.getItemPrice(),
				sellerMapper.toResponse(item.getSeller()),
				item.getImages());
	}
}
