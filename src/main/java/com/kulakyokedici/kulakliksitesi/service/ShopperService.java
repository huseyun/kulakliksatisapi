package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperDetailsDto;
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
	
	public Shopper provideShopperById(Long id)
	{
		return shopperRepository.findShopperById(id);
	}
	
	public List<Shopper> provideAllShoppers()
	{
		return shopperRepository.findAll();
	}
	
	@Transactional
	public void updateShopper(Shopper shopper)
	{
	    Shopper existing = shopperRepository.findById(shopper.getId()).orElse(null);
	    if (existing != null) {
	    	shopper.setPassword(existing.getPassword());
	    }
	}
	
	@Transactional
	public void updateShopperDetails(ShopperDetailsDto newShopperDetails)
	{
		Shopper existing = shopperRepository.findShopperById(newShopperDetails.getId());
		existing.setFirstName(newShopperDetails.getFirstName());
		existing.setLastName(newShopperDetails.getLastName());
	}
}
