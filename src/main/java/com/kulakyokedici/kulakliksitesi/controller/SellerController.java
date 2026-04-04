package com.kulakyokedici.kulakliksitesi.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerResponse;
import com.kulakyokedici.kulakliksitesi.service.SellerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sellers")
@PreAuthorize("hasRole('SELLER')")
public class SellerController
{
	private final SellerService sellerService;
	
	public SellerController(
			SellerService sellerService)
	{
		this.sellerService = sellerService;
	}
	
	@PutMapping
	public ResponseEntity<Void> updateSellerDetails(
			@Valid @RequestBody SellerDetailsUpdateRequest req,
			Principal principal)
	{
		SellerResponse seller = sellerService.getByUsername(principal.getName());
		
		sellerService.updateDetails(seller.id(), req);
		
		return ResponseEntity.noContent().build();
	}
}
