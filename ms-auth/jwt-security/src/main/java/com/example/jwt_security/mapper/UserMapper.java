package com.example.jwt_security.mapper;

import com.example.jwt_security.dto.RoleDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;
import com.example.jwt_security.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO userRequestDTO) {
       if(userRequestDTO == null) {
           return null;
       }
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }

    public UserResponseDTO toDTO(User user) {
        if(user == null) {
            return null;
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPassword(null);  //Note: We should not expose the password in the response DTO for security reasons.
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setUpdatedAt(user.getUpdatedAt());

        List<RoleDTO> roleDTOList = new ArrayList<>();
        if (user.getUserRoles() != null) {
            user.getUserRoles().forEach(userRole -> {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setId(userRole.getRole().getId());
                roleDTO.setRoleName(userRole.getRole().getRoleName());
                roleDTOList.add(roleDTO);
            });
        }
        userResponseDTO.setRoles(roleDTOList);
        return userResponseDTO;
    }
}
