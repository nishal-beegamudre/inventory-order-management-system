package com.inventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.Logistics;

@Repository
public interface LogisticsRepository extends JpaRepository<Logistics, String> {

	Optional<Logistics> findByCodeName(String codeName);
}
