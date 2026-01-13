package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperDetailsRequest;
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
	public void updateShopper(Shopper shopper)
	{
	    Shopper existing = shopperRepository.findById(shopper.getId()).orElse(null);
	    existing.fullUpdate(shopper);
	}
	
	@Transactional
	public void updateShopperDetails(Long shopperId, ShopperDetailsRequest newShopperDetails)
	{
		Shopper existing = provideShopperById(shopperId);
		existing.setFirstName(newShopperDetails.getFirstName());
		existing.setLastName(newShopperDetails.getLastName());
	}
}
