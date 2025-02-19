package org.example.waterbilling.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "energy")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Energy {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "total")
    private Float total;

    @ManyToOne
    private Canal canal;

    @Column
    private String type;

    @Column(name = "period")
    private String period;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fixed_at")
    private LocalDateTime fixedAt;
}
