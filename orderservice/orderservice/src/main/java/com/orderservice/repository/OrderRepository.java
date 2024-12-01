package com.orderservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

	@Query(value="SELECT order_number FROM orders ORDER BY creation_date DESC LIMIT 1", nativeQuery=true)
	String getLastOrderNumber();

	Optional<Order> findByOrderNumber(String orderNumber);

	@Query(value="SELECT * FROM orders WHERE is_consumed=false", nativeQuery=true)
	List<Order> findAllNonConsumedOrders();
	
}
