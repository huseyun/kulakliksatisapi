package com.kulakyokedici.kulakliksitesi.config.s3;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
public class StorageProperties {

    private final String endpoint;
    private final String publicEndPoint;
    
	private final String accessKey;
    private final String secretKey;
    
    private final String bucketProductImages;
    
    public StorageProperties(
    		String endpoint,
    		String accessKey,
    		String secretKey,
    		String publicEndPoint,
    		String bucketProductImages) 
    {
		this.endpoint = endpoint;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.publicEndPoint = publicEndPoint;
		this.bucketProductImages = bucketProductImages;
	}
    
	// Tüm bucket isimlerini listelemek için yardımcı metot
    public Map<String, String> getAllBuckets() {
        Map<String, String> buckets = new HashMap<>();
        buckets.put("product-images", bucketProductImages);
        return buckets;
    }
}