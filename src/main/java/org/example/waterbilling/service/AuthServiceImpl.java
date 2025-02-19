package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.config.jwt.JwtTokenProvider;
import org.example.waterbilling.model.dto.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    @Override
    public ResponseEntity<?> generateToken(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Missing refresh token"));
        }
        if (!tokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid refresh token"));
        }
        String username = tokenProvider.getUsernameFromJWT(refreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        return tokenProvider.generateToken(authentication);
    }
}
