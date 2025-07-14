package com.qudus.postra.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;

@Service
public class JwtService {

    // 🔐 This string holds the encoded secret key used to sign/verify tokens
    private String secreteKey = "";

    // ==========================
    // 1️⃣ Constructor - Generate Secret Key
    // ==========================
    public JwtService() {
        try {
            // Generate a secure key for HMAC SHA-256 algorithm
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();

            // Encode the raw key to a Base64 string
            secreteKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (Exception e) {
            System.out.println("Unable to generate secret key: " + e.getMessage());
        }
    }

    // ==========================
    // 2️⃣ Generate JWT Token
    // ==========================
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        // Build the JWT token:
        // - add claims
        // - set subject (username)
        // - set issue & expiration date
        // - sign with secret key
        return Jwts.builder()
                .claims().add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .and()
                .signWith(getKey())
                .compact();
    }

    // ==========================
    // 3️⃣ Get the Signing Key
    // ==========================
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ==========================
    // 4️⃣ Extract Claims (Generic)
    // ==========================
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // ==========================
    // 5️⃣ Extract All Claims from Token
    // ==========================
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ==========================
    // 6️⃣ Extract Username (Subject)
    // ==========================
    public String extractUsernameOrEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ==========================
    // 7️⃣ Extract Expiration Date
    // ==========================
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ==========================
    // 8️⃣ Check if Token is Expired
    // ==========================
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ==========================
    // 9️⃣ Validate Token Against UserDetails
    // ==========================
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsernameOrEmail(token);

        // Token is valid if:
        // - the username matches the one in UserDetails
        // - and token is not expired
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
