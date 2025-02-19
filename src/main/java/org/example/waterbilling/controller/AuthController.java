package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.AuthRequest;
import org.example.waterbilling.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {
        return authService.generateToken(loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
