package com.example.jwt_security.config;

import com.example.jwt_security.entity.User;
import com.example.jwt_security.entity.enums.Role;
import com.example.jwt_security.entity.enums.Status;
import com.example.jwt_security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }*/

    @Bean
    CommandLineRunner initAdmin(UserRepository repository, PasswordEncoder encoder) {

        return args -> {
            if (repository.findByUsername("admin@empresa.com").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin@empresa.com");
                admin.setPassword(
                        encoder.encode("admin123")
                );
                admin.setRole(Role.ADMINISTRATOR);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                admin.setStatus(Status.ENABLED);
                repository.save(admin);
            }
        };
    }
}
