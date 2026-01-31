package com.kulakyokedici.kulakliksitesi.objects.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super((resourceName
				+ ", " 
				+ fieldName 
				+ ":" 
				+ fieldValue 
				+ " değeri ile bulunamadı."), HttpStatus.NOT_FOUND);
	}

}
