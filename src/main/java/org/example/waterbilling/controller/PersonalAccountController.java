package org.example.waterbilling.controller;

import lombok.AllArgsConstructor;
import org.example.waterbilling.model.dto.ReportDynamicDto;
import org.example.waterbilling.service.PersonalAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal-account")
@AllArgsConstructor
public class PersonalAccountController {
    private final PersonalAccountService personalAccountService;

    @GetMapping("/table-columns")
    public ResponseEntity<?> getTableColumns(){
        return personalAccountService.getTableColumns();
    }

    @GetMapping("/table-data")
    public ResponseEntity<?> getTableData(@RequestBody ReportDynamicDto dto){
        return personalAccountService.getTableData(dto);
    }
}
