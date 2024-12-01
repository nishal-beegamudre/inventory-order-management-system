package com.inventoryservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.ListOfPincodeDTO;
import com.inventoryservice.service.PincodeService;
import com.inventoryservice.util.Utility;

@RestController
public class PincodeController {

    private static final Logger logger = LoggerFactory.getLogger(PincodeController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PincodeService pincodeService;
    
    @Autowired
    private Utility utility;

    @PutMapping("/inventory/updatePinCodesInBulk")
    public ListOfPincodeDTO updatePinCodesInBulk(@RequestBody ListOfPincodeDTO listOfPincodeDTO) throws JsonProcessingException {
        logger.info("Updating pin codes in bulk: {}", objectMapper.writeValueAsString(listOfPincodeDTO));
        
        ListOfPincodeDTO pincodeList = pincodeService.updatePinCodesInBulk(listOfPincodeDTO);
        
        return utility.addResponse(pincodeList,"PUT");
    }
}
