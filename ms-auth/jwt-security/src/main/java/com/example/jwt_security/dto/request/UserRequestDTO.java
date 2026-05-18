package com.example.jwt_security.dto.request;

import com.example.jwt_security.entity.enums.Role;
import lombok.Data;

@Data
public class UserRequestDTO {

    private String username;
    private String password;
    private Role role;
}
