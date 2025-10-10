package com.cinelist.api.user;

import com.cinelist.api.infra.security.TokenService;
import com.cinelist.api.refreshToken.RefreshTokenService;
import com.cinelist.api.user.dto.AuthRequestDTO;
import com.cinelist.api.user.dto.AuthResponseDTO;
import com.cinelist.api.user.dto.RefreshTokenRequestDTO;
import com.cinelist.api.user.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationService authenticationService, UserService userService, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login (@RequestBody @Valid AuthRequestDTO requestDTO) {
        AuthResponseDTO responseDTO = authenticationService.login(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO requestDTO) {
        userService.register(requestDTO);
        return ResponseEntity.status(201).body("Registration Successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody @Valid RefreshTokenRequestDTO requestDTO) {
        AuthResponseDTO responseDTO = authenticationService.refresh(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDTO requestDTO) {
        refreshTokenService.revokeToken(requestDTO.refreshToken());
        return ResponseEntity.noContent().build();
    }

}
