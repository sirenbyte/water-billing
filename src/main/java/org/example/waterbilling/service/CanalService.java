package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.repository.CanalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CanalService {
    private final CanalRepository canalRepository;

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(canalRepository.findAll()
                .stream()
                .map(it -> Map.of("id", it.getId(), "name", it.getName()))
                .collect(Collectors.toList()));
    }
}
