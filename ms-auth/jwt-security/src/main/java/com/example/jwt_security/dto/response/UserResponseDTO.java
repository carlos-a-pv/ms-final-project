package com.example.jwt_security.dto.response;

import com.example.jwt_security.dto.RoleDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoleDTO> roles;  //added latest
}
