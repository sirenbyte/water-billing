package org.example.waterbilling.service;

import org.example.waterbilling.model.dto.AuthRequest;
import org.example.waterbilling.model.dto.UserDto;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<?> generateToken(AuthRequest authRequest);
    ResponseEntity<?> getCurrentUser();
    ResponseEntity<?> create(UserDto dto);
    ResponseEntity<?> userContracts();
    ResponseEntity<?> types();

}
