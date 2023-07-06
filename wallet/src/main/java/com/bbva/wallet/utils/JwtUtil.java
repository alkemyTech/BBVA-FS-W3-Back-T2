package com.bbva.wallet.utils;

import io.jsonwebtoken.*;
import com.bbva.wallet.entities.User;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "uVZNPcEcqodLtqjHGU1e3KmdTs1CsMkQ+yDhFCNga1c=";

    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getFirstName())
                .claim("email", user.getEmail())
                .claim("user_id",user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour token validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token) {

            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }
}