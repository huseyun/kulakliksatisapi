package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;

public record UserTypeResponse(
		EUserType userType) 
{
	public UserTypeResponse(
			EUserType userType)
	{
		this.userType = userType;
	}
}
