package com.kulakyokedici.kulakliksitesi.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ItemCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ItemUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerResponse;
import com.kulakyokedici.kulakliksitesi.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/items")
public class ItemController
{
	private final ItemService itemService;
	
	public ItemController(
			ItemService itemService)
	{
		this.itemService = itemService;
	}
	
	/*
	 * GET istekleri
	 */
	
	@GetMapping
	public ResponseEntity<List<ItemSummaryResponse>> getSummaryItemList()
	{
		return ResponseEntity.ok(itemService.getSummaryAll());
	}
	
	@GetMapping("/search")
    public ResponseEntity<List<ItemSummaryResponse>> searchItems(
    		@RequestParam String keyword) {
        List<ItemSummaryResponse> results = itemService.search(keyword);
        return ResponseEntity.ok(results);
    }
	
	@PreAuthorize("hasRole('SELLER')")
	@GetMapping("/{id}")
	public ResponseEntity<ItemResponse> getItemById(
			@PathVariable Long id)
	{
		return ResponseEntity.ok(itemService.getById(id));
	}
	
	/*
	 * PUT istekleri
	 */
	
	@PreAuthorize("hasRole('SELLER')")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateItem(
			@PathVariable Long id,
			@RequestBody ItemUpdateRequest req,
			Principal principal)
	{
		SellerResponse sellerResp = itemService.getSellerById(id);
		
		if(!sellerResp.username().equals(principal.getName()))
			throw new AccessDeniedException("bunu yapmaya yetkiniz yok.");
		
		itemService.update(id, req);
		
		return ResponseEntity.noContent().build();
	}
	
	/*
	 * POST istekleri
	 */
	
	@PreAuthorize("hasRole('SELLER')")
	@PostMapping
	public ResponseEntity<ItemResponse> createItem(
			@Valid @RequestBody ItemCreateRequest item,
			Principal principal)
	{
		ItemResponse resp = itemService.add(item, principal.getName());
		
		return ResponseEntity.created(URI.create("/api/items/" + resp.id()))
				.body(resp);
	}
	
}
