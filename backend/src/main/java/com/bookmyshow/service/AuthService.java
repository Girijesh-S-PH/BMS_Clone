package com.bookmyshow.service;

import com.bookmyshow.dto.LoginRequest;
import com.bookmyshow.dto.SignupRequest;
import com.bookmyshow.dto.AuthResponse;
import com.bookmyshow.entity.User;
import com.bookmyshow.entity.UserRole;
import com.bookmyshow.exception.BadRequestException;
import com.bookmyshow.exception.ResourceNotFoundException;
import com.bookmyshow.exception.UnauthorizedException;
import com.bookmyshow.repository.UserRepository;
import com.bookmyshow.util.CaptchaValidator;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CaptchaValidator captchaValidator;

    public AuthService(UserRepository userRepository, CaptchaValidator captchaValidator) {
        this.userRepository = userRepository;
        this.captchaValidator = captchaValidator;
    }

    public AuthResponse signup(SignupRequest request) {
        // Validate captcha
        if (!captchaValidator.validateCaptcha(request.getCaptchaToken())) {
            throw new BadRequestException("Invalid captcha");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Phone number already registered");
        }

        // Validate phone number format (basic validation)
        if (request.getPhoneNumber() == null || request.getPhoneNumber().length() != 10) {
            throw new BadRequestException("Invalid phone number format. Must be 10 digits.");
        }

        // Hash password
        String passwordHash = captchaValidator.hashPassword(request.getPassword());

        // Create new user
        User user = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordHash)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(UserRole.USER)
                .build();

        user = userRepository.save(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Validate captcha
        if (!captchaValidator.validateCaptcha(request.getCaptchaToken())) {
            throw new BadRequestException("Invalid captcha");
        }

        // Find user by phone number
        Optional<User> userOpt = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("Invalid phone number or password");
        }

        User user = userOpt.get();

        // Verify password
        if (!captchaValidator.verifyPassword(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid phone number or password");
        }

        return AuthResponse.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .message("Login successful")
                .build();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User updateLastSelectedCity(Long userId, String city) {
        User user = getUserById(userId);
        user.setLastSelectedCity(city);
        return userRepository.save(user);
    }
}
