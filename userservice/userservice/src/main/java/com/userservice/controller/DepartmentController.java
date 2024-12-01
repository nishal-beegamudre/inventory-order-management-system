package com.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.DepartmentDTO;
import com.userservice.service.DepartmentService;
import com.userservice.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/user/createDepartment")
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) throws JsonProcessingException {
    	logger.info("Creating new department with payload : ", objectMapper.writeValueAsString(departmentDTO));
        
    	DepartmentDTO department = departmentService.createDepartment(departmentDTO);
    	
    	return utility.addResponse(department, "POST");
    }


    @PutMapping("/user/updateDepartment")
    public DepartmentDTO updateDepartment(@RequestBody DepartmentDTO departmentDTO) throws JsonProcessingException {
    	logger.info("Updating department with payload : ", objectMapper.writeValueAsString(departmentDTO));
        
    	DepartmentDTO department = departmentService.updateDepartment(departmentDTO);
    	
    	return utility.addResponse(department, "PUT");
    }


    @GetMapping("/user/getDepartmentByName")
    public DepartmentDTO getDepartmentByName(@RequestParam("name") String name) {
    	logger.info("Fetching department with name: {}", name);
        
    	DepartmentDTO department = departmentService.getDepartmentByName(name);
    	
    	return utility.addResponse(department, "GET");
    }
}
