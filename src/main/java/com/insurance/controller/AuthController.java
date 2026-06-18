package com.insurance.controller;

import com.insurance.dto.LoginRequestDTO;
import com.insurance.dto.LoginResponseDTO;
import com.insurance.dto.MessageResponse;
import com.insurance.dto.RegisterRequestDTO;
import com.insurance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication APIs",
        description = "User Registration and Login"
)
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log =
            LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register User",
            description = "Creates a new user account"
    )
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegisterRequestDTO dto) {

        log.info(
                "Registration request received for username: {}",
                dto.getUsername());

        String response = authService.register(dto);

        log.info(
                "Registration completed successfully for username: {}",
                dto.getUsername());

        return ResponseEntity.ok(new MessageResponse(response));
    }

    @Operation(
            summary = "Login",
            description = "Returns JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        log.info(
                "Login request received for username: {}",
                dto.getUsername());

        LoginResponseDTO response =
                authService.login(dto);

        log.info(
                "Login successful for username: {}",
                dto.getUsername());

        return ResponseEntity.ok(response);
    }
}