package com.example.jwt_security.service;

import com.example.jwt_security.dto.EmployeeCreatedEventDTO;
import com.example.jwt_security.dto.request.JwtRequestDTO;
import com.example.jwt_security.dto.request.ResetPasswordDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.JwtResponseDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;

import java.util.List;

public interface IAuthService {

    JwtResponseDTO login(JwtRequestDTO request);
    UserResponseDTO register(UserRequestDTO request);
    UserResponseDTO resetPassword(ResetPasswordDTO request);
    UserResponseDTO recoverPassword(String email);
    List<UserResponseDTO> getAllUsers();
    String createDefaultUser(EmployeeCreatedEventDTO eventDTO);
    void removeAccessCredientals(String email);
}
