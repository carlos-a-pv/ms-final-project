package com.example.jwt_security.dto.request;

import lombok.Data;

@Data
public class JwtRequestDTO {

    private String username;
    private String password;
}
