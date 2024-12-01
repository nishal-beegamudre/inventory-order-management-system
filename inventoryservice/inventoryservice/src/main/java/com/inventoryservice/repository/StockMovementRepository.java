package com.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.StockMovement;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, String> {

	@Query(value="SELECT * FROM stock_movements WHERE consignment_number=?1", nativeQuery=true)
	public Optional<StockMovement> findByConsignmentNumber(String consignmentNumber);

	@Query(value="SELECT * FROM stock_movements WHERE is_consumed=false", nativeQuery=true)
	public List<StockMovement> findAllNonConsumed();

	@Query(value="SELECT * FROM stock_movements WHERE warehouse_number=?1 AND sku_and_stock=?2 ORDER BY transaction_date DESC LIMIT 1", nativeQuery=true)
	public Optional<StockMovement> findByWarehouseSkuStock(String warehouseNumber, String skuAndStock);
}
