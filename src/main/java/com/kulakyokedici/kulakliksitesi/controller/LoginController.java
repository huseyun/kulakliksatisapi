package com.kulakyokedici.kulakliksitesi.controller;

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

import com.kulakyokedici.kulakliksitesi.objects.security.dto.AuthRequestDto;
import com.kulakyokedici.kulakliksitesi.service.security.JwtService;

@RestController
@RequestMapping("api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
}