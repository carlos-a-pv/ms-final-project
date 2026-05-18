package com.example.jwt_security.exception;

public class UserDisabledException extends RuntimeException{
    public  UserDisabledException(String message){
        super(message);
    }
}
