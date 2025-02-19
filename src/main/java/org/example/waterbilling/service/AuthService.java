package org.example.waterbilling.service;

import org.example.waterbilling.model.dto.AuthRequest;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<?> generateToken(AuthRequest authRequest);
    ResponseEntity<?> refreshToken(String refreshToken);
}
