package com.candidate.pks.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final AppProperties appProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails,appProperties.getTokenExpirationDays());
    }

    public String generateVerifyToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails,appProperties.getVerifyLinkExpire());
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            int expirationDays
    ) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expirationDate = new Date(nowMillis + 1000L * 60L * 60L * 24L * expirationDays);
        extraClaims.put("created", now.getTime()); // Store timestamp in milliseconds
        extraClaims.put("expiration", expirationDate.getTime()); // Store timestamp in milliseconds
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Integer id) {
        return generateToken(new HashMap<>(), id, appProperties.getRefreshTokenExpirationDays());
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            Integer id,
            int expirationDays

    ) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expirationDate = new Date(nowMillis + 1000L * 60L * 60L * 24L * expirationDays);
        extraClaims.put("created", now.getTime()); // Store timestamp in milliseconds
        extraClaims.put("expiration", expirationDate.getTime()); // Store timestamp in milliseconds
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(id))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isRefreshTokenValid(String token) {
        return (!isTokenExpired(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJwtSecretKeys());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Instant getCreationTimeFromToken(String token) {
        long creationTimeMillis = extractClaim(token, claims -> claims.get("created", Long.class));
        return Instant.ofEpochMilli(creationTimeMillis);
    }

    public Instant getExpirationTimeFromToken(String token) {
        long expirationTimeMillis = extractClaim(token, claims -> claims.get("expiration", Long.class));
        return Instant.ofEpochMilli(expirationTimeMillis);
    }
}