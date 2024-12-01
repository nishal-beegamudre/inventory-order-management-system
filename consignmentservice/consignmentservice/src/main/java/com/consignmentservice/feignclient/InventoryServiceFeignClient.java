package com.consignmentservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.consignmentservice.dto.StockMovementDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryServiceFeignClient {
	
	@PostMapping("/inventory/createStockMovement")
    public StockMovementDTO createStockMovement(@RequestBody StockMovementDTO stockMovementDTO);

}
