package com.kulakyokedici.kulakliksitesi.objects.data.dto;

public class UserInfoRequest {
	
	private String username;
	private String password;
	private String email;
	
	public String getPassword()
    {
    	return password;
    }
    
    public String getUsername()
    {
    	return username;
    }
    
    public String getEmail()
    {
    	return email;
    }
}
