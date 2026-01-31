package com.kulakyokedici.kulakliksitesi.objects.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
	
	private final HttpStatus httpStatus;
	
	public BaseException(String message, HttpStatus httpStatus)
	{
		this.httpStatus = httpStatus;
	}
}
