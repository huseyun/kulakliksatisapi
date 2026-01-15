package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.SellerUpdateRequest;

@Component
public class SellerMapper
{
	private PasswordEncoder passwordEncoder;
	
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
}
