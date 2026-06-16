package com.insurance.service;

import com.insurance.dto.LoginRequestDTO;
import com.insurance.dto.LoginResponseDTO;
import com.insurance.dto.RegisterRequestDTO;

public interface AuthService {

    String register(RegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

}
