package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.model.entity.Canal;
import org.example.waterbilling.model.entity.Contract;
import org.example.waterbilling.model.entity.User;
import org.example.waterbilling.repository.CanalRepository;
import org.example.waterbilling.repository.ContractRepository;
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
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final CanalRepository canalRepository;

    public ResponseEntity<?> getTableColumns(){
        List<Map<String, String>> result = AnnotationScript.getFieldsFromClass(Contract.class);
        result.add(Map.of("fullName","ФИО"));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getTableData(ReportDynamicDto dto){
        return ResponseEntity.ok(contractRepository.findAll().stream()
                .map(contract -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", contract.getId());
                    map.put("fullName", getFullName(contract.getUserId()));
                    map.put("canalId", getCanalName(contract.getCanalId()));
                    map.put("createdAt", contract.getCreatedAt());
                    map.put("fixedAt", contract.getFixedAt());
                    map.put("status", contract.getStatus());
                    map.put("tariff", contract.getTariff());
                    map.put("price", contract.getPrice());
                    return map;
                }));
    }

    private String getFullName(UUID id){
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            return "";
        }
        return user.getFirstname()+" "+user.getLastname()+" "+user.getFathersname();
    }
    private String getCanalName(UUID id){
        Canal canal = canalRepository.findById(id).orElse(null);
        if(canal==null){
            return "";
        }
        return canal.getName();
    }

    public ResponseEntity<?> action(UUID id, String action){
        Contract contract = contractRepository.findById(id).orElse(null);
        if(action.equals("start")){
            contract.setStatus("Успешно");
        }else {
            contract.setStatus("Отказано");
        }
        return ResponseEntity.ok(contract);
    }
}
