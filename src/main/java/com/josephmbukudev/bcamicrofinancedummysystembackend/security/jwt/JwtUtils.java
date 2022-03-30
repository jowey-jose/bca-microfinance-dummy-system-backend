package com.josephmbukudev.bcamicrofinancedummysystembackend.security.jwt;

// This Class has 3 Functions:
// - Generate a Jwt from username, date, expiration and secret.
// - Get username from Jwt.
// - Validate a Jwt.

import com.josephmbukudev.bcamicrofinancedummysystembackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger =LoggerFactory.getLogger(JwtUtils.class);
    @Value("${josephmbukudev.app.jwtSecret}")
    private String jwtSecret;
    @Value("${josephmbukudev.app.jwtExpirationMs}")
    private int jwtExpirationsMs;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationsMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e){
            logger.error("Invalid Jwt Token: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Jwt Token is expired: {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Jwt Token is unsupported: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Jwt Claims String is Empty: {}", e.getMessage());
        }
        return false;
    }
}
