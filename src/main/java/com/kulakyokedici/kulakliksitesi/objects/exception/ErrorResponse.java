package com.kulakyokedici.kulakliksitesi.objects.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
		int statusCode,
		String message,
		LocalDateTime timestamp)
{
	public ErrorResponse(
		int statusCode,
		String message,
		LocalDateTime timestamp)
	{
		this.statusCode = statusCode;
		this.message = message;
		this.timestamp = timestamp;
	}
}
