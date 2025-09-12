package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.repository.ItemRepository;

@Service
public class ItemService
{
	private ItemRepository itemRepository;
	
	public ItemService(ItemRepository itemRepository)
	{
		this.itemRepository = itemRepository;
	}
	
	public List<Item> getItemsBySellerId(Long sellerId)
	{
		return itemRepository.findBySellerId(sellerId);
	}
}
