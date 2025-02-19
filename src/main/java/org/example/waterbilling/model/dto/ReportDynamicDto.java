package org.example.waterbilling.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Builder
@Data
public class ReportDynamicDto {
    private String reportId;
    private String query;
    private String sort;
    private String fields;
    private Pageable pageable;
}
