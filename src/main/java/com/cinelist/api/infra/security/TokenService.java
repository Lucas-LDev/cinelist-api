package com.cinelist.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cinelist.api.refreshToken.RefreshTokenService;
import com.cinelist.api.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Value("${api.security.refresh.secret}")
    private String refreshSecret;

    private final RefreshTokenService refreshTokenService;

    public TokenService(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId().toString())
                    .withClaim("name", user.getName())
                    .withExpiresAt(generateAccessTokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exc) {
            throw new RuntimeException("Error while generating access token", exc);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            var expiration = generateRefreshTokenExpiration();
            var token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId().toString())
                    .withClaim("type", "refresh")
                    .withExpiresAt(expiration)
                    .sign(algorithm);

            refreshTokenService.saveRefreshToken(user, token, expiration);
            return token;

        } catch (JWTCreationException exc) {
            throw new RuntimeException("Error while generating refresh token", exc);
        }
    }

    public String validateAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String validateRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(refreshSecret);
            var decodedJWT = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .withClaim("type", "refresh")
                    .build()
                    .verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateAccessTokenExpiration() {
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC);
    }

    private Instant generateRefreshTokenExpiration() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC);
    }
}