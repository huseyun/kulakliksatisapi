package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.SellerMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.SellerUpdateRequest;
import com.kulakyokedici.kulakliksitesi.repository.SellerRepository;
import com.kulakyokedici.kulakliksitesi.repository.UserTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class SellerService
{
	private SellerRepository sellerRepository;
    private final UserTypeRepository userTypesRepository;
	private final PasswordEncoder passwordEncoder;
	private SellerMapper sellerMapper;
	
	@Autowired
	public SellerService(SellerRepository sellerRepository,
			UserTypeRepository userTypesRepository,
			PasswordEncoder passwordEncoder,
			SellerMapper sellerMapper)
	{
		this.sellerRepository = sellerRepository;
		this.userTypesRepository = userTypesRepository;
		this.passwordEncoder = passwordEncoder;
		this.sellerMapper = sellerMapper;
		
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
	public void updateSeller(Long id, SellerUpdateRequest newSeller)
	{
		Seller existing = sellerRepository.findById(id).orElse(null);

		Seller seller = sellerMapper.toEntity(newSeller);
		
		existing.fullUpdate(seller);
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
        
        sellerRepository.save(seller);
	}
}
