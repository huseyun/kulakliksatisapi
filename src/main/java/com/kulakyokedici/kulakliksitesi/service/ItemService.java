package com.kulakyokedici.kulakliksitesi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import com.kulakyokedici.kulakliksitesi.mapper.ItemMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ItemService
{
	private final ItemRepository itemRepository;
	private final ItemMapper itemMapper;
	private final EntityManager entityManager;
	
	public ItemService(
			ItemRepository itemRepository,
			ItemMapper itemMapper,
			EntityManager entityManager)
	{
		this.itemRepository = itemRepository;
		this.itemMapper = itemMapper;
		this.entityManager = entityManager;
	}
	
	public List<ItemSummaryResponse> getSummaryAll()
	{
		List<Item> items = itemRepository.findAll();
		
		List<ItemSummaryResponse> responseItems = items.stream()
				.map(i -> itemMapper.toSummaryResponse(i))
				.collect(Collectors.toList());
		
		return responseItems;
	}
	
	public Set<ItemSummaryResponse> getSummaryAllBySellerId(Long sellerId)
	{
		Set<Item> items = itemRepository.findBySellerId(sellerId);
		
		Set<ItemSummaryResponse> responseItems = items.stream()
				.map(i -> itemMapper.toSummaryResponse(i))
				.collect(Collectors.toSet());
		return responseItems;
	}
	
	@Transactional
    public List<ItemSummaryResponse> search(String keyword) {
        // hibernate search oturumunu başlat 
        SearchSession searchSession = Search.session(entityManager);

        List<Item> hits = searchSession.search(Item.class)
            .where(f -> f.match()
                .fields("title", "description", "brand")
                .matching(keyword)
                .fuzzy(2)) // 2 harfe kadar yazım yanlışlarını tolere et
            .fetchHits(20); // En alakalı ilk 20 sonucu getir
        
        List<ItemSummaryResponse> response = hits.stream()
        		.map(i -> itemMapper.toSummaryResponse(i))
        		.collect(Collectors.toList());
        
        return response;
	}
}
