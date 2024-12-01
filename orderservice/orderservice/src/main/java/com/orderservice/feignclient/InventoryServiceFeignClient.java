package com.orderservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.orderservice.dto.ListOfStockDTO;
import com.orderservice.dto.ListOfStringDTO;
import com.orderservice.dto.StateDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryServiceFeignClient {
	
	@PutMapping("/inventory/getStocksBySKUs")
    public ListOfStockDTO getStocksBySKUs(@RequestBody ListOfStringDTO listOfSKUs);
	
	@GetMapping("/inventory/getStateByPinCode")
    public StateDTO getStateByPinCode(@RequestParam("pincode") String pincode);
	
	@PutMapping("/inventory/updateStockInBulk")
    public ListOfStockDTO updateStockInBulk(@RequestBody ListOfStockDTO listOfStockDTO);

}
