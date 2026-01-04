package com.ppdm.backend.user.exception;

public class LoginFaildException extends RuntimeException {
    public LoginFaildException(String message) {
        super(message);
    }
}
