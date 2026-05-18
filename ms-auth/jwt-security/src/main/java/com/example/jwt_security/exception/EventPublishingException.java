package com.example.jwt_security.exception;

public class EventPublishingException extends RuntimeException{
    public EventPublishingException(String message, Throwable cause) {
        super(message);
    }
}
