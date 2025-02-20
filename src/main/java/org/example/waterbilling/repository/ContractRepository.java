package org.example.waterbilling.repository;

import org.example.waterbilling.model.entity.Canal;
import org.example.waterbilling.model.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> getContractsByUserId(UUID userId);
}
