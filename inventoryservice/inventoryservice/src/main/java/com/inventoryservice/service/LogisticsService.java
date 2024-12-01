package com.inventoryservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.LogisticsDTO;
import com.inventoryservice.entity.Logistics;
import com.inventoryservice.repository.LogisticsRepository;
import com.inventoryservice.util.Constants;

@Service
public class LogisticsService {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogisticsRepository logisticsRepository;

    @CachePut(value="logisticsData", key="#logisticsDTO.codeName")
    public LogisticsDTO createLogistics(LogisticsDTO logisticsDTO) {
        
    	Logistics existingLogistics = logisticsRepository.findByCodeName(logisticsDTO.getCodeName()).orElse(null);
    	
    	if(existingLogistics==null) {
    		
    		Logistics logistics = new Logistics();
    		
    		logistics.setId(logisticsDTO.getId());
    		logistics.setName(logisticsDTO.getName());
    		logistics.setCodeName(logisticsDTO.getCodeName());
    		logistics.setPointOfContactName(logisticsDTO.getPointOfContactName());
    		logistics.setPointOfContactPhoneNumber(logisticsDTO.getPointOfContactPhoneNumber());
    		logistics.setPointOfContactEmail(logisticsDTO.getPointOfContactEmail());
    		logistics.setIsActive(true);

    		
    		logistics.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    		logistics.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    		
    		logisticsRepository.save(logistics);
    		logisticsDTO.setResponseStatus("200");
    		logger.info("Logistics created successfully for codename: {}", logistics.getCodeName());
    	}else {
    		
    		logger.info("Logistics already found for codename: {}", logisticsDTO.getCodeName());
    		logisticsDTO.setResponseStatus("404");
    		
    	}
    	
        return logisticsDTO;
    }

    @CachePut(value="logisticsData", key="#logisticsDTO.codeName")
    public LogisticsDTO updateLogistics(LogisticsDTO logisticsDTO) {
        
    	Logistics logistics = logisticsRepository.findByCodeName(logisticsDTO.getCodeName()).orElse(null);
    	
    	if(logistics!=null) {
    		
    		logistics.setName((logisticsDTO.getName() != null) ? logisticsDTO.getName() : logistics.getName());
    		logistics.setCodeName((logisticsDTO.getCodeName() != null) ? logisticsDTO.getCodeName() : logistics.getCodeName());
    		logistics.setPointOfContactName((logisticsDTO.getPointOfContactName() != null) ? logisticsDTO.getPointOfContactName() : logistics.getPointOfContactName());
    		logistics.setPointOfContactPhoneNumber((logisticsDTO.getPointOfContactPhoneNumber() != null) ? logisticsDTO.getPointOfContactPhoneNumber() : logistics.getPointOfContactPhoneNumber());
    		logistics.setPointOfContactEmail((logisticsDTO.getPointOfContactEmail() != null) ? logisticsDTO.getPointOfContactEmail() : logistics.getPointOfContactEmail());
    		logistics.setIsActive((logisticsDTO.getIsActive() != null) ? logisticsDTO.getIsActive() : logistics.getIsActive());
    		logistics.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    		
    		logisticsRepository.save(logistics);
    		logisticsDTO.setResponseStatus("200");
    		logger.info("Logistics updated successfully for codename: {}", logistics.getCodeName());
    		
    	}else {
    		
    		logger.info("Logistics not found for codename: {}", logisticsDTO.getCodeName());
    		logisticsDTO.setResponseStatus("404");
    		
    	}
    	
        return logisticsDTO;
    }

    @CachePut(value="logisticsData", key="#codeName")
	public LogisticsDTO getLogisticsByCodeName(String codeName) {
		LogisticsDTO logisticsDTO = new LogisticsDTO();
		Logistics logistics = logisticsRepository.findByCodeName(codeName).orElse(null);
		
		if(logistics!=null) {
			
			logisticsDTO.setId(logistics.getId());
			logisticsDTO.setName(logistics.getName());
			logisticsDTO.setCodeName(logistics.getCodeName());
			logisticsDTO.setPointOfContactName(logistics.getPointOfContactName());
			logisticsDTO.setPointOfContactPhoneNumber(logistics.getPointOfContactPhoneNumber());
			logisticsDTO.setPointOfContactEmail(logistics.getPointOfContactEmail());
			logisticsDTO.setIsActive(logistics.getIsActive());
			logisticsDTO.setCreationDate(logistics.getCreationDate().toString());
			logisticsDTO.setLastModifiedDate(logistics.getLastModifiedDate().toString());
			
			logisticsDTO.setResponseStatus("200");
			logger.info("Logistics fetched successfully for codename: {}", logistics.getCodeName());
    		
    	}else {
    		
    		logger.info("Logistics not found for codename: {}", logisticsDTO.getCodeName());
    		logisticsDTO.setResponseStatus("404");
    		
    	}
		
		return logisticsDTO;
	}

}
