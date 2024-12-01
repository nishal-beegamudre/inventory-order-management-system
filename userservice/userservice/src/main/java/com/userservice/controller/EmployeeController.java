package com.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.EmployeeDTO;
import com.userservice.service.EmployeeService;
import com.userservice.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/user/createEmployee")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) throws JsonProcessingException {
        
    	logger.info("Creating new employee with payload : ", objectMapper.writeValueAsString(employeeDTO));
    	
    	EmployeeDTO employee = employeeService.createEmployee(employeeDTO);
    	
        return utility.addResponse(employee, "POST");
    }


    @PutMapping("/user/updateEmployee")
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO) throws JsonProcessingException {
    	logger.info("Updating employee with payload : ", objectMapper.writeValueAsString(employeeDTO));
        
    	EmployeeDTO employee = employeeService.updateEmployee(employeeDTO);
    	
    	return utility.addResponse(employee, "PUT");
    }


    @GetMapping("/user/getEmployeeByNumberOrOfficialEmail")
    public EmployeeDTO getEmployeeByNumberOrOfficialEmail(
        @RequestParam("employeeNumber") String employeeNumber, 
        @RequestParam("officialEmail") String officialEmail) {
    	
        logger.info("Fetching employee with employee number: {} or official email: {}", employeeNumber, officialEmail);
        
        EmployeeDTO employee = employeeService.getEmployeeByNumberOrOfficialEmail(employeeNumber, officialEmail);
        
        return utility.addResponse(employee, "GET");
    }
}
