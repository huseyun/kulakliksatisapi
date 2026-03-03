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

import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.SellerUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperDetailsUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.UserCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.UserUpdateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.AdminResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ItemSummaryResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerDetailedResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.SellerResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ShopperResponse;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.UserResponse;
import com.kulakyokedici.kulakliksitesi.objects.exception.InsufficientParametersException;
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
	
	/*
	 * GET istekleri
	 */
	
	@GetMapping("/get/user")
	public ResponseEntity<UserResponse> getUser(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email)
	{
		UserResponse userResponse = null;
		
		if(id != null)
			userResponse = userService.getById(id);
		else if(username != null && !username.isBlank())
			userResponse = userService.getByUsername(username);
		else if(email != null && !email.isBlank())
			userResponse = userService.getByEmail(email);
		else
			throw new InsufficientParametersException("user");
		
		return ResponseEntity.ok(userResponse);
	}
	
	@GetMapping("/get/admin")
	public ResponseEntity<AdminResponse> getAdmin(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email)
	{
		AdminResponse adminResponse = null;
		
		if(id != null)
			adminResponse = adminService.getById(id);
		else if(username != null && !username.isBlank())
			adminResponse = adminService.getByUsername(username);
		else if(email != null && !email.isBlank())
			adminResponse = adminService.getByEmail(email);
		else
			throw new InsufficientParametersException("admin");
		
		return ResponseEntity.ok(adminResponse);
	}
	
	@GetMapping("/get/seller")
	public ResponseEntity<SellerResponse> getSeller(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "company_name", required = false) String companyName)
	{
		SellerResponse sellerResponse = null;
		
		if(id != null)
			sellerResponse = sellerService.getById(id);
		else if(username != null && !username.isBlank())
			sellerResponse = sellerService.getByUsername(username);
		else if(email != null && !email.isBlank())
			sellerResponse = sellerService.getByEmail(email);
		else if(companyName != null && !companyName.isBlank())
			sellerResponse = sellerService.getByCompanyName(companyName);
		else
			throw new InsufficientParametersException("seller");
		
		return ResponseEntity.ok(sellerResponse);
	}
	
	@GetMapping("/get/allusers")
	public ResponseEntity<List<UserResponse>> getAllUsers()
	{
		return ResponseEntity.ok(userService.getAll());
	}
	
	@GetMapping("/get/allshoppers")
	public ResponseEntity<List<ShopperResponse>> getAllShoppers()
	{
		return ResponseEntity.ok(shopperService.getAll());
	}
	
	@GetMapping("/get/allsellers")
	public ResponseEntity<List<SellerDetailedResponse>> getAllSellers()
	{
		return ResponseEntity.ok(sellerService.getAll());
	}
	
	@GetMapping("/get/alladmins")
	public ResponseEntity<List<AdminResponse>> getAllAdmins()
	{
		return ResponseEntity.ok(adminService.getAll());
	}
	
	@GetMapping("/get/selleritems/{sellerId}")
	public ResponseEntity<Set<ItemSummaryResponse>> getItemsBySellerId(@PathVariable Long sellerId)
	{
		return ResponseEntity.ok(itemService.getAllBySellerId(sellerId));
	}
	
	/*
	 * POST istekleri
	 * yeni kaynak eklemek için.
	 */
	
	@PostMapping("/post/addadmin")
	public ResponseEntity<Void> addAdmin(@Valid @RequestBody UserCreateRequest newAdmin)
	{
		adminService.add(newAdmin);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/post/addseller")
	public ResponseEntity<Void> addSeller(@Valid @RequestBody SellerCreateRequest req)
	{	
		sellerService.add(req);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/post/addshopper")
	public ResponseEntity<Void> addShopper(@Valid @RequestBody ShopperCreateRequest req)
	{
		shopperService.add(req);
		return ResponseEntity.ok().build();
	}
	
	/*
	 * PUT istekleri
	 * kaynak güncellemek için.
	 */
	
	@PutMapping("/put/updateuser/{userId}")
	public ResponseEntity<Void> updateUser(@PathVariable Long userId,
			@Valid @RequestBody UserUpdateRequest newUser)
	{
		userService.update(userId, newUser);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateseller/{sellerId}")
	public ResponseEntity<Void> updateSeller(@PathVariable Long sellerId,
			@Valid @RequestBody SellerUpdateRequest newSeller)
	{
		sellerService.update(sellerId, newSeller);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateshopper/{shopperId}")
	public ResponseEntity<Void> updateShopper(@PathVariable Long shopperId, 
			@Valid @RequestBody ShopperUpdateRequest newShopper)
	{
		shopperService.update(shopperId, newShopper);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateshopperdetails/{shopperId}")
	public ResponseEntity<Void> updateShopperDetails(@PathVariable Long shopperId, 
			@Valid @RequestBody ShopperDetailsUpdateRequest newShopperDetails)
	{
		shopperService.updateDetails(shopperId, newShopperDetails);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/put/updateadmin/{adminId}")
	public ResponseEntity<Void> updateAdmin(@PathVariable Long adminId,
			@Valid @RequestBody UserUpdateRequest newAdmin)
	{
		adminService.update(adminId, newAdmin);
		return ResponseEntity.noContent().build();
	}
}