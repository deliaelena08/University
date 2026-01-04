package com.ppdm.backend.security.config;

import com.ppdm.backend.security.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JwtTokenService {

    private static final long EXPIRATION_TIME = Duration.ofDays(1).toMillis();
    private static final String HEADER_STRING = "app-auth";
    private static final String CLAIM_ID = "id";

    @Value("${application.secret}")
    private String secret;

    public Authentication getAuthentication(final HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return null;
        }

        try {
            Claims claims = parseToken(token);
            Long userId = extractId(claims);
            return new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("user")));

        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtAuthenticationException(ex);
        }
    }

    public String createJwtToken(final Long id) {
        return Jwts.builder()
                .claim(CLAIM_ID, id)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        return (token != null && !token.isBlank()) ? token : null;
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Long extractId(Claims claims) {
        String value = Optional.ofNullable(claims.get(CLAIM_ID))
                .map(Object::toString)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("No id found in JWT"));
        return Long.valueOf(value);
    }

    public Authentication getAuthenticationFromToken(String token) {
        if (token == null) {
            return null;
        }

        try {
            Claims claims = parseToken(token);
            Long userId = extractId(claims);

            return new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("user")));

        } catch (Exception ex) {
            log.error("JWT parsing error: {}", ex.getMessage());
            throw new JwtAuthenticationException(ex);
        }
    }
}
