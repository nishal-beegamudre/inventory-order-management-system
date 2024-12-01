package com.userservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.DepartmentDTO;
import com.userservice.entity.Customer;
import com.userservice.entity.Department;
import com.userservice.repository.DepartmentRepository;
import com.userservice.util.Constants;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Cacheable(value="departmentData", key="#name")
    public DepartmentDTO getDepartmentByName(String name) {
        
    	Department department = departmentRepository.findByName(name).orElse(null);
    	DepartmentDTO departmentDTO = new DepartmentDTO();
        
    	if(department!=null) {
	
    		departmentDTO.setId(department.getId());
    		departmentDTO.setName(department.getName());
    		departmentDTO.setIsActive(department.getIsActive());
    		departmentDTO.setIsSpecialAccessEnabled(department.getIsSpecialAccessEnabled());
    		departmentDTO.setCreationDate(department.getCreationDate().toString()); 
    		departmentDTO.setLastModifiedDate(department.getLastModifiedDate().toString());

	    	
    		departmentDTO.setResponseStatus("200");
	    	
	    	logger.info("Department fetched successfully for name : {}",departmentDTO.getName());
    	
    	}else {
    		
    		logger.info("Department does not exist for name : {}",departmentDTO.getName());
    		
    		departmentDTO.setResponseStatus("404");
    		
    	}
    	
        return departmentDTO;
    	
    }

    @CachePut(value="departmentData", key="#departmentDTO.name")
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        
    	    Department department = departmentRepository.findByName(departmentDTO.getName()).orElse(null);

    	    if (department != null) {
    	        department.setName(departmentDTO.getName() != null ? departmentDTO.getName() : department.getName());
    	        department.setIsActive(departmentDTO.getIsActive() != null ? departmentDTO.getIsActive() : department.getIsActive());
    	        department.setIsSpecialAccessEnabled(departmentDTO.getIsSpecialAccessEnabled() != null ? departmentDTO.getIsSpecialAccessEnabled() : department.getIsSpecialAccessEnabled());

    	        // Set last modified date to current time
    	        department.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

    	        departmentRepository.save(department);

    	        departmentDTO.setResponseStatus("200");
    	        logger.info("Department updated successfully for ID : {}", departmentDTO.getId());
    	    } else {
    	        logger.info("Department does not exist for ID : {}", departmentDTO.getId());
    	        departmentDTO.setResponseStatus("404");
    	    }

    	    return departmentDTO;

    	
    }

    @CachePut(value="departmentData", key="#departmentDTO.name")
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        
    	Department existingDepartment = departmentRepository.findByName(departmentDTO.getName()).orElse(null);

    	if(existingDepartment == null) {

    	    Department department = new Department();

    	    department.setName(departmentDTO.getName());
    	    department.setIsActive(departmentDTO.getIsActive());
    	    department.setIsSpecialAccessEnabled(departmentDTO.getIsSpecialAccessEnabled());
    	    
    	    // Set ZonedDateTime attributes
    	    department.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    	    department.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

    	    departmentRepository.save(department);

    	    departmentDTO.setResponseStatus("200");

    	    logger.info("Department created successfully for name : {}", departmentDTO.getName());

    	} else {

    	    logger.info("Department already exists for name : {}", departmentDTO.getName());

    	    departmentDTO.setResponseStatus("404");

    	}

    	return departmentDTO;

    	
    }
}
