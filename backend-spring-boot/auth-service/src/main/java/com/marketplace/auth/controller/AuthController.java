package com.marketplace.auth.controller;

import com.marketplace.auth.dto.*;
import com.marketplace.auth.service.AuthService;
import com.marketplace.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        Long userId = userService.registerNewUser(userRequest);
        return ResponseEntity.status(201).body(new UserResponse(userId, "User registered successfully"));
    }

    @PostMapping("/validate/token")
    public ResponseEntity<TokenVerificationResponse> verifyToken(@RequestBody TokenVerificationRequest tokenRequest) {
        TokenVerificationResponse response = authService.verifyToken(tokenRequest.getToken());
        return ResponseEntity.ok(response);
    }

}
