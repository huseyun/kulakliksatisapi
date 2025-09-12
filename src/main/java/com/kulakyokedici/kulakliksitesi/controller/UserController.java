package com.kulakyokedici.kulakliksitesi.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.service.ShopperService;
import com.kulakyokedici.kulakliksitesi.service.UserService;

@RestController
@RequestMapping("api/profile")
public class UserController
{
	private ShopperService shopperService;
	private UserService userService;

	@Autowired
	public UserController(ShopperService shopperService, UserService userService)
	{
		this.shopperService = shopperService;
		this.userService = userService;
	}
	
	@GetMapping
	private ResponseEntity<? extends User> getUserDetails(Principal principal)
	{
		return ResponseEntity.ok(userService.provideUserByUsername(principal.getName()));
	}
}
