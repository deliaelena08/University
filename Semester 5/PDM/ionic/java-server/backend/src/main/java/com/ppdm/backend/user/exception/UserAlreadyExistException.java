package com.ppdm.backend.user.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("User already exists");
    }
}
