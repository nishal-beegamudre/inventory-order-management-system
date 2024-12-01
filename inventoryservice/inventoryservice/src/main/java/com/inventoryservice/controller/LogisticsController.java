package com.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.LogisticsDTO;
import com.inventoryservice.service.LogisticsService;
import com.inventoryservice.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LogisticsController {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogisticsService logisticsService;
    
    @Autowired
    private Utility utility;

    @PostMapping("/inventory/createLogistics")
    public LogisticsDTO createLogistics(@RequestBody LogisticsDTO logisticsDTO) throws JsonProcessingException {
    	
        logger.info("Creating logistics: {}", objectMapper.writeValueAsString(logisticsDTO));
        
        LogisticsDTO logistics = logisticsService.createLogistics(logisticsDTO);
        
        return utility.addResponse(logistics,"POST");
    }


    @PutMapping("/inventory/updateLogistics")
    public LogisticsDTO updateLogistics(@RequestBody LogisticsDTO logisticsDTO) throws JsonProcessingException {
        logger.info("Updating logistics: {}", objectMapper.writeValueAsString(logisticsDTO));
        
        LogisticsDTO logistics = logisticsService.updateLogistics(logisticsDTO);
        
        return utility.addResponse(logistics,"PUT");
    }
    
    @GetMapping("/inventory/getLogisticsByCodeName")
    public LogisticsDTO getLogisticsByCodeName(@RequestParam("codeName") String codeName) {
        logger.info("Getting logistics by codename : {}", codeName);
        
        LogisticsDTO logistics = logisticsService.getLogisticsByCodeName(codeName);
        
        return utility.addResponse(logistics,"GET");
    }
}

