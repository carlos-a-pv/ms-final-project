package com.example.jwt_security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping
    public String adminEndpoint() {
        log.info("Admin endpoint accessed");
        return "Welcome, Admin!";
    }
}
