package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.repository.CanalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CanalService {
    private final CanalRepository canalRepository;

    public ResponseEntity<?> getAll() {
        Map<String, Object> result = new HashMap<>();
        result.put("data",canalRepository.findAll()
                .stream()
                .map(it -> Map.of("id", it.getId(), "name", it.getName()))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getById(UUID id){
        return ResponseEntity.ok(canalRepository.findById(id).orElse(null));
    }
}
