package com.example.jwt_security.service;

import com.example.jwt_security.dto.request.JwtRequestDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.JwtResponseDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;

public interface IAuthService {

    JwtResponseDTO login(JwtRequestDTO request);
    UserResponseDTO register(UserRequestDTO request);
}
