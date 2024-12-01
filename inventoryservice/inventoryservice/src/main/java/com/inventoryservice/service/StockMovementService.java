package com.inventoryservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import com.inventoryservice.dto.StockMovementDTO;
import com.inventoryservice.entity.StockMovement;
import com.inventoryservice.entity.Warehouse;
import com.inventoryservice.repository.StockMovementRepository; 
import com.inventoryservice.util.Constants;

@Service
public class StockMovementService {

    private static final Logger logger = LoggerFactory.getLogger(StockMovementService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockMovementRepository stockMovementRepository; 

    public StockMovementDTO createStockMovement(StockMovementDTO stockMovementDTO) {


            
        	StockMovement stockMovement = new StockMovement();

        	stockMovement.setWarehouseNumber(stockMovementDTO.getWarehouseNumber());
        	stockMovement.setSkuAndStock(stockMovementDTO.getSkuAndStock());
        	stockMovement.setReceivedByEmployeeNumber(stockMovementDTO.getReceivedByEmployeeNumber());
        	stockMovement.setMovementType(stockMovementDTO.getMovementType());
        	stockMovement.setReason(stockMovementDTO.getReason());
        	stockMovement.setConsignmentNumber(stockMovementDTO.getConsignmentNumber());
        	stockMovement.setLogisticsCodeName(stockMovementDTO.getLogisticsCodeName());
        	stockMovement.setLogisticsDelivererName(stockMovementDTO.getLogisticsDelivererName());
        	stockMovement.setLogisticsDelivererNumber(stockMovementDTO.getLogisticsDelivererNumber());
        	stockMovement.setStatus(stockMovementDTO.getStatus());
        	stockMovement.setIsConsumed(stockMovementDTO.getIsConsumed());
        	stockMovement.setTransactionDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	stockMovement.setCompletionDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            
        	stockMovementRepository.save(stockMovement);

            StockMovement movement = stockMovementRepository.findByWarehouseSkuStock
            		(stockMovementDTO.getWarehouseNumber(),stockMovementDTO.getSkuAndStock()).orElse(null);
        	
            stockMovementDTO.setId(movement!=null?movement.getId():"");
        	stockMovementDTO.setResponseStatus("200");

            logger.info("StockMovement created successfully with warehouse number: {} and SKU_AND_Stocks : {}", stockMovementDTO.getWarehouseNumber(), stockMovementDTO.getSkuAndStock());


        return stockMovementDTO;
    	
    }

    public StockMovementDTO updateStockMovement(StockMovementDTO stockMovementDTO) {
        
    	StockMovement stockMovement = stockMovementRepository.findById(stockMovementDTO.getId()).orElse(null);
    	
    	if(stockMovement!=null) {
    		
    	    stockMovement.setWarehouseNumber((stockMovementDTO.getWarehouseNumber() != null) ? stockMovementDTO.getWarehouseNumber() : stockMovement.getWarehouseNumber());
    	    stockMovement.setSkuAndStock((stockMovementDTO.getSkuAndStock() != null) ? stockMovementDTO.getSkuAndStock() : stockMovement.getSkuAndStock());
    	    stockMovement.setReceivedByEmployeeNumber((stockMovementDTO.getReceivedByEmployeeNumber() != null) ? stockMovementDTO.getReceivedByEmployeeNumber() : stockMovement.getReceivedByEmployeeNumber());
    	    stockMovement.setMovementType((stockMovementDTO.getMovementType() != null) ? stockMovementDTO.getMovementType() : stockMovement.getMovementType());
    	    stockMovement.setReason((stockMovementDTO.getReason() != null) ? stockMovementDTO.getReason() : stockMovement.getReason());
    	    stockMovement.setConsignmentNumber((stockMovementDTO.getConsignmentNumber() != null) ? stockMovementDTO.getConsignmentNumber() : stockMovement.getConsignmentNumber());
    	    stockMovement.setStatus(stockMovementDTO.getStatus()!=null?stockMovementDTO.getStatus():stockMovement.getStatus());
    	    stockMovement.setLogisticsCodeName((stockMovementDTO.getLogisticsCodeName() != null) ? stockMovementDTO.getLogisticsCodeName() : stockMovement.getLogisticsCodeName());
    	    stockMovement.setLogisticsDelivererName((stockMovementDTO.getLogisticsDelivererName() != null) ? stockMovementDTO.getLogisticsDelivererName() : stockMovement.getLogisticsDelivererName());
    	    stockMovement.setLogisticsDelivererNumber((stockMovementDTO.getLogisticsDelivererNumber() != null) ? stockMovementDTO.getLogisticsDelivererNumber() : stockMovement.getLogisticsDelivererNumber());
    	    stockMovement.setIsConsumed((stockMovementDTO.getIsConsumed() != null) ? stockMovementDTO.getIsConsumed() : stockMovement.getIsConsumed());

    	  
    	    //stockMovement.setCompletionDate(stockMovementDTO.getCompletionDate()!=null?stockMovementDTO.getCompletionDate():stockMovement.getCompletionDate());
    		
    		stockMovementDTO.setResponseStatus("200");

            logger.info("StockMovement updated successfully with warehouse number: {} and SKU_AND_Stocks : {}", stockMovementDTO.getWarehouseNumber(), stockMovementDTO.getSkuAndStock());
    		
    		
    	}else {
    		
    		stockMovementDTO.setResponseStatus("404");

            logger.info("StockMovement not found for ID: {} ", stockMovementDTO.getId());
    		
    	}
    	
    	return stockMovementDTO;
    	
    }

}
