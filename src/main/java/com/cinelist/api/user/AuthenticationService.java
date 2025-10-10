package com.cinelist.api.user;

import com.cinelist.api.infra.security.TokenService;
import com.cinelist.api.refreshToken.RefreshTokenService;
import com.cinelist.api.refreshToken.exceptions.InvalidTokenException;
import com.cinelist.api.user.domain.User;
import com.cinelist.api.user.dto.AuthRequestDTO;
import com.cinelist.api.user.dto.AuthResponseDTO;
import com.cinelist.api.user.dto.RefreshTokenRequestDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, TokenService tokenService, RefreshTokenService refreshTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    public AuthResponseDTO login(AuthRequestDTO requestDTO) {
        var emailPassword = new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password());
        var auth = this.authenticationManager.authenticate(emailPassword);
        User user = (User) auth.getPrincipal();

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO refresh(RefreshTokenRequestDTO requestDTO) {
        String refreshToken = requestDTO.refreshToken();
        String email = tokenService.validateRefreshToken(refreshToken);

        if (!refreshTokenService.isValid(refreshToken)) {
            throw new InvalidTokenException("Refresh Token has been revoked or is invalid");
        }

        User user = userService.findByEmail(email);
        String newAccessToken = tokenService.generateAccessToken(user);
        return new AuthResponseDTO(newAccessToken, refreshToken);
    }
}
