package com.kulakyokedici.kulakliksitesi.config.s3;

public class Bucket
{
    private String productPhotos;
    private String userAvatars;
    
    public String getProductPhotos()
	{
		return productPhotos;
	}
	public void setProductPhotos(String productPhotos)
	{
		this.productPhotos = productPhotos;
	}
}
