package com.insurance.serviceimpl;

import com.insurance.dto.LoginRequestDTO;
import com.insurance.dto.LoginResponseDTO;
import com.insurance.dto.RegisterRequestDTO;
import com.insurance.entity.UsersEntity;
import com.insurance.exception.InvalidCredentials;
import com.insurance.exception.UserAlreadyExistsException;
import com.insurance.repository.UserRepository;
import com.insurance.security.JwtUtil;
import com.insurance.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String register(RegisterRequestDTO dto) {

        log.info(
                "Registration attempt for username: {}",
                dto.getUsername());

        if (userRepository
                .findByUsername(dto.getUsername())
                .isPresent()) {

            log.warn(
                    "Registration failed. User already exists: {}",
                    dto.getUsername());

            throw new UserAlreadyExistsException(
                    "User already present");
        }

        UsersEntity user = new UsersEntity();

        user.setRole("USER");
        user.setUsername(dto.getUsername());
        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setLob(dto.getLob());

        userRepository.save(user);

        log.info(
                "User registered successfully: {}",
                dto.getUsername());

        return "User created successfully";
    }

    @Override
    public LoginResponseDTO login(
            LoginRequestDTO dto) {

        log.info(
                "Login attempt for username: {}",
                dto.getUsername());

        UsersEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> {
            log.warn("Login failed. User not found: {}", dto.getUsername());return new InvalidCredentials("Invalid credentials");});

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {

            log.warn("Login failed. Invalid password for user: {}", dto.getUsername());

            throw new InvalidCredentials("Invalid credentials");
        }

        String token =
                jwtUtil.generateToken(
                        user.getUsername());

        log.info(
                "JWT generated successfully for user: {}",
                dto.getUsername());

        LoginResponseDTO response =
                new LoginResponseDTO();

        response.setUsername(
                user.getUsername());

        response.setToken(token);

        response.setRole(
                user.getRole());

        log.info(
                "Login successful for user: {}",
                dto.getUsername());

        return response;
    }
}