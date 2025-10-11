package com.ppdm.backend.security.controller;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
