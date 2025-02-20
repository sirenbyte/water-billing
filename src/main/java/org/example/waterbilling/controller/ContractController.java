package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.service.ContractService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/contract")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 36000,allowCredentials = "false")
public class ContractController {
    private final ContractService contractService;

    @GetMapping("/table-columns")
    public ResponseEntity<?> getTableColumns(){
        return contractService.getTableColumns();
    }

    @GetMapping("/table-data")
    public ResponseEntity<?> getTableData(@RequestParam(value = "reportId", required = false) String reportId,
                                          @RequestParam(value = "query", required = false) String query,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "fields", required = false) String fields,
                                          Pageable pageable){
        return contractService.getTableData(ReportDynamicDto.builder().build());
    }

    @PostMapping("/action")
    public ResponseEntity<?> action(@RequestParam UUID contractId,@RequestParam String action){
        return contractService.action(contractId,action);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getContractsByClientId(@PathVariable UUID clientId){
        return contractService.getByClientId(clientId);
    }

    @PostMapping("/change-tariff/{contractId}")
    public ResponseEntity<?> changeTariff(@PathVariable UUID contractId,@RequestParam Float tariff){
        return contractService.changeTariff(contractId,tariff);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable UUID contractId,@RequestParam UUID canalId,@RequestParam Float tariff){
        return contractService.changeTariff(contractId,tariff);
    }
}

