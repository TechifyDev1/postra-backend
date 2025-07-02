package com.qudus.postra.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private String secreteKey = "";

    JwtService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secreteKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (Exception e) {
            System.out.println("Unable o generate secrete key" + e.getMessage());
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30)).and().signWith(getKey()).compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
