package com.kulakyokedici.kulakliksitesi.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ShopperResponse;
import com.kulakyokedici.kulakliksitesi.service.ShopperService;

@RestController
@RequestMapping("api")
public class ShopperController
{
	private final ShopperService shopperService;
	
	public ShopperController(
			ShopperService shopperService)
	{
		this.shopperService = shopperService;
	}
	
	@GetMapping("/profile")
	public ResponseEntity<ShopperResponse> getCurrentUser(Principal principal)
	{
		return ResponseEntity.ok(shopperService.getByUsername(principal.getName()));
	}
}
