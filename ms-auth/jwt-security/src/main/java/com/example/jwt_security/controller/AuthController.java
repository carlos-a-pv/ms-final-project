package com.example.jwt_security.controller;

import com.example.jwt_security.dto.request.JwtRequestDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.JwtResponseDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;
import com.example.jwt_security.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO requestDTO){
        return ResponseEntity.ok(authService.login(requestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO requestDTO){
        return ResponseEntity.ok(authService.register(requestDTO));
    }
}
