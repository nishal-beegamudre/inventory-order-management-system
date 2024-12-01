package com.inventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {

	Optional<Warehouse> findByWarehouseNumber(String warehouseNumber);
}
