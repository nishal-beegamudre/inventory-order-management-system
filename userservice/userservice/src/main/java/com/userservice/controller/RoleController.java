package com.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.RoleDTO;
import com.userservice.service.RoleService;
import com.userservice.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/user/createRole")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) throws JsonProcessingException {
        
    	logger.info("Creating new role with payload : ", objectMapper.writeValueAsString(roleDTO));
    	
    	RoleDTO role = roleService.createRole(roleDTO);
    	
        return utility.addResponse(role, "POST");
    }


    @PutMapping("/user/updateRole")
    public RoleDTO updateRole(@RequestBody RoleDTO roleDTO) throws JsonProcessingException {
    	
    	logger.info("Updating role with payload : ", objectMapper.writeValueAsString(roleDTO));
        
    	RoleDTO role = roleService.updateRole(roleDTO);
    	
        return utility.addResponse(role, "PUT");
    }

 
    @GetMapping("/user/getRoleByName")
    public RoleDTO getRoleByName(@RequestParam("name") String name) {
    	
        logger.info("Fetching role by name: {}", name);
        
        RoleDTO role = roleService.getRoleByName(name);
        
        return utility.addResponse(role, "GET");
    }
}
