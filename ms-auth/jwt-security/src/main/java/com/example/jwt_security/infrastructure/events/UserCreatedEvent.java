package com.example.jwt_security.infrastructure.events;

import com.example.jwt_security.entity.User;

public record UserCreatedEvent (User user, String token){
}
