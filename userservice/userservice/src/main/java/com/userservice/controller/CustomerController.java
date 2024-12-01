package com.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.CustomerDTO;
import com.userservice.service.CustomerService;
import com.userservice.util.Utility;

@RestController
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/user/createCustomer")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) throws JsonProcessingException {
        logger.info("Creating new customer with payload : ", objectMapper.writeValueAsString(customerDTO));
        
        CustomerDTO customer = customerService.createCustomer(customerDTO);
        
        return utility.addResponse(customer,"POST");
    }


    @PutMapping("/user/updateCustomer")
    public CustomerDTO updateCustomer(@RequestBody CustomerDTO customerDTO) throws JsonProcessingException {
    	logger.info("Updating customer with payload : ", objectMapper.writeValueAsString(customerDTO));
        
    	CustomerDTO customer = customerService.updateCustomer(customerDTO);
    	
    	return utility.addResponse(customer,"PUT");
    }


    @GetMapping("/user/getCustomerByPhoneAndEmail")
    public CustomerDTO getCustomerByPhoneAndEmail(@RequestParam("phone") String phone, @RequestParam("email") String email) {
        logger.info("Fetching customer with phone: {} and email: {}", phone, email);
        String phoneNumber = phone.replace(" ", "+");
        CustomerDTO customer = customerService.getCustomerByPhoneAndEmail(phoneNumber, email);
        
        return utility.addResponse(customer,"GET");
    }


    @GetMapping("/user/getCustomerById")
    public CustomerDTO getCustomerById(@RequestParam("id") String id) {
        logger.info("Fetching customer with ID: {}", id);
        
        CustomerDTO customer = customerService.getCustomerById(id);
        logger.info("Reached here");
        return utility.addResponse(customer,"GET");
    }
}
