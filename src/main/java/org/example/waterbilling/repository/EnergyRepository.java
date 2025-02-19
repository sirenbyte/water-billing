package org.example.waterbilling.repository;

import org.example.waterbilling.model.entity.Canal;
import org.example.waterbilling.model.entity.Energy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EnergyRepository extends JpaRepository<Energy, UUID> {
    List<Energy> findAllByCanal_IdAndFixedAtBetween(UUID canalId, LocalDateTime from,LocalDateTime to);
}
