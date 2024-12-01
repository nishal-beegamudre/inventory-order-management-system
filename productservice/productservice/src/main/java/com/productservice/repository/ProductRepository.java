package com.productservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	Optional<Product> findBySku(String sku);
	
	@Query(value = "SELECT * FROM products WHERE sku IN ?1", nativeQuery=true)
	List<Product> findBySkus(List<String> skus);
}
