package com.kulakyokedici.kulakliksitesi.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kulakyokedici.kulakliksitesi.config.s3.StorageProperties;
import com.kulakyokedici.kulakliksitesi.mapper.ItemMapper;
import com.kulakyokedici.kulakliksitesi.mapper.SellerMapper;
import com.kulakyokedici.kulakliksitesi.objects.data.Image;
import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ItemCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ItemUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerResponse;
import com.kulakyokedici.kulakliksitesi.objects.exception.EErrorCode;
import com.kulakyokedici.kulakliksitesi.objects.exception.ResourceNotFoundException;
import com.kulakyokedici.kulakliksitesi.repository.ItemRepository;
import com.kulakyokedici.kulakliksitesi.repository.SellerRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class ItemService
{
	private final ItemRepository itemRepository;
	private final SellerRepository sellerRepository;
	private final ItemMapper itemMapper;
	private final SellerMapper sellerMapper;
	private final EntityManager entityManager;
	private final StorageProperties storageProperties;
	private final StorageService storageService;
	
	
	public ItemService(
			ItemRepository itemRepository,
			SellerRepository sellerRepository,
			SellerMapper sellerMapper,
			ItemMapper itemMapper,
			EntityManager entityManager,
			StorageProperties storageProperties,
			StorageService storageService)
	{
		this.itemRepository = itemRepository;
		this.itemMapper = itemMapper;
		this.sellerMapper = sellerMapper;
		this.entityManager = entityManager;
		this.sellerRepository = sellerRepository;
		this.storageProperties = storageProperties;
		this.storageService = storageService;
	}
	
	public ItemResponse getById(Long id)
	{
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("item", "id", id, EErrorCode.ITEM_NOT_FOUND));
		
		return itemMapper.toResponse(item);
	}
	
	public SellerResponse getSellerById(Long id)
	{
	    Item item = itemRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("item", "id", id, EErrorCode.SELLER_NOT_FOUND));
		
		return sellerMapper.toResponse(item.getSeller());
	}
	
	@Transactional
	public void update(
			Long id,
			ItemUpdateRequest req)
	{
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("item", "id", id, EErrorCode.ITEM_NOT_FOUND));
		
		itemMapper.updateEntity(item, req);
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
	
	@Transactional
	public ItemResponse add(ItemCreateRequest req, String username)
	{
		Seller seller = sellerRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("seller", "username", username, EErrorCode.ITEM_NOT_FOUND));
		
		Item item = itemMapper.toEntity(req, seller);
		
		seller.getItems().add(item);
		
		return itemMapper.toResponse(item);
	}
	
	@Transactional
	public void addImage(MultipartFile file, Long id)
	{
		Item item = itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("item", "id", id, EErrorCode.ITEM_NOT_FOUND));
		
		String targetBucket = storageProperties.getAllBuckets().get("product-images");
		String uniqueId = UUID.randomUUID().toString();
		String extension = getExtension(file.getOriginalFilename());
		
		
		String productName = item.getItemUuid();
		String baseFolderPath = productName + "/";
		
		String originalKey = baseFolderPath + "original-" + uniqueId + extension;
		String thumbnailKey = baseFolderPath + "thumbnail-" + uniqueId + extension;
		String standardKey = baseFolderPath + "standard-" + uniqueId + extension;
		
		ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream();
		ByteArrayOutputStream standardOutput = new ByteArrayOutputStream();
		
		// BURAYA BAKILACAK
		try {
			Thumbnails.of(file.getInputStream())
					.size(800, 800)
				    .outputFormat("jpg") // Formatı sabitlediğimiz için aşağıda image/jpeg diyeceğiz
				   	.toOutputStream(thumbnailOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        byte[] thumbnailBytes = thumbnailOutput.toByteArray();
        InputStream thumbnailStream = new ByteArrayInputStream(thumbnailBytes);
        long thumbnailSize = thumbnailBytes.length;
        
		try {
			Thumbnails.of(file.getInputStream())
					.size(1500, 1500)
				    .outputFormat("jpg") // Formatı sabitlediğimiz için aşağıda image/jpeg diyeceğiz
				   	.toOutputStream(standardOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] standardBytes = standardOutput.toByteArray();
		InputStream standardStream = new ByteArrayInputStream(standardBytes);
		long standardSize = standardBytes.length;
        
        // BURALARA DA BAKILACAK
		try {
			storageService.uploadFile(
					file.getInputStream(), 
					file.getSize(), 
				    file.getContentType(), 
				    targetBucket, 
				    originalKey
				);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		storageService.uploadFile(
                thumbnailStream, 
                thumbnailSize, 
                "image/jpeg", // Thumbnailator jpg ürettiği için tip sabit
                targetBucket, 
                thumbnailKey
        );
		
		storageService.uploadFile(
                standardStream, 
                standardSize, 
                "image/jpeg", // Thumbnailator jpg ürettiği için tip sabit
                targetBucket, 
                standardKey
        );
		
		Image image = new Image();
		image.setOriginalKey(originalKey);
		image.setThumbnailKey(thumbnailKey);
		image.setStandartKey(standardKey);
		
		item.getImages().add(image);
	}
	
	public void delete(Long id)
	{
		itemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("seller", "id", id, EErrorCode.ITEM_NOT_FOUND));
		
		itemRepository.deleteById(id);
		
	}
	
	private String getExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg"; // Varsayılan
    }
}
