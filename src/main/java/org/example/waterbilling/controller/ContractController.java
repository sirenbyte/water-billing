package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/contract")
@AllArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @GetMapping("/table-columns")
    public ResponseEntity<?> getTableColumns(){
        return contractService.getTableColumns();
    }

    @GetMapping("/table-data")
    public ResponseEntity<?> getTableData(@RequestBody ReportDynamicDto dto){
        return contractService.getTableData(dto);
    }

    @PostMapping("/action")
    public ResponseEntity<?> action(@RequestParam UUID contractId,@RequestParam String action){
        return contractService.action(contractId,action);
    }
}
