package com.ppdm.backend.security.controller;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
}
