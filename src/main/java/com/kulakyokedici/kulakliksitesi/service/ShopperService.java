package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.repository.ShopperRepository;

@Service
public class ShopperService
{
	private ShopperRepository shopperRepository;
	
	@Autowired
	public ShopperService(ShopperRepository shopperRepository)
	{
		this.shopperRepository = shopperRepository;
	}
	
	public Shopper getShopperDetails(Long id)
	{
		return shopperRepository.findShopperById(id);
	}
	
	public List<Shopper> getAllShoppers()
	{
		return shopperRepository.findAll();
	}
	
	public void updateShopper(Shopper shopper)
	{
	    Shopper existing = shopperRepository.findById(shopper.getId()).orElse(null);
	    if (existing != null) {
	        if (shopper.getPassword() == null) {
	            shopper.setPassword(existing.getPassword());
	        }
	        
	        shopperRepository.save(shopper);
	    }
	}
}
