package com.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtils {

    private Key secretKey;

    // Время действия токена: 24 часа
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    @PostConstruct
    public void init() {
        // Пример строки, которую можно заменить на значение из конфигурации
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Генерация токена на основе UserDetails
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Извлечение имени пользователя
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Проверка, не истёк ли токен
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Проверка, валиден ли токен
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
