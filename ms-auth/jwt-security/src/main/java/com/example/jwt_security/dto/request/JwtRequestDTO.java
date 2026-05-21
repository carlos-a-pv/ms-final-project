package com.example.jwt_security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequestDTO {
    @NotNull
    @NotEmpty
    @Email(message = "The correct format email is required")
    private String username;
    @NotNull
    @NotEmpty
    private String password;
}
