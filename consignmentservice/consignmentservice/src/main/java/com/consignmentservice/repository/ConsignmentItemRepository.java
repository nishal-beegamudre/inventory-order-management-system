package com.consignmentservice.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.consignmentservice.dto.ConsignmentNumberCountDTO;
import com.consignmentservice.entity.ConsignmentItem;

@Repository
public interface ConsignmentItemRepository extends JpaRepository<ConsignmentItem, String> {

	@Query(value="SELECT * FROM consignment_items WHERE consignment_item_number IN ?1",
			nativeQuery=true)
	public List<ConsignmentItem> findAllConsignmentItemsByItemNumbers
	(List<String> consignmentItemNumbers);
	
	@Query(value="SELECT * FROM consignment_items WHERE order_item_number IN ?1",
			nativeQuery=true)
	public List<ConsignmentItem> findAllConsignmentItemsByOrderItemNumbers
	(List<String> orderItemNumbers);
	
	@Query(value = "SELECT consignment_number AS consignmentNumber, COUNT(*) AS count " +
            "FROM consignment_items " +
            "WHERE consignment_number IN ?1 " +
            "GROUP BY consignment_number", nativeQuery = true)
    public List<Object> fetchConsignmentCountByConsignmentNumbers(Set<String> consignmentNumbers);
	
	@Query(value = "SELECT * FROM consignment_items WHERE consignment_number IN ?1 ORDER BY consignment_number", nativeQuery=true)
	public List<ConsignmentItem> fetchConsignmentItemsByConsignmentNumbers(Set<String> consignmentNumbers);

}
