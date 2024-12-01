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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.ListOfPincodeDTO;
import com.inventoryservice.dto.PincodeDTO;
import com.inventoryservice.entity.Pincode;
import com.inventoryservice.repository.PincodeRepository; 
import com.inventoryservice.util.Constants;

@Service
public class PincodeService {

    private static final Logger logger = LoggerFactory.getLogger(PincodeService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PincodeRepository pincodeRepository;

    public ListOfPincodeDTO updatePinCodesInBulk(ListOfPincodeDTO listOfPincodeDTO) {
        
    	List<PincodeDTO> pincodeList = listOfPincodeDTO.getListOfPincodes();
    	List<String> pinCodeNumberList = new ArrayList<String>();
    	Map<String,Pincode> idToPincodeMap = new HashMap<String,Pincode>();
    	List<Pincode> pincodesToUpdate = new ArrayList<Pincode>();
    	List<Pincode> pincodesToCreate = new ArrayList<Pincode>();
    	ListOfPincodeDTO listOfPincodeResponseDTO = new ListOfPincodeDTO();
    	
    	for(PincodeDTO pincodeDTO: pincodeList) {
    		pinCodeNumberList.add(pincodeDTO.getPincodeNumber());
    	}
    	
    	List<Pincode> pincodes = pincodeRepository.findPincodesByPincodeNumbers(pinCodeNumberList);
    	
    	for(Pincode pincode: pincodes) {
    		idToPincodeMap.put(pincode.getPincodeNumber(),pincode);
    	}
    	
    	for(PincodeDTO pincodeDTO: pincodeList) {
    		
    		if(idToPincodeMap.get(pincodeDTO.getPincodeNumber())!=null) {
    			
    			Pincode pincode = new Pincode();
    			
    			pincode.setPincodeNumber((pincodeDTO.getPincodeNumber() != null) ? pincodeDTO.getPincodeNumber() : pincode.getPincodeNumber());
    			pincode.setLatitude((pincodeDTO.getLatitude() != null) ? pincodeDTO.getLatitude() : pincode.getLatitude());
    			pincode.setLongitude((pincodeDTO.getLongitude() != null) ? pincodeDTO.getLongitude() : pincode.getLongitude());
    			pincode.setDistrict((pincodeDTO.getDistrict() != null) ? pincodeDTO.getDistrict() : pincode.getDistrict());
    			pincode.setStateId((pincodeDTO.getStateId() != null) ? pincodeDTO.getStateId() : pincode.getStateId());
    			pincode.setIsServed((pincodeDTO.getIsServed() != null) ? pincodeDTO.getIsServed() : pincode.getIsServed());
    			pincode.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    			
    			pincodesToUpdate.add(pincode);
    			
    		}else {
    			
    			Pincode pincode = new Pincode();
    			
    			pincode.setId(pincodeDTO.getId());
    			pincode.setPincodeNumber(pincodeDTO.getPincodeNumber());
    			pincode.setLatitude(pincodeDTO.getLatitude());
    			pincode.setLongitude(pincodeDTO.getLongitude());
    			pincode.setDistrict(pincodeDTO.getDistrict());
    			pincode.setStateId(pincodeDTO.getStateId());
    			pincode.setIsServed(pincodeDTO.getIsServed());

    			pincode.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    			pincode.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    			
    			pincodesToCreate.add(pincode);
    			
    		}
    		
    		pincodeRepository.saveAll(pincodesToUpdate);
    		pincodeRepository.saveAll(pincodesToCreate);
    		
    	}
    	
    	listOfPincodeResponseDTO.setResponseStatus("200");
    	
        logger.info("Updating/Creating pin codes in bulk: {}", listOfPincodeDTO);
        return listOfPincodeResponseDTO;
    }

}
