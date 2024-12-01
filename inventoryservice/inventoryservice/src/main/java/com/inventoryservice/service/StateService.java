package com.inventoryservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.ListOfStateDTO;
import com.inventoryservice.dto.StateDTO;
import com.inventoryservice.entity.Pincode;
import com.inventoryservice.entity.State;
import com.inventoryservice.repository.PincodeRepository;
import com.inventoryservice.repository.StateRepository;
import com.inventoryservice.util.Constants;

@Service
public class StateService {

    private static final Logger logger = LoggerFactory.getLogger(StateService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StateRepository stateRepository;
    
    @Autowired
    private PincodeRepository pincodeRepository;

    @Cacheable(value="statesData", key="#pincodeNumber")
    public StateDTO getStateByPinCode(String pincodeNumber) {
        
    	StateDTO stateDTO = new StateDTO();
    	Pincode pincode = pincodeRepository.findByPincode(pincodeNumber).orElse(null);
    	
    	if (pincode != null) {
    		
    		State state = stateRepository.findById(pincode.getStateId()).orElse(null);
    		
    		stateDTO.setId(state.getId());
    		stateDTO.setName(state.getName());
    		stateDTO.setFirstWarehouseNumber(state.getFirstWarehouseNumber());
    		stateDTO.setSecondWarehouseNumber(state.getSecondWarehouseNumber());
    		stateDTO.setThirdWarehouseNumber(state.getThirdWarehouseNumber());
    		
    		stateDTO.setResponseStatus("200");
            logger.info("State fetched successfully for pin code number: {}", pincodeNumber);
    		
    	}else {
    		
    		logger.info("Pincode not found for pin code number: {}", pincodeNumber);
    		stateDTO.setResponseStatus("404");
    		
    	}
    	
    	return stateDTO;
    	
    	
    }

    public ListOfStateDTO updateStatesInBulk(ListOfStateDTO listOfStateDTO) {
        
    	List<StateDTO> stateList = listOfStateDTO.getListOfStates();
    	List<String> stateNames = new ArrayList<String>();
    	ListOfStateDTO listOfStates = new ListOfStateDTO();
    	List<StateDTO> stateListOutput = new ArrayList<StateDTO>();
    	Map<String,State> idToStateMap = new HashMap<String,State>();
    	List<String> notFoundStateNames = new ArrayList<String>();
    	List<State> statesToUpdate = new ArrayList<State>();
    	
    	for(StateDTO stateDTO: stateList) {
    		
    		stateNames.add(stateDTO.getName());
    		
    	}
    	
    	List<State> states = stateRepository.findByNames(stateNames);
    	
    	for(State state: states) {
    		idToStateMap.put(state.getName(), state);
    	}
    	
    	for(StateDTO stateDTO: stateList) {
    		
    		if(idToStateMap.get(stateDTO.getName())!=null) {
    			
    			State state = new State();
    			
    			state.setName((stateDTO.getName() != null) ? stateDTO.getName() : state.getName());
    			state.setFirstWarehouseNumber((stateDTO.getFirstWarehouseNumber() != null) ? stateDTO.getFirstWarehouseNumber() : state.getFirstWarehouseNumber());
    			state.setSecondWarehouseNumber((stateDTO.getSecondWarehouseNumber() != null) ? stateDTO.getSecondWarehouseNumber() : state.getSecondWarehouseNumber());
    			state.setThirdWarehouseNumber((stateDTO.getThirdWarehouseNumber() != null) ? stateDTO.getThirdWarehouseNumber() : state.getThirdWarehouseNumber());
    			state.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST))); // Set lastModifiedDate to current time in IST
    			
    			statesToUpdate.add(state);
    			
    		}else {
    			
    			State state = new State();
    			
    			state.setName((stateDTO.getName() != null) ? stateDTO.getName() : state.getName());
    			state.setFirstWarehouseNumber((stateDTO.getFirstWarehouseNumber() != null) ? stateDTO.getFirstWarehouseNumber() : state.getFirstWarehouseNumber());
    			state.setSecondWarehouseNumber((stateDTO.getSecondWarehouseNumber() != null) ? stateDTO.getSecondWarehouseNumber() : state.getSecondWarehouseNumber());
    			state.setThirdWarehouseNumber((stateDTO.getThirdWarehouseNumber() != null) ? stateDTO.getThirdWarehouseNumber() : state.getThirdWarehouseNumber());
    			state.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST))); // Set lastModifiedDate to current time in IST
    			
    			statesToUpdate.add(state);
    			
    			notFoundStateNames.add(stateDTO.getName());
    		}
    		
    	}
    	
    	stateRepository.saveAll(statesToUpdate);
    	
    	listOfStates.setResponseStatus("200");
    	logger.info("Update is successful, but the following states are not found : {} ",notFoundStateNames);
    	
    	return listOfStates;
    	
    }

}
