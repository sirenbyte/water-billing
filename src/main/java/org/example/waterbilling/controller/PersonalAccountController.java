package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.service.PersonalAccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/personal-account")
@AllArgsConstructor
@CrossOrigin
public class PersonalAccountController {
    private final PersonalAccountService personalAccountService;

    @GetMapping("/table-columns")
    public ResponseEntity<?> getTableColumns(){
        return personalAccountService.getTableColumns();
    }

    @GetMapping("/table-data")
    public ResponseEntity<?> getTableData(@RequestParam(value = "reportId", required = false) String reportId,
                                          @RequestParam(value = "query", required = false) String query,
                                          @RequestParam(value = "sort", required = false) String sort,
                                          @RequestParam(value = "fields", required = false) String fields,
                                          Pageable pageable){
        return personalAccountService.getTableData(ReportDynamicDto.builder().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id){
        return personalAccountService.getById(id);
    }
}
