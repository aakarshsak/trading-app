package com.sinha.trading_app.common.util;

import com.sinha.trading_app.common.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    @Autowired
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID userId, String email, List<String> roles, List<String> permissions) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpiry());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", roles)
                .claim("permissions", permissions)
                .claim("type", "ACCESS")
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .id(UUID.randomUUID().toString())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiry());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", "REFRESH")
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .id(UUID.randomUUID().toString())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Parse and validate JWT token
     * Verifies signature and expiration
     *
     * @param token JWT token string
     * @return Claims object containing token data
     * @throws JwtException if token is invalid
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            System.out.println("Error in parsing..." + e);
            throw e;
        }
    }


    /**
     * Extract user ID from token subject claim
     */
    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(parseToken(token).getSubject());
    }

    /**
     * Extract email from token subject claim
     */
    public String getEmailFromToken(String token) {
        return parseToken(token).get("email", String.class);
    }

    /**
     * Extract roles from token subject claim
     */
    public List getUserRolesFromToken(String token) {
        return parseToken(token).get("roles", List.class);
    }

    /**
     * Extract permissions from token subject claim
     */
    public List getUserPermissionsFromToken(String token) {
        return parseToken(token).get("roles", List.class);
    }

    /**
     * Get JWT ID (jti claim)
     * User for token blacklisting on logout
     */
    public String getTokenId(String token) {
        return parseToken(token).getId();
    }

    /**
     * Get token type from custom claims
     * Returns "ACCESS" or "REFRESH"
     */
    public String getTokenType(String token) {
        return parseToken(token).get("type", String.class);
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Extract expiration date from token
     */
    public Date getExpirationDate(String token) {
        return parseToken(token).getExpiration();
    }

    /**
     * Extract issued-at date from token
     */
    public Date getIssuedAtDate(String token) {
        return parseToken(token).getIssuedAt();
    }

    /**
     * Get all claims as a Map
     */
    public Map<String, Object> getAllClaims(String token) {
        return parseToken(token);
    }

    /**
     * Validate token signature and expiry
     * Returns false for any invalid token (malformed, expired, etc)
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch(SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException ignored) {
        }

        return false;
    }
}
