package com.kulakyokedici.kulakliksitesi.repository;

import java.util.SortedSet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kulakyokedici.kulakliksitesi.objects.data.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
    
    SortedSet<Item> findBySellerId(Long sellerId);
    
    Item findByIdAndSellerId(Long itemId, Long sellerId);
    
    @Query("SELECT i FROM Item i WHERE i.seller.username = :username")
    SortedSet<Item> findBySellerUsername(@Param("username") String username);
    
    public boolean existsByItemName(String itemName);
}