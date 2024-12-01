package com.orderservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.orderservice.dto.ConsignmentResponseDTO;
import com.orderservice.dto.ListOfConsignmentDTO;

@FeignClient(name = "consignment-service", url = "http://localhost:8086")
public interface ConsignmentServiceFeignClient {
	
	@PutMapping("/consignment/cancelConsignmentsByOrderNumber")
    public ConsignmentResponseDTO cancelConsignmentsByOrderNumber(@RequestParam("orderNumber") String orderNumber);

	@PostMapping("/consignment/createConsignmentsInBulk")
    public ListOfConsignmentDTO createConsignmentsInBulk(@RequestBody ListOfConsignmentDTO consignmentDTOList);

}
