package com.inventoryservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.ListOfStateDTO;
import com.inventoryservice.dto.StateDTO;
import com.inventoryservice.service.StateService;
import com.inventoryservice.util.Utility;

@RestController
public class StateController {

    private static final Logger logger = LoggerFactory.getLogger(StateController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StateService stateService;

    @Autowired
    private Utility utility;
    
    @GetMapping("/inventory/getStateByPinCode")
    public StateDTO getStateByPinCode(@RequestParam("pincode") String pincode) {
        logger.info("Retrieving state for pincode: {}", pincode);
        
        StateDTO state = stateService.getStateByPinCode(pincode);
        
        return utility.addResponse(state, "GET");
    }


    @PutMapping("/inventory/updateStatesInBulk")
    public ListOfStateDTO updateStatesInBulk(@RequestBody ListOfStateDTO listOfStateDTO) throws JsonProcessingException {
        
    	logger.info("Updating states in bulk: {}", objectMapper.writeValueAsString(listOfStateDTO));
    	
    	ListOfStateDTO stateList = stateService.updateStatesInBulk(listOfStateDTO);
    	
        return utility.addResponse(stateList, "PUT");
    }
}
