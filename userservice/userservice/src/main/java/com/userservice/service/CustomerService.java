package com.userservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.CustomerDTO;
import com.userservice.entity.Customer;
import com.userservice.repository.CustomerRepository;
import com.userservice.util.Constants;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Cacheable(value="customerDataById", key="#id")
    public CustomerDTO getCustomerById(String id) {
        
    	Customer customer = customerRepository.findById(id).orElse(null);
    	CustomerDTO customerDTO = new CustomerDTO();
        
    	if(customer!=null) {
	
    		customerDTO.setId(customer.getId());
    		customerDTO.setName(customer.getName());
    		customerDTO.setPhone(customer.getPhone());
    		customerDTO.setEmail(customer.getEmail());
    		customerDTO.setAddress(customer.getAddress());
    		customerDTO.setPincode(customer.getPincode());
    		customerDTO.setIsActive(customer.getIsActive());
    		customerDTO.setCreationDate(customer.getCreationDate().toString());
    		customerDTO.setLastModifiedDate(customer.getLastModifiedDate().toString());

	    	
	    	customerDTO.setResponseStatus("200");
	    	
	    	logger.info("Customer updated successfully for email : {}",customerDTO.getEmail());
    	
    	}else {
    		
    		logger.info("Customer does not exist for email : {}",customerDTO.getEmail());
    		
    		customerDTO.setResponseStatus("404");
    		
    	}
    	
        return customerDTO;
        
    	
    }

    @Cacheable(value="customerData", key="#email")
    public CustomerDTO getCustomerByPhoneAndEmail(String phone, String email) {

    	Customer customer = customerRepository.findByEmailAndPhone(phone,email).orElse(null);
    	CustomerDTO customerDTO = new CustomerDTO();
        
    	if(customer!=null) {
	
    		customerDTO.setId(customer.getId());
    		customerDTO.setName(customer.getName());
    		customerDTO.setPhone(customer.getPhone());
    		customerDTO.setEmail(customer.getEmail());
    		customerDTO.setAddress(customer.getAddress());
    		customerDTO.setPincode(customer.getPincode());
    		customerDTO.setIsActive(customer.getIsActive());
    		customerDTO.setCreationDate(customer.getCreationDate().toString());
    		customerDTO.setLastModifiedDate(customer.getLastModifiedDate().toString());

	    	
	    	customerDTO.setResponseStatus("200");
	    	
	    	logger.info("Customer updated successfully for email : {}",customerDTO.getEmail());
    	
    	}else {
    		
    		logger.info("Customer does not exist for email : {}",customerDTO.getEmail());
    		
    		customerDTO.setResponseStatus("404");
    		
    	}
    	
        return customerDTO;
    	
    	
    }

    @CachePut(value="customerData", key="#customerDTO.email")
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {

    	Customer customer = customerRepository.findByEmail(customerDTO.getEmail()).orElse(null);
        
    	if(customer!=null) {
	
	    	customer.setName((customerDTO.getName()==null)?customer.getName():customerDTO.getName());
	    	customer.setPhone((customerDTO.getPhone()==null)?customer.getPhone():customerDTO.getPhone());
	    	customer.setEmail((customerDTO.getEmail()==null)?customer.getEmail():customerDTO.getEmail());
	    	customer.setAddress((customerDTO.getAddress()==null)?customer.getAddress():customerDTO.getAddress());
	    	customer.setPincode((customerDTO.getPincode()==null)?customer.getPincode():customerDTO.getPincode());
	    	customer.setIsActive(true);
	
	    	customer.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
	
	    	customerRepository.save(customer);
	    	
	    	customerDTO.setResponseStatus("200");
	    	
	    	logger.info("Customer updated successfully for email : {}",customerDTO.getEmail());
    	
    	}else {
    		
    		logger.info("Customer does not exist for email : {}",customerDTO.getEmail());
    		
    		customerDTO.setResponseStatus("404");
    		
    	}
    	
        return customerDTO;
    	
    	
    }

    @CachePut(value="customerData", key="#customerDTO.email")
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
    	
    	Customer existingCustomer = customerRepository.findByEmail(customerDTO.getEmail()).orElse(null);
        
    	if(existingCustomer==null) {
    	
	       	Customer customer = new Customer();
	
	    	customer.setName(customerDTO.getName());
	    	customer.setPhone(customerDTO.getPhone());
	    	customer.setEmail(customerDTO.getEmail());
	    	customer.setAddress(customerDTO.getAddress());
	    	customer.setPincode(customerDTO.getPincode());
	    	customer.setIsActive(true);
	
	    	customer.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
	    	customer.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
	
	    	customerRepository.save(customer);
	    	
	    	customerDTO.setResponseStatus("200");
	    	
	    	logger.info("Customer created successfully for email : {}",customerDTO.getEmail());
    	
    	}else {
    		
    		logger.info("Customer already exists for email : {}",customerDTO.getEmail());
    		
    		customerDTO.setResponseStatus("404");
    		
    	}
    	
        return customerDTO;
    }
}
