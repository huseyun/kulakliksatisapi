package com.kulakyokedici.kulakliksitesi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.service.ItemService;

@RestController
@RequestMapping("api")
public class ItemController
{
	private final ItemService itemService;
	
	public ItemController(
			ItemService itemService)
	{
		this.itemService = itemService;
	}
	
	@GetMapping("/items")
	public ResponseEntity<List<ItemSummaryResponse>> getSummaryItemList()
	{
		return ResponseEntity.ok(itemService.getSummaryAll());
	}
}
