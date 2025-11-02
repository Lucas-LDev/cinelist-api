package com.cinelist.api.refreshToken;

import com.cinelist.api.refreshToken.domain.RefreshToken;
import com.cinelist.api.refreshToken.exceptions.InvalidTokenException;
import com.cinelist.api.user.domain.User;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken saveRefreshToken(User user, String token, Instant expiresAt) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(expiresAt);
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(tk -> !tk.isExpired() && !tk.isRevoked())
                .isPresent();
    }

    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(tk -> {tk.setRevoked(true);
                refreshTokenRepository.save(tk);
                });
    }

    public void validateStoredToken(String token) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found or revoked"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new InvalidTokenException("Refresh token expired");
        }
    }
}
