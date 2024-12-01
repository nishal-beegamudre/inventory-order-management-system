package com.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.orderservice.dto.OrderItemStatusDTO;
import com.orderservice.entity.OrderItem;

import jakarta.persistence.SqlResultSetMapping;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

	@Query(value="SELECT * FROM order_items WHERE order_number=?1", nativeQuery=true)
	List<OrderItem> findOrderItemsByOrderNumber(String orderNumber);

	@Query(value="SELECT * FROM order_items WHERE order_item_number IN ?1", nativeQuery=true)
	List<OrderItem> findOrderItemsByItemNumbers(List<String> orderItemNumbers);
	
	@Query(value="SELECT * FROM order_items WHERE order_number=?1 AND status<>?2", nativeQuery=true)
	List<OrderItem> findOrderItemsExcludingGivenStatusByOrderNumber(String orderNumber,String status);
	
	@Query(value="SELECT * FROM order_items WHERE consignment_number=?1", nativeQuery=true)
	List<OrderItem> findOrderItemsByConsignmentNumber(String consignmentNumber);
	
	@Query(value="SELECT status AS status, order_item_number AS orderItemNumber FROM order_items WHERE order_number=?1",
			nativeQuery=true)
	List<Object> findOrderItemsStatusByOrderNumber(String orderNumber);

	@Query(value="SELECT * FROM order_items WHERE order_number=?1 AND status IN ?2",
			nativeQuery=true)
	List<OrderItem> findOrderItemsByOrderNumberAndStatus(String orderNumber, List<String> status);
}
