package com.inventoryservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.StockMovementDTO;
import com.inventoryservice.service.StockMovementService;
import com.inventoryservice.util.Utility;

@RestController
public class StockMovementController {

    private static final Logger logger = LoggerFactory.getLogger(StockMovementController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockMovementService stockMovementService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/inventory/createStockMovement")
    public StockMovementDTO createStockMovement(@RequestBody StockMovementDTO stockMovementDTO) throws JsonProcessingException {
        logger.info("Creating new stock movement: {}", objectMapper.writeValueAsString(stockMovementDTO));
        
        StockMovementDTO stockMovement = stockMovementService.createStockMovement(stockMovementDTO);
        
        return utility.addResponse(stockMovement, "POST");
    }


    @PutMapping("/inventory/updateStockMovement")
    public StockMovementDTO updateStockMovement(@RequestBody StockMovementDTO stockMovementDTO) throws JsonProcessingException {
        logger.info("Updating stock movement: {}", objectMapper.writeValueAsString(stockMovementDTO));
        
        StockMovementDTO stockMovement = stockMovementService.updateStockMovement(stockMovementDTO);
        
        return utility.addResponse(stockMovement, "PUT");
    }
    
}
