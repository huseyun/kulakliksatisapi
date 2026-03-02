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
	private ShopperMapper shopperMapper;
	
	@Autowired
	public ShopperService(ShopperRepository shopperRepository, ShopperMapper shopperMapper)
	{
		this.shopperRepository = shopperRepository;
		this.shopperMapper = shopperMapper; 
	}
	
	public ShopperResponse getById(Long id)
	{
		Shopper shopper = shopperRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("shopper", "id", id));
		
		return shopperMapper.toResponse(shopper);
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
			ShopperUpdateRequest req)
	{
	    Shopper existing = shopperRepository.findById(id)
	    		.orElseThrow(() -> new ResourceNotFoundException("shopper", "id", id));
	    
	    shopperMapper.updateEntity(existing, req);
	}
	
	@Transactional
	public void updateDetails(Long id, 
			ShopperDetailsUpdateRequest newShopperDetails)
	{
		Shopper existing = shopperRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("shopper", "id", id));
		
		shopperMapper.updateEntity(existing, newShopperDetails);
	}
}
