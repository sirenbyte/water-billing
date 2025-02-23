package org.example.waterbilling.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.waterbilling.model.annotation.FiledTitle;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "contract")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    private UUID canalId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @FiledTitle("Дата заявки")
    @Column(name = "fixed_at")
    private LocalDateTime fixedAt;

    @FiledTitle("Статус подачи воды")
    private String waterStatus;

    @FiledTitle("Статус оплаты")
    private String payStatus;

    @FiledTitle("Тариф")
    private String tariff;

    private String value;

    private String price;
}
