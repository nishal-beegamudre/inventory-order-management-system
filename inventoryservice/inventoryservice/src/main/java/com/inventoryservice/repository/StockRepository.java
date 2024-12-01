package com.inventoryservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
	
	@Query(value="SELECT * FROM stocks WHERE warehouse_number=?1 AND sku=?2",
			nativeQuery=true)
	public Optional<Stock> findStockByWarehouseNumberAndSku
	(String warehouseNumber, String sku);
	
	@Query(value="SELECT * FROM stocks WHERE name IN ?1",
			nativeQuery=true)
	public List<Stock> findStocksByName
	(Set<String> names);
	
	@Query(value="SELECT * FROM stocks WHERE sku IN ?1 AND warehouse_number IN ?2 ORDER BY warehouse_number,sku",
			nativeQuery=true)
	public List<Stock> findAllStocksBySkusAndWarehouseNumbers
	(List<String> skus, List<String> warehouseNumbers);
	
	@Query(value="SELECT * FROM stocks WHERE sku IN ?1 ORDER BY warehouse_number,sku",
			nativeQuery=true)
	public List<Stock> findAllStocksBySkus
	(List<String> skus);

	
	@Query(value="SELECT * FROM stocks WHERE sku IN ?1 AND warehouse_number NOT IN ?2  ORDER BY warehouse_number,sku",
	nativeQuery=true)
	public List<Stock> findAllStocksBySkusAndExcludingWarehouseNumbers
	(List<String> skus, List<String> warehouseNumbers);
	
}
