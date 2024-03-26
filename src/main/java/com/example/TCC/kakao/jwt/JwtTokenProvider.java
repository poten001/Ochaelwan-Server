package com.example.TCC.kakao.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

    @Component
    public class JwtTokenProvider {
        private final Key key;

        public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
            byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }

        public String generateToken(String subject, long validityPeriodMilliseconds) {
            Date now = new Date();
            Date validity = new Date(now.getTime() + validityPeriodMilliseconds);

            return Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }

        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
        }

        public String getSubject(String token) {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        }
    }