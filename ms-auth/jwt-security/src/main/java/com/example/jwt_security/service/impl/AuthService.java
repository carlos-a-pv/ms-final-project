package com.example.jwt_security.service.impl;

import com.example.jwt_security.constant.ApplicationConstant;
import com.example.jwt_security.dto.request.JwtRequestDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.JwtResponseDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;
import com.example.jwt_security.entity.Role;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.entity.UserRole;
import com.example.jwt_security.exception.AlreadyExistException;
import com.example.jwt_security.exception.ResourceNotFoundException;
import com.example.jwt_security.mapper.UserMapper;
import com.example.jwt_security.repository.RoleRepository;
import com.example.jwt_security.repository.UserRepository;
import com.example.jwt_security.security.JwtService;
import com.example.jwt_security.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public JwtResponseDTO login(JwtRequestDTO request) {
        log.info("login method got called with username : {} and password : {}",request.getUsername(), request.getPassword());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstant.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException(ApplicationConstant.INVALID_CREDENTIALS);
        String token = jwtService.generateToken(user.getUsername());
        JwtResponseDTO responseDTO = new JwtResponseDTO();
        responseDTO.setToken(token);
        responseDTO.setUsername(user.getUsername());
        return responseDTO;
    }

    @Override
    public UserResponseDTO register(UserRequestDTO request) {
        log.info("register method got called with username : {} and password : {}",request.getUsername(), request.getPassword());
        boolean exists = userRepository.existsByEmailAndUsername(request.getEmail(), request.getUsername());
        if(exists)
            throw new AlreadyExistException(ApplicationConstant.USER_ALREADY_EXIST);
        User user = userMapper.toEntity(request);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        List<UserRole> userRoleList = new ArrayList<>();
        if(request.getRoles() != null && !request.getRoles().isEmpty()){
            request.getRoles().forEach(roleDTO -> {
                UserRole userRole = new UserRole();
                Role role = null;
                //default customer role if role is not exist
                if(roleDTO.getRoleName()==null){
                    role = roleRepository.findByRoleNameIgnoreCase(ApplicationConstant.CUSTOMER)
                            .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstant.ROLE_NOT_FOUND));
                    userRole.setUser(user);
                    userRole.setRole(role);
                }else{
                    role = roleRepository.findByRoleNameIgnoreCase(roleDTO.getRoleName())
                            .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstant.ROLE_NOT_FOUND));
                    userRole.setUser(user);
                    userRole.setRole(role);
                }
                userRoleList.add(userRole);
            });
        }
        user.setUserRoles(userRoleList);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}
