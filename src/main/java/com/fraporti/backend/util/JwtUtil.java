package com.fraporti.backend.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    public static SecretKey secretKey = Keys.hmacShaKeyFor(generateSafeToken().getBytes(StandardCharsets.UTF_8));

    private static String generateSafeToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[36];
        random.nextBytes(bytes);
        var encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    public static String generateToken(String username) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        return Jwts.builder().claims().empty().add(claims)
                .expiration(new Date(System.currentTimeMillis()+Duration.ofDays(7).toMillis()))
                .issuedAt(new Date()).and().signWith(secretKey, Jwts.SIG.HS256).compact();
    }

    public static String getUsernameToken(String token){
        if(token !=null){
            return parseToken(token).getSubject();
        }else{
            return null;
        }
    }

    public static boolean isTokenExpired(String token){
        if(token !=null){
            return parseToken(token).getExpiration().before(new Date());
        }else{
            return false;
        }
    }

    private static Claims parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token.replace("Bearer ", "")).getPayload();
    }
}