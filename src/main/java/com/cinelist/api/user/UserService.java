package com.cinelist.api.user;

import com.cinelist.api.user.domain.User;
import com.cinelist.api.user.dto.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequestDTO dto) {
        if (this.userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Registration failed. Please verify your information.");
        }

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User newUser = new User(dto.name(), dto.email(), encryptedPassword);
        userRepository.save(newUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
