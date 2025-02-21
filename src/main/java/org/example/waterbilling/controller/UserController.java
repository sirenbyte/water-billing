package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.UserDto;
import org.example.waterbilling.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final AuthService userService;


    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto dto) {
        return userService.create(dto);
    }
}
