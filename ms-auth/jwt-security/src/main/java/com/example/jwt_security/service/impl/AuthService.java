package com.example.jwt_security.service.impl;

import com.example.jwt_security.constant.ApplicationConstant;
import com.example.jwt_security.dto.EmployeeCreatedEventDTO;
import com.example.jwt_security.dto.request.JwtRequestDTO;
import com.example.jwt_security.dto.request.ResetPasswordDTO;
import com.example.jwt_security.dto.request.UserRequestDTO;
import com.example.jwt_security.dto.response.JwtResponseDTO;
import com.example.jwt_security.dto.response.UserResponseDTO;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.entity.enums.Role;
import com.example.jwt_security.entity.enums.Status;
import com.example.jwt_security.exception.AlreadyExistException;
import com.example.jwt_security.exception.InvalidCredentialsException;
import com.example.jwt_security.exception.ResourceNotFoundException;
import com.example.jwt_security.exception.UserDisabledException;
import com.example.jwt_security.infrastructure.EventProcessingService;
import com.example.jwt_security.infrastructure.events.UserCreatedEvent;
import com.example.jwt_security.infrastructure.events.UserRecoverEventDTO;
import com.example.jwt_security.mapper.UserMapper;
import com.example.jwt_security.repository.UserRepository;
import com.example.jwt_security.security.JwtService;
import com.example.jwt_security.service.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    //private final EventProcessingService eventProcessingService;
    //private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public JwtResponseDTO login(JwtRequestDTO request) {
        log.info("login method got called with username : {} and password : {}",request.getUsername(), request.getPassword());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstant.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException(ApplicationConstant.INVALID_CREDENTIALS);

        if(user.getStatus().equals(Status.DISABLED))
            throw new UserDisabledException(ApplicationConstant.USER_DISABLED);

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return JwtResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }

    @Override
    public UserResponseDTO register(UserRequestDTO request) {
        log.info("register method got called with username : {} and password : {}",request.getUsername(), request.getPassword());
        boolean exists = userRepository.existsByUsername(request.getUsername());
        if(exists)
            throw new AlreadyExistException(ApplicationConstant.USER_ALREADY_EXIST);
        User user = userMapper.toEntity(request);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public UserResponseDTO resetPassword(ResetPasswordDTO request) {

        jwtService.validateResetPasswordToken(request.getToken());

        String username = jwtService.extractUsername(request.getToken());
        User userFound = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationConstant.USER_NOT_FOUND));


        userFound.setPassword(passwordEncoder.encode((request.getNewPassword())));
        userFound.setUpdatedAt(LocalDateTime.now());
        userFound.setStatus(Status.ENABLED);
        userRepository.save(userFound);

        return UserResponseDTO.builder()
                .id(userFound.getId())
                .username(userFound.getUsername())
                .password(passwordEncoder.encode(request.getNewPassword()))
                .createdAt(userFound.getCreatedAt())
                .updatedAt(userFound.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public UserResponseDTO recoverPassword(String email) {

        User userFound = userRepository.findByUsername(email).orElseThrow( ()-> new ResourceNotFoundException(ApplicationConstant.USER_NOT_FOUND));
        String resetToken = generateRecoverToken(email);

        applicationEventPublisher.publishEvent(
                UserRecoverEventDTO.builder()
                        .id(userFound.getId())
                        .email(userFound.getUsername())
                        .recoverToken(resetToken)
                        .build()
        );

        return UserResponseDTO.builder()
                .id(userFound.getId())
                .username(userFound.getUsername())
                .password(userFound.getPassword())
                .createdAt(userFound.getCreatedAt())
                .updatedAt(userFound.getUpdatedAt())
                .build();
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public String createDefaultUser(EmployeeCreatedEventDTO eventDTO){

        Optional<User> userOptional = userRepository.findByUsername(eventDTO.email());

        if(userOptional.isPresent()){
            return jwtService.generateResetToken(eventDTO.email());
        }

        User userSaved = userRepository.save(User.builder()
                .username(eventDTO.email())
                .password(passwordEncoder.encode(eventDTO.email()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.USER)
                .status(Status.DISABLED)
                .employeeId(eventDTO.id())
                .build());

        String token =  jwtService.generateResetToken(userSaved.getUsername());
        applicationEventPublisher.publishEvent(new UserCreatedEvent(userSaved, token));
        return token;
    }

    @Override
    public void removeAccessCredientals(String email) {
        User userFound = userRepository.findByUsername(email).orElseThrow( ()->  new ResourceNotFoundException(ApplicationConstant.USER_NOT_FOUND));

        userFound.setStatus(Status.DISABLED);
        userRepository.save(userFound);
    }

    public String generateRecoverToken(String email){
        return jwtService.generateResetToken(email);
    }
}
