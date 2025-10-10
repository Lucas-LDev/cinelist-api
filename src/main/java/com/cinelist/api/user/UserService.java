package com.cinelist.api.user;

import com.cinelist.api.user.domain.User;
import com.cinelist.api.user.dto.RegisterRequestDTO;
import com.cinelist.api.user.exceptions.EmailAlreadyRegisteredException;
import com.cinelist.api.user.exceptions.UserNotFoundException;
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
            throw new EmailAlreadyRegisteredException("This email is already associated with an account");
        }

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User newUser = new User(dto.name(), dto.email(), encryptedPassword);
        userRepository.save(newUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
