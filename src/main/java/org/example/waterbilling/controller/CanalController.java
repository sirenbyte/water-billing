package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.service.CanalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/canal")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 36000,allowCredentials = "false")
public class CanalController {
    private final CanalService canalService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return canalService.getAll();
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getById(@PathVariable UUID id){
        return canalService.getById(id);
    }
}
