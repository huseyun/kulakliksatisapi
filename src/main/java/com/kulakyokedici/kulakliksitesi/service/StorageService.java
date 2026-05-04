package com.kulakyokedici.kulakliksitesi.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kulakyokedici.kulakliksitesi.config.s3.StorageProperties;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class StorageService 
{
	private final S3Client s3Client;
	
	public StorageService(
			S3Client s3Client)
	{
		this.s3Client = s3Client;
	}
	
	public String uploadFile(
			InputStream inputStream, 
			long contentLength, 
			String contentType, 
			String bucketName, 
			String objectKey)
	{
		try
		{
			PutObjectRequest req = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(objectKey)
					.contentType(contentType)
					.build();
			
			s3Client.putObject(
					req, 
					RequestBody.fromInputStream(inputStream, contentLength));
			
			return objectKey;
		} catch(Exception e)
		{
			throw new RuntimeException("Dosya depolama servisine yüklenirken hata oluştu: " + e.getMessage(), e);
		}
		
	}
	
	private String getExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg";
    }
}
