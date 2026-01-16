package com.kulakyokedici.kulakliksitesi.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.ItemMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ItemResponse;
import com.kulakyokedici.kulakliksitesi.repository.ItemRepository;

@Service
public class ItemService
{
	private final ItemRepository itemRepository;
	private final ItemMapper itemMapper;
	
	public ItemService(ItemRepository itemRepository, ItemMapper itemMapper)
	{
		this.itemRepository = itemRepository;
		this.itemMapper = itemMapper;
	}
	
	public Set<ItemResponse> getItemsBySellerId(Long sellerId)
	{
		Set<Item> items = itemRepository.findBySellerId(sellerId);
		
		Set<ItemResponse> responseItems = items.stream()
				.map(i -> itemMapper.toResponse(i))
				.collect(Collectors.toSet());
		return responseItems;
	}
}
