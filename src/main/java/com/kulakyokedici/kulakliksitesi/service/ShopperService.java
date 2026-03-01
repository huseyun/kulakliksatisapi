package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.ShopperMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ShopperResponse;
import com.kulakyokedici.kulakliksitesi.objects.exception.ResourceNotFoundException;
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
	
	public Shopper provideShopperById(Long id)
	{
		return shopperRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("shopper", "id", id));
	}
	
	public List<ShopperResponse> getAll()
	{
		List<Shopper> shoppers = shopperRepository.findAll();
		
		return shoppers.stream()
				.map(seller -> shopperMapper.toResponse(seller))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public void update(Long id,
			ShopperUpdateRequest newShopper)
	{
	    Shopper existing = shopperRepository.findById(id)
	    		.orElseThrow(() -> new ResourceNotFoundException("shopper", "id", id));
	    
	    Shopper shopper = shopperMapper.toEntity(newShopper);
	    
	    existing.fullUpdate(shopper);
	}
	
	@Transactional
	public void updateDetails(Long shopperId, 
			ShopperDetailsUpdateRequest newShopperDetails)
	{
		Shopper existing = provideShopperById(shopperId);
		existing.setFirstName(newShopperDetails.firstName());
		existing.setLastName(newShopperDetails.lastName());
	}
}
