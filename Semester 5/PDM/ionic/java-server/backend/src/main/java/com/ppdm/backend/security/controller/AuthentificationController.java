package com.ppdm.backend.security.controller;

import com.ppdm.backend.security.config.JwtTokenService;
import com.ppdm.backend.user.UserService;
import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.exception.LoginFaildException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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

        String jwt = jwtTokenService.createJwtToken(user.getId());
        Cookie cookie = new Cookie("auth-cookie",jwt);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @PostMapping("/signup")
    @SneakyThrows
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, HttpServletResponse response) {
        UserDto user;
        try {
            UserDto userDto = new UserDto();
            userDto.setUsername(request.getUsername());
            userDto.setPassword(request.getPassword());
            userDto.setActive(true);
            userDto.setLastActive(LocalDateTime.now());
            user = userService.saveUser(userDto);

            String jwt = jwtTokenService.createJwtToken(user.getId());
            Cookie cookie = new Cookie("auth-cookie", jwt);
            cookie.setPath("/");
            cookie.setDomain("localhost");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "token", jwt
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        var authentication = jwtTokenService.getAuthentication(request);
        if (authentication != null) {
            Long userId = (Long) authentication.getPrincipal();
            userService.setUserInactive(userId);
        }
        Cookie cookie = new Cookie("auth-cookie", null);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

}
