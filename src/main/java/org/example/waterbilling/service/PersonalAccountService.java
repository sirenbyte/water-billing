package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.model.entity.User;
import org.example.waterbilling.repository.UserRepository;
import org.example.waterbilling.service.script.AnnotationScript;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonalAccountService {
    private final UserRepository userRepository;

    public ResponseEntity<?> getTableColumns(){
        List<Map<String, String>> result = AnnotationScript.getFieldsFromClass(User.class);
        result.add(Map.of("accessor","fullName","title","ФИО"));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getTableData(ReportDynamicDto dto){
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fullName", user.getFirstname()+" "+user.getLastname()+" "+user.getFathersname());
                    map.put("phoneNumber", user.getPhoneNumber());
                    map.put("login", user.getLogin());
                    map.put("email", user.getEmail());
                    map.put("personalAccountNumber", user.getPersonalAccountNumber());
                    map.put("status", user.getStatus());
                    map.put("position", user.getPosition());
                    map.put("createdAt", user.getCreatedAt());
                    map.put("organization", user.getOrganization());
                    map.put("iin", user.getIin());
                    map.put("bin", user.getBin());
                    return map;
                }));

    }

    public ResponseEntity<?> getById(UUID id){
        User user = userRepository.findById(id).orElse(null);
        Map<String,String> map = new HashMap<>();
        map.put("fullName", user.getFirstname()+" "+user.getLastname()+" "+user.getFathersname());
        map.put("phoneNumber", user.getPhoneNumber());
        map.put("login", user.getLogin());
        map.put("email", user.getEmail());
        map.put("personalAccountNumber", user.getPersonalAccountNumber());
        map.put("status", user.getStatus());
        map.put("position", user.getPosition());
        map.put("createdAt", String.valueOf(user.getCreatedAt()));
        map.put("organization", user.getOrganization());
        map.put("iin", user.getIin());
        map.put("bin", user.getBin());
        return ResponseEntity.ok(map);
    }
}
