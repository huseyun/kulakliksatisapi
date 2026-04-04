package com.kulakyokedici.kulakliksitesi.controller;

import java.net.URI;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ShopperResponse;
import com.kulakyokedici.kulakliksitesi.service.ShopperService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/shoppers")
public class ShopperController
{
	private final ShopperService shopperService;
	
	public ShopperController(
			ShopperService shopperService)
	{
		this.shopperService = shopperService;
	}
	
	/*
	 * GET istekleri
	 */
	
	@PreAuthorize("hasRole('SHOPPER')")
	@GetMapping("/profile")
	public ResponseEntity<ShopperResponse> getCurrentUser(Principal principal)
	{
		return ResponseEntity.ok(shopperService.getByUsername(principal.getName()));
	}
	
	/*
	 * POST istekleri
	 */
	
	@PostMapping()
	public ResponseEntity<ShopperResponse> createUser(@Valid @RequestBody ShopperCreateRequest req)
	{
		ShopperResponse resp = shopperService.add(req);
		
		return ResponseEntity.created(URI.create("/api/shoppers/" + resp.id()))
				.body(resp);
	}
	
	/*
	 * PUT istekleri
	 */
	
	@PreAuthorize("hasRole('SHOPPER')")
	@PutMapping("/profile")
	public ResponseEntity<Void> updateShopperDetails(
			@Valid @RequestBody ShopperDetailsUpdateRequest req,
			Principal principal)
	{
		ShopperResponse shopper = shopperService.getByUsername(principal.getName());
		
		shopperService.updateDetails(shopper.id(), req);
		
		return ResponseEntity.noContent().build();
	}
	
	/*
	 * Yardımcı metotlar
	 */
}
