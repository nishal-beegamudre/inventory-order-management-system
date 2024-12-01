package com.inventoryservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.WarehouseDTO;
import com.inventoryservice.entity.Warehouse;
import com.inventoryservice.repository.WarehouseRepository;
import com.inventoryservice.util.Constants;

@Service
public class WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Cacheable(value="warehouseData", key="#warehouseNumber")
    public WarehouseDTO getWarehouseByNumber(String warehouseNumber) {
        
    	WarehouseDTO warehouseDTO = new WarehouseDTO();
    	Warehouse warehouse = warehouseRepository.findByWarehouseNumber(warehouseDTO.getWarehouseNumber()).orElse(null);
    	
    	if (warehouse != null) {
    		
    		warehouseDTO.setId(warehouse.getId());
    	    warehouseDTO.setName(warehouse.getName());
    	    warehouseDTO.setWarehouseNumber(warehouse.getWarehouseNumber());
    	    warehouseDTO.setIsActive(warehouse.getIsActive());
    	    warehouseDTO.setPinCode(warehouse.getPinCode());
    	    warehouseDTO.setAddress(warehouse.getAddress());
    	    warehouseDTO.setLatitude(warehouse.getLatitude());
    	    warehouseDTO.setLongitude(warehouse.getLongitude());
    	    warehouseDTO.setDistrict(warehouse.getDistrict());
    	    warehouseDTO.setStateId(warehouse.getStateId());
    	    warehouseDTO.setOfficialPhoneNumber(warehouse.getOfficialPhoneNumber());
    	    warehouseDTO.setOfficialEmailId(warehouse.getOfficialEmailId());
    	    warehouseDTO.setPointOfContactName(warehouse.getPointOfContactName());
    	    warehouseDTO.setPointOfContactPhoneNumber(warehouse.getPointOfContactPhoneNumber());
    	    warehouseDTO.setSecondPOC(warehouse.getSecondPOC());
    	    warehouseDTO.setSecondPOCNumber(warehouse.getSecondPOCNumber());
    	    warehouseDTO.setCreationDate(warehouse.getCreationDate().toString());
    	    warehouseDTO.setLastModifiedDate(warehouse.getLastModifiedDate().toString());
    		
    		warehouseDTO.setResponseStatus("200");
            logger.info("Warehouse fetched successfully for number: {}", warehouseDTO.getWarehouseNumber());
    		
    	}else {
    		
    		logger.info("Warehouse not found for number: {}", warehouseDTO.getWarehouseNumber());
            warehouseDTO.setResponseStatus("404");
    		
    	}
    	
    	return warehouseDTO;
    	
    }

    @CachePut(value="warehouseData", key="#warehouseDTO.warehouseNumber")
    public WarehouseDTO updateWarehouse(WarehouseDTO warehouseDTO) {
        
    	Warehouse warehouse = warehouseRepository.findByWarehouseNumber(warehouseDTO.getWarehouseNumber()).orElse(null);

        if (warehouse != null) {
        	

            warehouse.setName((warehouseDTO.getName() != null) ? warehouseDTO.getName() : warehouse.getName());
            warehouse.setWarehouseNumber((warehouseDTO.getWarehouseNumber() != null) ? warehouseDTO.getWarehouseNumber() : warehouse.getWarehouseNumber());
            warehouse.setIsActive((warehouseDTO.getIsActive() != null) ? warehouseDTO.getIsActive() : warehouse.getIsActive());
            warehouse.setPinCode((warehouseDTO.getPinCode() != null) ? warehouseDTO.getPinCode() : warehouse.getPinCode());
            warehouse.setAddress((warehouseDTO.getAddress() != null) ? warehouseDTO.getAddress() : warehouse.getAddress());
            warehouse.setLatitude((warehouseDTO.getLatitude() != null) ? warehouseDTO.getLatitude() : warehouse.getLatitude());
            warehouse.setLongitude((warehouseDTO.getLongitude() != null) ? warehouseDTO.getLongitude() : warehouse.getLongitude());
            warehouse.setDistrict((warehouseDTO.getDistrict() != null) ? warehouseDTO.getDistrict() : warehouse.getDistrict());
            warehouse.setStateId((warehouseDTO.getStateId() != null) ? warehouseDTO.getStateId() : warehouse.getStateId());
            warehouse.setOfficialPhoneNumber((warehouseDTO.getOfficialPhoneNumber() != null) ? warehouseDTO.getOfficialPhoneNumber() : warehouse.getOfficialPhoneNumber());
            warehouse.setOfficialEmailId((warehouseDTO.getOfficialEmailId() != null) ? warehouseDTO.getOfficialEmailId() : warehouse.getOfficialEmailId());
            warehouse.setPointOfContactName((warehouseDTO.getPointOfContactName() != null) ? warehouseDTO.getPointOfContactName() : warehouse.getPointOfContactName());
            warehouse.setPointOfContactPhoneNumber((warehouseDTO.getPointOfContactPhoneNumber() != null) ? warehouseDTO.getPointOfContactPhoneNumber() : warehouse.getPointOfContactPhoneNumber());
            warehouse.setSecondPOC((warehouseDTO.getSecondPOC() != null) ? warehouseDTO.getSecondPOC() : warehouse.getSecondPOC());
            warehouse.setSecondPOCNumber((warehouseDTO.getSecondPOCNumber() != null) ? warehouseDTO.getSecondPOCNumber() : warehouse.getSecondPOCNumber());

            warehouse.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            warehouseRepository.save(warehouse);

            warehouseDTO.setResponseStatus("200");
            logger.info("Warehouse updated successfully for number: {}", warehouseDTO.getWarehouseNumber());

        } else {
        	
            logger.info("Warehouse not found for number: {}", warehouseDTO.getWarehouseNumber());
            warehouseDTO.setResponseStatus("404");
        }

        return warehouseDTO;
    	
    }

    @CachePut(value="warehouseData", key="#warehouseDTO.warehouseNumber")
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        
    	Warehouse existingWarehouse = warehouseRepository.findByWarehouseNumber(warehouseDTO.getWarehouseNumber()).orElse(null);

        if (existingWarehouse == null) {
            
        	Warehouse warehouse = new Warehouse();

        	warehouse.setName(warehouseDTO.getName());
        	warehouse.setWarehouseNumber(warehouseDTO.getWarehouseNumber());
        	warehouse.setIsActive(true);
        	warehouse.setPinCode(warehouseDTO.getPinCode());
        	warehouse.setAddress(warehouseDTO.getAddress());
        	warehouse.setLatitude(warehouseDTO.getLatitude());
        	warehouse.setLongitude(warehouseDTO.getLongitude());
        	warehouse.setDistrict(warehouseDTO.getDistrict());
        	warehouse.setStateId(warehouseDTO.getStateId());
        	warehouse.setOfficialPhoneNumber(warehouseDTO.getOfficialPhoneNumber());
        	warehouse.setOfficialEmailId(warehouseDTO.getOfficialEmailId());
        	warehouse.setPointOfContactName(warehouseDTO.getPointOfContactName());
        	warehouse.setPointOfContactPhoneNumber(warehouseDTO.getPointOfContactPhoneNumber());
        	warehouse.setSecondPOC(warehouseDTO.getSecondPOC());
        	warehouse.setSecondPOCNumber(warehouseDTO.getSecondPOCNumber());
        	warehouse.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	warehouse.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            
            warehouseRepository.save(warehouse);

            
            warehouseDTO.setResponseStatus("200");

            logger.info("Warehouse created successfully with number: {}", warehouseDTO.getWarehouseNumber());

        } else {
            
            logger.info("Warehouse already exists with number: {}", warehouseDTO.getWarehouseNumber());

            warehouseDTO.setResponseStatus("404");
        }

        return warehouseDTO;
    	
    }

}
