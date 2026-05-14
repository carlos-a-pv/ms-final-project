package com.example.jwt_security.security;

import com.example.jwt_security.constant.ApplicationConstant;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.entity.UserRole;
import com.example.jwt_security.exception.ResourceNotFoundException;
import com.example.jwt_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("loadUserByUsername method got called with username : {}", username);
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ApplicationConstant.USER_NOT_FOUND));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getUserRoles().stream()
                                .map(userRole ->
                                        new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName()))
                                .toList()
                )
                .build();
    }
}
