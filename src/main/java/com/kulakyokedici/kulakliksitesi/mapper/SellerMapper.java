package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerDetailedResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerResponse;

@Component
public class SellerMapper
{
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public SellerMapper(PasswordEncoder passwordEncoder)
	{
		this.passwordEncoder = passwordEncoder;
	}
	
	public Seller toEntity(SellerUpdateRequest newSeller)
	{
		Seller seller = new Seller();
		seller.setUsername(newSeller.username());
		seller.setPassword(passwordEncoder.encode(newSeller.password()));
		seller.setCompanyName(newSeller.companyName());
		seller.setEmail(newSeller.email());
		
		return seller;
	}
	
	public Seller toEntity(SellerCreateRequest newSeller)
	{
		Seller seller = new Seller();
		seller.setUsername(newSeller.username());
		seller.setPassword(passwordEncoder.encode(newSeller.password()));
		seller.setCompanyName(newSeller.companyName());
		seller.setEmail(newSeller.email());
		
		return seller;
	}
	
	public SellerDetailedResponse toDetailedResponse(Seller seller)
	{
		return new SellerDetailedResponse(
				seller.getUsername(),
				seller.getEmail(),
				seller.getCompanyName(),
				seller.getItems().stream()
					.map(item -> new ItemSummaryResponse(
							item.getItemName(),
							item.getItemPrice(),
							item.getImages()))
					.collect(java.util.stream.Collectors.toSet()));
	}
	
	public SellerResponse toResponse(Seller seller)
	{
		return new SellerResponse(
				seller.getUsername(),
				seller.getEmail(),
				seller.getCompanyName());
	}
}
