package com.example.jwt_security.repository;

import com.example.jwt_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndUsername(String email, String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(String username);

    Optional<User> findByUsername(String username);
}
