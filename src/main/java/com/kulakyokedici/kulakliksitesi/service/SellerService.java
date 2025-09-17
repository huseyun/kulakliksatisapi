package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.repository.SellerRepository;

@Service
public class SellerService
{
	private SellerRepository sellerRepository;
	
	@Autowired
	public SellerService(SellerRepository sellerRepository)
	{
		this.sellerRepository = sellerRepository;
	}
	
	public Seller getSellerDetails(Long id)
	{
		return sellerRepository.findSellerById(id);
	}
	
	public List<Seller> getAllSellers()
	{
		return sellerRepository.findAll();
	}
	
	public void updateSeller(Seller seller)
	{
		sellerRepository.save(seller);
	}
}
