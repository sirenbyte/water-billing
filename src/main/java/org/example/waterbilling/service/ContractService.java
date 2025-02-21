package org.example.waterbilling.service;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.CardDto;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.model.entity.Canal;
import org.example.waterbilling.model.entity.Contract;
import org.example.waterbilling.model.entity.User;
import org.example.waterbilling.repository.CanalRepository;
import org.example.waterbilling.repository.ContractRepository;
import org.example.waterbilling.repository.UserRepository;
import org.example.waterbilling.service.script.AnnotationScript;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final CanalRepository canalRepository;

    public ResponseEntity<?> getTableColumns(){
        List<Map<String, String>> result = AnnotationScript.getFieldsFromClass(Contract.class);
        result.add(Map.of("accessor","fullName","title","ФИО"));
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
                    map.put("payStatus", contract.getPayStatus());
                    map.put("waterStatus", contract.getWaterStatus());
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
            contract.setWaterStatus("Успешно");
        }else {
            contract.setWaterStatus("Отказано");
        }
        return ResponseEntity.ok(contract);
    }

    public ResponseEntity<?> getByClientId(UUID clientId){
        List<Map> contracts = contractRepository.getContractsByUserId(clientId).stream().map(contract -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", contract.getId());
            map.put("fullName", getFullName(contract.getUserId()));
            map.put("canalId", getCanalName(contract.getCanalId()));
            map.put("createdAt", contract.getCreatedAt());
            map.put("fixedAt", contract.getFixedAt());
            map.put("payStatus", contract.getPayStatus());
            map.put("waterStatus", contract.getWaterStatus());
            map.put("tariff", contract.getTariff());
            map.put("price", contract.getPrice());
            map.put("value", contract.getValue());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(contracts);
    }

    public ResponseEntity<?> changeTariff(UUID id,Float tariff){
        Contract contract = contractRepository.findById(id).orElse(null);
        contract.setTariff(String.valueOf(tariff));
        contractRepository.save(contract);
        return ResponseEntity.ok(contract);
    }

    public ResponseEntity<?> createContract(UUID id,Float value){
        Contract contract = new Contract();
        contract.setValue(String.valueOf(value));
        contract.setCanalId(id);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        contract.setUserId(userRepository.findByEmail(login).getId());
        return ResponseEntity.ok(contractRepository.save(contract));
    }

    public ResponseEntity<?> pay(UUID id, CardDto dto){
        Contract contract = contractRepository.findById(id).orElse(null);
        contract.setPayStatus("Оплачено");
        return ResponseEntity.ok(contract);
    }
}
