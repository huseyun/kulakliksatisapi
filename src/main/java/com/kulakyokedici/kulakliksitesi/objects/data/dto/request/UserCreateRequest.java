package com.kulakyokedici.kulakliksitesi.objects.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
		@NotBlank(message = "kullanici adi bos olamaz.")
		String username,
		@NotBlank(message = "sifre bos olamaz.")
		String password,
		@NotBlank(message = "e-posta girilmelidir.")
		@Email(message = "wrong e-mail format")
		String email) 
{}
