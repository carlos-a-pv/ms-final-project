package com.example.jwt_security.repository;

import com.example.jwt_security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleNameIgnoreCase(String name);

}
