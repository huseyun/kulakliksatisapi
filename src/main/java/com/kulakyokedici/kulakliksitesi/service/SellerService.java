package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.repository.SellerRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypesRepository;

import jakarta.transaction.Transactional;

@Service
public class SellerService
{
	private SellerRepository sellerRepository;
    private final UserTypesRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public SellerService(SellerRepository sellerRepository,
			UserTypesRepository userTypesRepository,
			PasswordEncoder passwordEncoder)
	{
		this.sellerRepository = sellerRepository;
		this.userTypesRepository = userTypesRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public Seller getSellerDetails(Long id)
	{
		return sellerRepository.findSellerById(id);
	}
	
	public List<Seller> getAllSellers()
	{
		return sellerRepository.findAll();
	}
	
	@Transactional
	public void updateSeller(Seller seller)
	{
		Seller existing = sellerRepository.findById(seller.getId()).orElse(null);
	            existing.setUsername(seller.getUsername());
	            existing.setEmail(seller.getEmail());
	            existing.setPassword(seller.getPassword());
	            existing.setCompanyName(seller.getCompanyName());
	}
	
	@Transactional
	public void addSeller(Seller seller)
	{
		seller.resetId();
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));

        Set<UserType> managedUserTypes = seller.getUserTypes().stream()
                .map(ut -> userTypesRepository.findById(ut.getId()).orElse(null))
                .filter(ut -> ut != null)
                .collect(Collectors.toSet());
        
        seller.setUserTypes(managedUserTypes);
	}
}
