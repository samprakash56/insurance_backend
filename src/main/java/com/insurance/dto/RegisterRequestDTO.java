package com.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20,
            message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20,
            message = "Password must be between 6 and 20 characters")
    private String password;

    @NotBlank(message = "First Name is required")
    @Size(min = 3, max = 30,
            message = "First Name must be between 3 and 30 characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 1, max = 30,
            message = "Last Name must be between 1 and 30 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Lob is required")
    @Size(min = 3, max = 10,
            message = "Lob must be between 3 and 10 characters")
    private String lob;



}