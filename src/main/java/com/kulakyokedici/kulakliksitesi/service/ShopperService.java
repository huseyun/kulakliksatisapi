package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.ShopperMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperUpdateRequest;
import com.kulakyokedici.kulakliksitesi.repository.ShopperRepository;

import jakarta.transaction.Transactional;

@Service
public class ShopperService
{
	private ShopperRepository shopperRepository;
	private PasswordEncoder passwordEncoder;
	private ShopperMapper shopperMapper;
	
	@Autowired
	public ShopperService(ShopperRepository shopperRepository, PasswordEncoder passwordEncoder, ShopperMapper shopperMapper)
	{
		this.shopperRepository = shopperRepository;
		this.passwordEncoder = passwordEncoder;
		this.shopperMapper = shopperMapper; 
	}
	
	public Shopper provideShopperById(Long shopperId)
	{
		return shopperRepository.findShopperById(shopperId);
	}
	
	public List<Shopper> provideAllShoppers()
	{
		return shopperRepository.findAll();
	}
	
	@Transactional
	public void updateShopper(Long id,
			ShopperUpdateRequest newShopper)
	{
	    Shopper existing = shopperRepository.findById(id).orElse(null);
	    
	    Shopper shopper = shopperMapper.toEntity(newShopper);
	    
	    existing.fullUpdate(shopper);
	}
	
	@Transactional
	public void updateShopperDetails(Long shopperId, 
			ShopperDetailsUpdateRequest newShopperDetails)
	{
		Shopper existing = provideShopperById(shopperId);
		existing.setFirstName(newShopperDetails.firstName());
		existing.setLastName(newShopperDetails.lastName());
	}
}
