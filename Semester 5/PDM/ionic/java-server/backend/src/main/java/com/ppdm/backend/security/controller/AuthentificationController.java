package com.ppdm.backend.security.controller;

import com.ppdm.backend.security.config.JwtTokenService;
import com.ppdm.backend.user.UserService;
import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.exception.LoginFaildException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthentificationController {

    private  final JwtTokenService jwtTokenService;
    private final UserService userService;

    @PostMapping(value = "/login")
    @SneakyThrows
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserDto user;
        try{
            user = userService.login(request);
        } catch (LoginFaildException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }

        String jwt = jwtTokenService.createJwtToken(user.getId(), user.getUsername());
        Cookie cookie = new Cookie("auth-cookie",jwt);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
