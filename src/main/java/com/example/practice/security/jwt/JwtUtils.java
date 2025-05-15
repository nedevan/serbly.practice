package com.example.practice.security.jwt;

import com.example.practice.security.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateToken(AppUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }
        catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }
        catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
