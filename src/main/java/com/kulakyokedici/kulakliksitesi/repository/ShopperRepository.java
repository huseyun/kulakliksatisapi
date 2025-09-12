package com.kulakyokedici.kulakliksitesi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;

public interface ShopperRepository extends CrudRepository<Shopper, Long>
{
	public Shopper findShopperById(Long id);
	
	public Shopper findByUsername(String username);
	
	public List<Shopper> findAll();
}
