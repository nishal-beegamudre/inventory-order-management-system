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
import com.userservice.dto.CustomerDTO;
import com.userservice.dto.RoleDTO;
import com.userservice.entity.Customer;
import com.userservice.entity.Role;
import com.userservice.repository.RoleRepository;
import com.userservice.util.Constants;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @CachePut(value="roleData", key="#roleDTO.name")
    public RoleDTO createRole(RoleDTO roleDTO) {
        
    	Role existingRole = roleRepository.findByName(roleDTO.getName()).orElse(null);

    	if(existingRole == null) {

    	    Role role = new Role();

    	    role.setName(roleDTO.getName());
    	    role.setIsActive(roleDTO.getIsActive());
    	    role.setIsSpecialAccessEnabled(roleDTO.getIsSpecialAccessEnabled());
    	    role.setIsAdmin(roleDTO.getIsAdmin());
    	    
    	    // Set ZonedDateTime attributes
    	    role.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    	    role.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

    	    roleRepository.save(role);

    	    roleDTO.setResponseStatus("200");

    	    logger.info("Role created successfully for name : {}", roleDTO.getName());

    	} else {

    	    logger.info("Role already exists for name : {}", roleDTO.getName());

    	    roleDTO.setResponseStatus("404");

    	}

    	return roleDTO;

    	
    }

    @CachePut(value="roleData", key="#roleDTO.name")
    public RoleDTO updateRole(RoleDTO roleDTO) {
        
    	    Role role = roleRepository.findByName(roleDTO.getName()).orElse(null);

    	    if (role != null) {
    	    	
    	        role.setName(roleDTO.getName() != null ? roleDTO.getName() : role.getName());
    	        role.setIsActive(roleDTO.getIsActive() != null ? roleDTO.getIsActive() : role.getIsActive());
    	        role.setIsSpecialAccessEnabled(roleDTO.getIsSpecialAccessEnabled() != null ? roleDTO.getIsSpecialAccessEnabled() : role.getIsSpecialAccessEnabled());
    	        role.setIsAdmin(roleDTO.getIsAdmin() != null ? roleDTO.getIsAdmin() : role.getIsAdmin());

    	        // Set last modified date to current time
    	        role.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

    	        roleRepository.save(role);

    	        roleDTO.setResponseStatus("200");
    	        logger.info("Role updated successfully for ID : {}", roleDTO.getId());
    	    } else {
    	        logger.info("Role does not exist for ID : {}", roleDTO.getId());
    	        roleDTO.setResponseStatus("404");
    	    }

    	    return roleDTO;

    	
    	
    }

    @Cacheable(value="roleData", key="#name")
    public RoleDTO getRoleByName(String name) {
        
    	Role role = roleRepository.findByName(name).orElse(null);
    	RoleDTO roleDTO = new RoleDTO();
        
    	if(role!=null) {
	
    		roleDTO.setId(role.getId());
    		roleDTO.setName(role.getName());
    		roleDTO.setIsActive(role.getIsActive());
    		roleDTO.setIsSpecialAccessEnabled(role.getIsSpecialAccessEnabled());
    		roleDTO.setIsAdmin(role.getIsAdmin());
    		roleDTO.setCreationDate(role.getCreationDate().toString());
    		roleDTO.setLastModifiedDate(role.getLastModifiedDate().toString());

	    	
    		roleDTO.setResponseStatus("200");
	    	
	    	logger.info("Role fetched successfully for name : {}",roleDTO.getName());
    	
    	}else {
    		
    		logger.info("Role does not exist for name : {}",roleDTO.getName());
    		
    		roleDTO.setResponseStatus("404");
    		
    	}
    	
        return roleDTO;
    	
    }
}
