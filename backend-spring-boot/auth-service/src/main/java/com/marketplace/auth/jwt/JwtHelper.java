package com.marketplace.auth.jwt;

import com.marketplace.auth.exception.JwtTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {
    private final String jwtSecret;
    private final Long jwtExpirationHrs;

    public JwtHelper(@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.expiration_hrs}") Long jwtExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationHrs = jwtExpirationMs;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationHrs * 3600 * 1000))
                .signWith(new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName()), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expDate = claims.getExpiration();

        return expDate.before(new Date());
    }

    public String verifyToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String email = claims.getSubject();

        if (isTokenExpired(token)) {
            throw new JwtTokenExpiredException("JWT token has expired");
        }

        return email;
    }
}
