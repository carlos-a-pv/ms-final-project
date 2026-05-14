package com.example.jwt_security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @GetMapping
    public String customerEndpoint() {
        log.info("customerEndpoint method got called..");
        return "Hello Customer!";
    }
}
