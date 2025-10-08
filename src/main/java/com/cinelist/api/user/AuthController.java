package com.cinelist.api.user;

import com.cinelist.api.infra.security.TokenService;
import com.cinelist.api.refreshToken.RefreshTokenService;
import com.cinelist.api.user.domain.User;
import com.cinelist.api.user.dto.AuthRequestDTO;
import com.cinelist.api.user.dto.AuthResponseDTO;
import com.cinelist.api.user.dto.RefreshTokenRequestDTO;
import com.cinelist.api.user.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody @Valid AuthRequestDTO requestDTO) {
        try {
            var emailPassword = new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password());
            var auth = this.authenticationManager.authenticate(emailPassword);
            User user = (User) auth.getPrincipal();
            String accessToken = tokenService.generateAccessToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);

            return ResponseEntity.ok(new AuthResponseDTO(accessToken, refreshToken));
        } catch (Exception exc) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO requestDTO) {
        try {
            userService.register(requestDTO);
            return ResponseEntity.status(201).body("Registration Successful.");
        } catch (Exception exc) {
            return ResponseEntity.status(409).body("Registration failed.");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody @Valid RefreshTokenRequestDTO requestDTO) {
        try {
            String email = tokenService.validateRefreshToken(requestDTO.refreshToken());
            if (email == null) {
                return ResponseEntity.status(401).body(new AuthResponseDTO(null, null));
            }

            if (!refreshTokenService.isValid(requestDTO.refreshToken())) {
                return ResponseEntity.status(401).body(new AuthResponseDTO(null, null));
            }

            User user = userService.findByEmail(email);
            String newAccessToken = tokenService.generateAccessToken(user);

            return ResponseEntity.ok(new AuthResponseDTO(newAccessToken, null));
        } catch (Exception exc) {
            return ResponseEntity.status(401).body(new AuthResponseDTO(null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDTO requestDTO) {
        refreshTokenService.revokeToken(requestDTO.refreshToken());
        return ResponseEntity.noContent().build();
    }

}
