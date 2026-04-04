package com.kulakyokedici.kulakliksitesi.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulakyokedici.kulakliksitesi.objects.data.dto.request.ShopperCreateRequest;
import com.kulakyokedici.kulakliksitesi.objects.data.dto.response.ShopperResponse;
import com.kulakyokedici.kulakliksitesi.objects.security.dto.AuthRequestDto;
import com.kulakyokedici.kulakliksitesi.service.ShopperService;
import com.kulakyokedici.kulakliksitesi.service.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ShopperService shopperService;
    
    @Autowired
    public AuthController(
    		AuthenticationManager authenticationManager, 
    		JwtService jwtService,
    		ShopperService shopperService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.shopperService = shopperService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDto request) {
        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authenticatedPrincipal = authenticationManager.authenticate(authenticationRequest);
        
        UserDetails userDetails = (UserDetails) authenticatedPrincipal.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        
        String jsonResponse = "{\"token\": \"" + token + "\"}";
        
        return ResponseEntity.ok(jsonResponse);
    }
    
	@PostMapping("/register")
	public ResponseEntity<ShopperResponse> createShopper(@Valid @RequestBody ShopperCreateRequest req)
	{
		ShopperResponse resp = shopperService.add(req);
		
		return ResponseEntity.created(URI.create("/api/shoppers/" + resp.id()))
				.body(resp);
	}
}