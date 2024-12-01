package com.consignmentservice.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.consignmentservice.dto.ConsignmentCountDTO;
import com.consignmentservice.entity.Consignment;

@Repository
public interface ConsignmentRepository extends JpaRepository<Consignment, String> {
	
	@Query(value = "SELECT order_number AS orderNumber, COUNT(*) AS count " +
            "FROM consignments " +
            "WHERE order_number IN ?1 " +
            "GROUP BY order_number", nativeQuery = true)
    public List<Object> fetchConsignmentCountByOrderNumbers(Set<String> orderNumbers);
	
	
	@Query(value = "SELECT * FROM consignments WHERE order_number=?1", nativeQuery=true)
	public List<Consignment> findConsignmentsByOrderNumber(String orderNumber);
	
	@Query(value = "SELECT * FROM consignments WHERE consignment_number IN ?1", nativeQuery=true)
	public List<Consignment> findConsignmentsByConsignmentNumbers(Set<String> consignmentNumbers);
}
