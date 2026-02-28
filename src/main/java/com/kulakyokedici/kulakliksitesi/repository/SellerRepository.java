package com.kulakyokedici.kulakliksitesi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;

public interface SellerRepository extends CrudRepository<Seller, Long>
{
	public Seller findSellerById(Long id);
	
	public Seller findByUsername(String username);
	
	// eager loading, proxy yerine tek sorguda getir.
	@EntityGraph(attributePaths = {"items", "items.images"})
	public List<Seller> findAll();
}
