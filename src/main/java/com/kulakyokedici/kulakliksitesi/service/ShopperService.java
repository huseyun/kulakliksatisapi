package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		shopperRepository.save(shopper);
	}
}
