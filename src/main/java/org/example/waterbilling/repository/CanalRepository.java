package org.example.waterbilling.repository;

import org.example.waterbilling.model.entity.Canal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CanalRepository extends JpaRepository<Canal, UUID> {
}
