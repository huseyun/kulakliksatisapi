package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperUpdateRequest;
import com.kulakyokedici.kulakliksitesi.repository.ShopperRepository;

import jakarta.transaction.Transactional;

@Service
public class ShopperService
{
	private ShopperRepository shopperRepository;
	
	@Autowired
	public ShopperService(ShopperRepository shopperRepository)
	{
		this.shopperRepository = shopperRepository;
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
	    
	    Shopper shopper = new Shopper();
	    shopper.setUsername(newShopper.username());
	    shopper.setEmail(newShopper.email());
	    shopper.setFirstName(newShopper.firstName());
	    shopper.setLastName(newShopper.lastName());
	    shopper.setPassword(newShopper.password());
	    
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
