package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.config.jwt.JwtUtil;
import org.example.waterbilling.model.dto.AuthRequest;
import org.example.waterbilling.model.dto.UserDto;
import org.example.waterbilling.model.entity.User;
import org.example.waterbilling.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContractService contractService;

    @Override
    public ResponseEntity<?> generateToken(AuthRequest dto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(dto.getLogin()));
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Неверный email или пароль"));
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword()));

        String jwt = jwtUtil.generateToken(dto.getLogin());

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("role", user.get().getRole());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return ResponseEntity.ok(toDto(user));
    }

    @Override
    public ResponseEntity<?> create(UserDto dto) {
        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("user");
        user.setEmail(dto.getEmail());
        user.setPosition(dto.getPosition());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setFathersname(dto.getFathersname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPersonalAccountNumber(dto.getPersonalAccountNumber());
        user.setIin(dto.getIin());
        user.setBin(dto.getBin());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(user));
    }

    @Override
    public ResponseEntity<?> userContracts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return contractService.getByClientId(user.getId());
    }

    private Map<String,String> toDto(User user){
        Map<String,String> map = new HashMap<>();
        map.put("id",user.getId().toString());
        map.put("email",user.getEmail());
        map.put("role",user.getRole());
        map.put("fullName",user.getFirstname()+" "+user.getLastname()+" "+user.getFathersname());
        return map;
    }
}
