package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.AdminResponse;

@Component
public class AdminMapper {
	
	public AdminResponse toResponse(Admin admin)
	{
		return new AdminResponse(
				admin.getUsername(),
				admin.getEmail());
	}
}
