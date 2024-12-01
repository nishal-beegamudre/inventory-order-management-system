package com.inventoryservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.FetchStockInBulkRequestDTO;
import com.inventoryservice.dto.ListOfListOfStockDTO;
import com.inventoryservice.dto.ListOfStockDTO;
import com.inventoryservice.dto.ListOfStringDTO;
import com.inventoryservice.dto.StockDTO;
import com.inventoryservice.service.StockService;
import com.inventoryservice.util.Utility;

@RestController
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockService stockService;
    
    @Autowired
    private Utility utility;


    @PutMapping("/inventory/updateStock")
    public StockDTO updateStock(@RequestBody StockDTO stockDTO) throws JsonProcessingException {
        logger.info("Updating stock: {}", objectMapper.writeValueAsString(stockDTO));
        
        StockDTO stock = stockService.updateStock(stockDTO);
        
        return utility.addResponse(stock, "PUT");
    }


    @PutMapping("/inventory/updateStockInBulk")
    public ListOfStockDTO updateStockInBulk(@RequestBody ListOfStockDTO listOfStockDTO) throws JsonProcessingException {
        logger.info("Updating stocks in bulk: {}", objectMapper.writeValueAsString(listOfStockDTO));
        
        ListOfStockDTO stockList = stockService.updateStockInBulk(listOfStockDTO);
        
        return utility.addResponse(stockList, "PUT");
    }


    @GetMapping("/inventory/getStocksByWarehouseNumbersAndSKUs")
    public ListOfStockDTO getStocksByWarehouseNumbersAndSKUs(@RequestBody FetchStockInBulkRequestDTO requestDTO) throws JsonProcessingException {
        logger.info("Retrieving stocks for warehouse numbers and SKUs: {}", objectMapper.writeValueAsString(requestDTO));
        
        ListOfStockDTO stockList = stockService.getStocksByWarehouseNumbersAndSKUs(requestDTO);
        
        return utility.addResponse(stockList, "PUT");

    }


    @PutMapping("/inventory/getStocksBySKUs")
    public ListOfStockDTO getStocksBySKUs(@RequestBody ListOfStringDTO listOfSKUs) throws JsonProcessingException {
        logger.info("Retrieving stocks for SKUs: {}", objectMapper.writeValueAsString(listOfSKUs));
        
        ListOfStockDTO ListOfStocks = stockService.getStocksBySKUs(listOfSKUs);
        
        return utility.addResponse(ListOfStocks,"GET");
    }


    @GetMapping("/inventory/getStocksBySKUsAndExcludingGivenWarehouseNumbers")
    public ListOfStockDTO getStocksBySKUsAndExcludingGivenWarehouseNumbers(@RequestBody FetchStockInBulkRequestDTO requestDTO) throws JsonProcessingException {
        logger.info("Retrieving stocks for SKUs excluding certain warehouse numbers: {}", objectMapper.writeValueAsString(requestDTO));
        
        ListOfStockDTO stockList = stockService.getStocksBySKUsAndExcludingGivenWarehouseNumbers(requestDTO);
        
        return utility.addResponse(stockList, "GET");
    }
}
