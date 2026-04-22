package com.kulakyokedici.kulakliksitesi.config.s3;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String endpoint;


	private String accessKey;
    private String secretKey;

    private Bucket bucket = new Bucket();

    // Tüm bucket isimlerini listelemek için yardımcı metot
    public Map<String, String> getAllBuckets() {
        Map<String, String> buckets = new HashMap<>();
        buckets.put("product-photos", bucket.getProductPhotos());
        return buckets;
    }
    
    public String getEndpoint()
	{
		return endpoint;
	}

	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

	public String getAccessKey()
	{
		return accessKey;
	}

	public void setAccessKey(String accessKey)
	{
		this.accessKey = accessKey;
	}

	public String getSecretKey()
	{
		return secretKey;
	}

	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}
}