package org.example.waterbilling.config.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Секретный ключ (в реальном проекте храните его в application.properties или в переменных окружения)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Время жизни токена (например, 1 час)
    private final long jwtExpirationInMs = 3600000;
    private final long refreshTokenExpirationInMs = 604800000;     // 7 дней

    // Генерация токена на основе аутентификации
    public ResponseEntity<?> generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

         String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
         return ResponseEntity.ok(JwtResponse.builder()
                 .accessToken(jwt)
                 .refreshToken(generateRefreshToken(authentication))
                 .fullName(username)
                 .roleName("admin")
                 .privileges(new ArrayList<>())
                 .build());
    }

    public String generateRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Извлечение username из токена
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public void getClaimsFromJWT(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Валидация токена
    public boolean validateToken(String authToken) {
        try {
            getClaimsFromJWT(authToken);
            return true;
        } catch (JwtException ex) {
            // Можно добавить логирование ошибки
            System.out.println("Invalid JWT: " + ex.getMessage());
        }
        return false;
    }
}
