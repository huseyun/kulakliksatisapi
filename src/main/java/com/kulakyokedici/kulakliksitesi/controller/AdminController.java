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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.Admin;
import com.kulakyokedici.kulakliksitesi.objects.data.Item;
import com.kulakyokedici.kulakliksitesi.objects.data.Seller;
import com.kulakyokedici.kulakliksitesi.objects.data.Shopper;
import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.UserInfoDto;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.ShopperDetailsDto;
import com.kulakyokedici.kulakliksitesi.service.AdminService;
import com.kulakyokedici.kulakliksitesi.service.ItemService;
import com.kulakyokedici.kulakliksitesi.service.SellerService;
import com.kulakyokedici.kulakliksitesi.service.ShopperService;
import com.kulakyokedici.kulakliksitesi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admin")
public class AdminController
{
	private UserService userService;
	private ShopperService shopperService;
	private SellerService sellerService;
	private ItemService itemService;
	private AdminService adminService;
	
	@Autowired
	public AdminController(UserService userService,
			ShopperService shopperService,
			SellerService sellerService,
			ItemService itemService,
			AdminService adminService)
	{
		this.userService = userService;
		this.shopperService = shopperService;
		this.sellerService = sellerService;
		this.itemService = itemService;
		this.adminService = adminService;
	}
	
	@GetMapping("/get/user")
	public ResponseEntity<User> getUser(@RequestParam(name = "id", required = false) Long requestedId,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email)
	{
		User user;
		if(requestedId != 0)
			user = userService.provideUserById(0);
		else if(username != "")
			user = userService.provideUserByUsername(username);
		else if(email != "")
			user = userService.provideUserByEmail(email);
		else
			user = null;
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
		return ResponseEntity.ok(shopperService.provideAllShoppers());
	}
	
	@GetMapping("/get/allsellers")
	public ResponseEntity<List<Seller>> getAllSellers()
	{
		return ResponseEntity.ok(sellerService.getAllSellers());
	}
	
	@GetMapping("/get/alladmins")
	public ResponseEntity<List<Admin>> getAllAdmins()
	{
		return ResponseEntity.ok(adminService.provideAllAdmins());
	}
	
	@GetMapping("/get/selleritems/{sellerId}")
	public ResponseEntity<List<Item>> getItemsBySellerId(@PathVariable Long sellerId)
	{
		return ResponseEntity.ok(itemService.getItemsBySellerId(sellerId));
	}
	
	@PostMapping("/post/addadmin")
	public ResponseEntity<Void> addAdmin(@Valid @RequestBody UserInfoDto newAdmin)
	{
		adminService.addAdmin(newAdmin);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/post/addseller")
	public ResponseEntity<Void> addSeller(@Valid @RequestBody Seller seller)
	{
		sellerService.addSeller(seller);
		return ResponseEntity.ok().build();
	}
	
//	@PutMapping("/put/updateuser")
//	public ResponseEntity<Void> updateUser(@Valid @RequestBody User newUser)
//	{
//		userService.updateUser(newUser);
//		return ResponseEntity.noContent().build();
//	}
	
	@PutMapping("/put/updateseller")
	public ResponseEntity<Void> updateSeller(@Valid @RequestBody Seller newSeller)
	{
		sellerService.updateSeller(newSeller);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateshopper")
	public ResponseEntity<Void> updateShopper(@Valid @RequestBody Shopper newShopper)
	{
		shopperService.updateShopper(newShopper);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateshopperdetails")
	public ResponseEntity<Void> updateShopperDetails(@Valid @RequestBody ShopperDetailsDto newShopperDetails)
	{
		shopperService.updateShopperDetails(newShopperDetails);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateadmin")
	public ResponseEntity<Void> updateAdmin(@Valid @RequestBody Admin newAdmin)
	{
		adminService.updateAdmin(newAdmin);
		return ResponseEntity.noContent().build();
	}
}
