package com.project.ExpenseTracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private final String jwtSecret = "YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=";
    private final int jwtExpiration = 172800000;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            return null;
        }

        String headerValue = bearerToken.trim();
        if (headerValue.isEmpty()) {
            return null;
        }

        if (headerValue.startsWith("Bearer ")) {
            String token = headerValue.substring(7).trim();
            return token.isEmpty() ? null : token;
        }

        return headerValue;
    }

    public String generateToken(String userName, int userId) {
        return Jwts.builder()
                .subject(userName)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith((SecretKey) key())
                .compact();
    }

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(jwtToken);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserNameFromToken(String jwt) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public Long getUserIdFromToken(String jwt) {
        Claims claims = getAllClaims(jwt);
        Number userIdValue = claims.get("userId", Number.class);
        return userIdValue == null ? null : userIdValue.longValue();
    }

    public Claims getAllClaims(String jwt) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

}
