package org.example.waterbilling.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractDto {
    private String value;
    private LocalDateTime fixedAt;
}
