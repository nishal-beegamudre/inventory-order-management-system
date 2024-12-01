package com.inventoryservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.WarehouseDTO;
import com.inventoryservice.service.WarehouseService;
import com.inventoryservice.util.Utility;

@RestController
public class WarehouseController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WarehouseService warehouseService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/inventory/createWarehouse")
    public WarehouseDTO createWarehouse(@RequestBody WarehouseDTO warehouseDTO) throws JsonProcessingException {
        logger.info("Creating new warehouse: {}", objectMapper.writeValueAsString(warehouseDTO));
        
        WarehouseDTO warehouse = warehouseService.createWarehouse(warehouseDTO);
        
        return utility.addResponse(warehouse, "POST");
    }


    @PutMapping("/inventory/updateWarehouse")
    public WarehouseDTO updateWarehouse(@RequestBody WarehouseDTO warehouseDTO) throws JsonProcessingException {
        logger.info("Updating warehouse: {}", objectMapper.writeValueAsString(warehouseDTO));
        
        WarehouseDTO warehouse = warehouseService.updateWarehouse(warehouseDTO);
        
        return utility.addResponse(warehouse, "PUT");
    }


    @GetMapping("/inventory/getWarehouseByNumber")
    public WarehouseDTO getWarehouseByNumber(@RequestParam("warehouseNumber") String warehouseNumber) {
        logger.info("Fetching warehouse with warehouseNumber: {}", warehouseNumber);
        
        WarehouseDTO warehouse = warehouseService.getWarehouseByNumber(warehouseNumber);
        
        return utility.addResponse(warehouse, "GET");
    }
}
