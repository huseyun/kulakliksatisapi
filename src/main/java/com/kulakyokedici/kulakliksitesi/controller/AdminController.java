package com.kulakyokedici.kulakliksitesi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.service.ItemService;
import com.kulakyokedici.kulakliksitesi.service.SellerService;
import com.kulakyokedici.kulakliksitesi.service.ShopperService;
import com.kulakyokedici.kulakliksitesi.service.UserService;

@RestController
@RequestMapping("api/admin")
public class AdminController
{
	private UserService userService;
	private ShopperService shopperService;
	private SellerService sellerService;
	private ItemService itemService;
	
	@Autowired
	public AdminController(UserService userService, ShopperService shopperService, SellerService sellerService, ItemService itemService)
	{
		this.userService = userService;
		this.shopperService = shopperService;
		this.sellerService = sellerService;
		this.itemService = itemService;
	}
	
	@GetMapping("/get/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable long requestedId)
	{
		User user = userService.provideUserById(requestedId);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/get/allusers")
	public ResponseEntity<List<User>> getAllUsers()
	{
		return ResponseEntity.ok(userService.provideAllUsers());
	}
	
	@GetMapping("/get/allshoppers")
	public ResponseEntity<List<Shopper>> getAllShoppers()
	{
		return ResponseEntity.ok(shopperService.getAllShoppers());
	}
	
	@GetMapping("/get/allsellers")
	public ResponseEntity<List<Seller>> getAllSellers()
	{
		return ResponseEntity.ok(sellerService.getAllSellers());
	}
	
	@PostMapping("/post/adduser")
	public ResponseEntity<Void> addUser(@RequestBody User user)
	{
		userService.addUser(user);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/put/updateuser")
	public ResponseEntity<Void> updateUser(@RequestBody User newUser)
	{
		userService.updateUser(newUser);
		return ResponseEntity.noContent().build();
	}
}
