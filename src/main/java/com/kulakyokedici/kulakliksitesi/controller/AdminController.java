package com.kulakyokedici.kulakliksitesi.controller;

import java.util.List;
import java.util.Set;

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

import com.kulakyokedici.kulakliksitesi.objects.data.User;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.UserCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.UserUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.AdminResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerDetailedResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ShopperResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.UserResponse;
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
		User user = null;
		if(requestedId != null)
			user = userService.provideUserById(requestedId);
		else if(!username.isEmpty() && username != null)
			user = userService.provideUserByUsername(username);
		else if(!email.isEmpty() && email != null)
			user = userService.provideUserByEmail(email);
		
		if(user != null)
			return ResponseEntity.ok(user);
		else
			return ResponseEntity.notFound().build(); // burda bir hata var
	}
	
	@GetMapping("/get/allusers")
	public ResponseEntity<List<UserResponse>> getAllUsers()
	{
		return ResponseEntity.ok(userService.provideAllUsers());
	}
	
	@GetMapping("/get/allshoppers")
	public ResponseEntity<List<ShopperResponse>> getAllShoppers()
	{
		return ResponseEntity.ok(shopperService.provideAllShoppers());
	}
	
	@GetMapping("/get/allsellers")
	public ResponseEntity<List<SellerDetailedResponse>> getAllSellers()
	{
		return ResponseEntity.ok(sellerService.getAllSellers());
	}
	
	@GetMapping("/get/alladmins")
	public ResponseEntity<List<AdminResponse>> getAllAdmins()
	{
		return ResponseEntity.ok(adminService.provideAllAdmins());
	}
	
	@GetMapping("/get/selleritems/{sellerId}")
	public ResponseEntity<Set<ItemSummaryResponse>> getItemsBySellerId(@PathVariable Long sellerId)
	{
		return ResponseEntity.ok(itemService.getItemsBySellerId(sellerId));
	}
	
	@PostMapping("/post/addadmin")
	public ResponseEntity<Void> addAdmin(@Valid @RequestBody UserCreateRequest newAdmin)
	{
		adminService.addAdmin(newAdmin);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/post/addseller")
	public ResponseEntity<Void> addSeller(@Valid @RequestBody SellerCreateRequest newSeller)
	{	
		sellerService.addSeller(newSeller);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/put/updateuser/{userId}")
	public ResponseEntity<Void> updateUser(@PathVariable Long userId,
			@Valid @RequestBody UserUpdateRequest newUser)
	{
		userService.updateUser(userId, newUser);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateseller/{sellerId}")
	public ResponseEntity<Void> updateSeller(@PathVariable Long sellerId,
			@Valid @RequestBody SellerUpdateRequest newSeller)
	{
		sellerService.updateSeller(sellerId, newSeller);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateshopper/{shopperId}")
	public ResponseEntity<Void> updateShopper(@PathVariable Long shopperId, 
			@Valid @RequestBody ShopperUpdateRequest newShopper)
	{
		shopperService.updateShopper(shopperId, newShopper);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateshopperdetails/{shopperId}")
	public ResponseEntity<Void> updateShopperDetails(@PathVariable Long shopperId, 
			@Valid @RequestBody ShopperDetailsUpdateRequest newShopperDetails)
	{
		shopperService.updateShopperDetails(shopperId, newShopperDetails);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateadmin/{adminId}")
	public ResponseEntity<Void> updateAdmin(@PathVariable Long adminId,
			@Valid @RequestBody UserUpdateRequest newAdmin)
	{
		adminService.updateAdmin(adminId, newAdmin);
		return ResponseEntity.noContent().build();
	}
}
