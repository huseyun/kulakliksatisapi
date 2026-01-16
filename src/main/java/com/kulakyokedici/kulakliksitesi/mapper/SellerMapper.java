package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.SellerResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.SellerUpdateRequest;

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
	
	public SellerResponse toResponse(Seller seller)
	{
		return new SellerResponse(
				seller.getUsername(),
				seller.getPassword(),
				seller.getCompanyName());
	}
}
