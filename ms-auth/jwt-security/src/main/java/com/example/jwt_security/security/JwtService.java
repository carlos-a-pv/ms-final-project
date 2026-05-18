package com.example.jwt_security.security;

import com.example.jwt_security.constant.ApplicationConstant;
import com.example.jwt_security.entity.enums.Role;
import com.example.jwt_security.exception.TokenExpiredException;
import com.example.jwt_security.exception.TokenInvalidException;
import com.example.jwt_security.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("1000000")
    private long jwtResetExpiration;


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateResetToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "RESET_PASSWORD")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtResetExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    public String getRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }


    public void validateResetPasswordToken(String token) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String type = claims.get("type", String.class);

            if (!"RESET_PASSWORD".equals(type)) {
                throw new TokenInvalidException(
                        ApplicationConstant.TOKEN_INVALID
                );
            }

            String email = claims.getSubject();

            if (email == null || email.isBlank()) {
                throw new TokenInvalidException(
                        ApplicationConstant.TOKEN_INVALID
                );
            }

        } catch (TokenExpiredException e) {

            throw new TokenExpiredException(
                    ApplicationConstant.TOKEN_EXPIRED
            );

        } catch (TokenInvalidException e) {

            throw new TokenInvalidException(
                    ApplicationConstant.TOKEN_INVALID
            );
        }
    }

}
