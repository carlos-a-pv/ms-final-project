package com.example.jwt_security.controller;

import com.example.jwt_security.dto.ApiError;
import com.example.jwt_security.dto.request.JwtRequestDTO;
import com.example.jwt_security.dto.request.ResetPasswordDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.JwtResponseDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;
import com.example.jwt_security.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name="Authentication", description = "Endpoint for users authentication")
public class AuthController {

    private final IAuthService authService;

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Login success",
                                    value = """
                                        {
                                          "token": "eyJhbGciOiJIUzI1NiJ9...",
                                          "type": "Bearer"
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    name = "Bad request",
                                    value = """
                                        {
                                          "message": "Invalid credentials"
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO requestDTO){
        return ResponseEntity.ok(authService.login(requestDTO));
    }

    @Operation(
            summary = "Recover password",
            description = "Starts the password recovery process for a user account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recovery process started successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Recovery success",
                                    value = """
                                        {
                                          "id": 1,
                                          "email": "user@example.com",
                                          "message": "Recovery email sent successfully"
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    name = "Bad request",
                                    value = """
                                        {
                                          "message": "Email is required"
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @PostMapping("/recover-password/{email}")
    public ResponseEntity<UserResponseDTO> recoverPassword(@PathVariable String email){
        return ResponseEntity.ok(authService.recoverPassword(email));
    }

    @Operation(
            summary = "Reset password",
            description = "Resets the user's password using the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Password reset success",
                                    value = """
                                        {
                                          "id": 1,
                                          "email": "user@example.com",
                                          "message": "Password updated successfully"
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    name = "Invalid token",
                                    value = """
                                        {
                                          "message": "Reset token is invalid or expired"
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @PostMapping("/reset-password")
    public ResponseEntity<UserResponseDTO> resetPassword(@RequestBody ResetPasswordDTO requestDTO){
        return ResponseEntity.ok(authService.resetPassword(requestDTO));
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all registered users"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "Users list",
                                    value = """
                                        [
                                          {
                                            "id": 1,
                                            "name": "Carlos",
                                            "email": "carlos@example.com"
                                          },
                                          {
                                            "id": 2,
                                            "name": "Ana",
                                            "email": "ana@example.com"
                                          }
                                        ]
                                        """
                            ))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    ))
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(authService.getAllUsers());
    }

}
