package com.kulakyokedici.kulakliksitesi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;

public interface SellerRepository extends CrudRepository<Seller, Long>
{
	public Seller findSellerById(Long id);
	
	public Seller findByUsername(String username);
	
	public List<Seller> findAll();
}
