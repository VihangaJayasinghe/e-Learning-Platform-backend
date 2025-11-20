package com.Learn.ELP_backend.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStore.SecretKeyEntry;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.Learn.ELP_backend.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    public String SecretKey = "";

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            SecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            
            e.printStackTrace();
        }
    }

    public String GenerateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userid", user.getId());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000)) // 1 day
                .and()
                .signWith(GetKey())
                .compact();
    }

    private SecretKey GetKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String ExtractUsername(String token) {
    return Jwts.parser()
            .verifyWith(GetKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
}

public boolean ValidateToken(String token, UserDetails userDetails) {
    final String username = ExtractUsername(token);
    return (username.equals(userDetails.getUsername()) && !IsTokenExpired(token));
}

public String ExtractRole(String token) {
    return Jwts.parser()
            .verifyWith(GetKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
}

private boolean IsTokenExpired(String token) {
    return Jwts.parser()
            .verifyWith(GetKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date(System.currentTimeMillis()));
}
}
