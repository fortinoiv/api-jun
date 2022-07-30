package com.fortinoiv.apijun.controllers;

import com.fortinoiv.apijun.application.AuthApplication;
import com.fortinoiv.apijun.domain.dto.request.LoginRequestDTO;
import com.fortinoiv.apijun.domain.dto.request.SignupRequestDTO;
import com.fortinoiv.apijun.domain.dto.response.JwtResponseDTO;
import com.fortinoiv.apijun.domain.dto.response.MessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthApplication authApplication;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return this.authApplication.authenticateUser(loginRequestDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequestDTO) {
        return this.authApplication.registerUser(signUpRequestDTO);
    }
}
