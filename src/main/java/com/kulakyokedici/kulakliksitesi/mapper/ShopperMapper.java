package com.kulakyokedici.kulakliksitesi.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperUpdateRequest;

@Component
public class ShopperMapper
{
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public ShopperMapper(PasswordEncoder passwordEncoder)
	{
		this.passwordEncoder = passwordEncoder;
	}
	
	public Shopper toEntity(ShopperUpdateRequest newShopper)
	{
		Shopper shopper = new Shopper();
		
		shopper.setUsername(newShopper.username());
		shopper.setPassword(newShopper.password());
		shopper.setEmail(newShopper.email());
		shopper.setFirstName(newShopper.firstName());
		shopper.setLastName(newShopper.lastName());
		
		return shopper;
	}
}
