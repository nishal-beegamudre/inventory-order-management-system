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
import com.userservice.dto.EmployeeDTO;
import com.userservice.entity.Customer;
import com.userservice.entity.Employee;
import com.userservice.repository.EmployeeRepository;
import com.userservice.util.Constants;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Cacheable(value="employeeData", key="#officialEmail")
    public EmployeeDTO getEmployeeByNumberOrOfficialEmail(String employeeNumber, String officialEmail) {
        
    	Employee employee = new Employee();
    	EmployeeDTO employeeDTO = new EmployeeDTO();
    	
    	if(officialEmail!=null) {
    	
    		employee = employeeRepository.findByOfficialEmail(officialEmail).orElse(null);
    	
    	}else {
    		employee = employeeRepository.findByEmployeeNumber(employeeNumber).orElse(null);
    	}
    	
    	if(employee!=null) {
	
    		employeeDTO.setId(employee.getId());
    		employeeDTO.setName(employee.getName());
    		employeeDTO.setEmployeeNumber(employee.getEmployeeNumber());
    		employeeDTO.setRoleName(employee.getRoleName());
    		employeeDTO.setDepartmentName(employee.getDepartmentName());
    		employeeDTO.setPhone(employee.getPhone());
    		employeeDTO.setSecondaryPhone(employee.getSecondaryPhone());
    		employeeDTO.setPersonalEmail(employee.getPersonalEmail());
    		employeeDTO.setOfficialEmail(employee.getOfficialEmail());
    		employeeDTO.setOfficialPhoneNumber(employee.getOfficialPhoneNumber());
    		employeeDTO.setIsActive(employee.getIsActive());
    		employeeDTO.setAddress(employee.getAddress());
    		employeeDTO.setPincode(employee.getPincode());
    		employeeDTO.setWarehouseNumber(employee.getWarehouseNumber());
    		employeeDTO.setJoiningDate(employee.getJoiningDate());
    		employeeDTO.setSalary(employee.getSalary());
    		employeeDTO.setReporterName(employee.getReporterName());
    		employeeDTO.setReporterEmployeeNumber(employee.getReporterEmployeeNumber());
    		employeeDTO.setCreationDate(employee.getCreationDate().toString());
    		employeeDTO.setIsPresentlyWorkingInThisCompany(employee.getIsPresentlyWorkingInThisCompany());
    		employeeDTO.setLastModifiedDate(employee.getLastModifiedDate().toString());

	    	
    		employeeDTO.setResponseStatus("200");
	    	
	    	logger.info("Employee fetched successfully for email : {}",employeeDTO.getOfficialEmail());
    	
    	}else {
    		
    		logger.info("Employee does not exist for given input : {}");
    		
    		employeeDTO.setResponseStatus("404");
    		
    	}
    	
        return employeeDTO;
    	
    	
    }

    @CachePut(value="employeeData", key="#employeeDTO.officialEmail")
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        
    	    Employee employee = employeeRepository.findByOfficialEmail(employeeDTO.getOfficialEmail()).orElse(null);

    	    if (employee != null) {
    	    	
    	    	employee.setName(employeeDTO.getName() != null ? employeeDTO.getName() : employee.getName());
    	        employee.setEmployeeNumber(employeeDTO.getEmployeeNumber() != null ? employeeDTO.getEmployeeNumber() : employee.getEmployeeNumber());
    	        employee.setRoleName(employeeDTO.getRoleName() != null ? employeeDTO.getRoleName() : employee.getRoleName());
    	        employee.setDepartmentName(employeeDTO.getDepartmentName() != null ? employeeDTO.getDepartmentName() : employee.getDepartmentName());
    	        employee.setPhone(employeeDTO.getPhone() != null ? employeeDTO.getPhone() : employee.getPhone());
    	        employee.setSecondaryPhone(employeeDTO.getSecondaryPhone() != null ? employeeDTO.getSecondaryPhone() : employee.getSecondaryPhone());
    	        employee.setPersonalEmail(employeeDTO.getPersonalEmail() != null ? employeeDTO.getPersonalEmail() : employee.getPersonalEmail());
    	        employee.setOfficialEmail(employeeDTO.getOfficialEmail() != null ? employeeDTO.getOfficialEmail() : employee.getOfficialEmail());
    	        employee.setOfficialPhoneNumber(employeeDTO.getOfficialPhoneNumber() != null ? employeeDTO.getOfficialPhoneNumber() : employee.getOfficialPhoneNumber());
    	        employee.setIsActive(employeeDTO.getIsActive() != null ? employeeDTO.getIsActive() : employee.getIsActive());
    	        employee.setAddress(employeeDTO.getAddress() != null ? employeeDTO.getAddress() : employee.getAddress());
    	        employee.setPincode(employeeDTO.getPincode() != null ? employeeDTO.getPincode() : employee.getPincode());
    	        employee.setWarehouseNumber(employeeDTO.getWarehouseNumber() != null ? employeeDTO.getWarehouseNumber() : employee.getWarehouseNumber());
    	        employee.setSalary(employeeDTO.getSalary() != null ? employeeDTO.getSalary() : employee.getSalary());
    	        employee.setReporterName(employeeDTO.getReporterName() != null ? employeeDTO.getReporterName() : employee.getReporterName());
    	        employee.setReporterEmployeeNumber(employeeDTO.getReporterEmployeeNumber() != null ? employeeDTO.getReporterEmployeeNumber() : employee.getReporterEmployeeNumber());
    	        employee.setIsPresentlyWorkingInThisCompany(employeeDTO.getIsPresentlyWorkingInThisCompany() != null ? employeeDTO.getIsPresentlyWorkingInThisCompany() : employee.getIsPresentlyWorkingInThisCompany());

    	        // Set last modified date to current time
    	        employee.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

    	        employeeRepository.save(employee);

    	        employeeDTO.setResponseStatus("200");
    	        logger.info("Employee updated successfully for ID : {}", employeeDTO.getId());
    	    } else {
    	        logger.info("Employee does not exist for ID : {}", employeeDTO.getId());
    	        employeeDTO.setResponseStatus("404");
    	    }

    	    return employeeDTO;

    	
    	
    }

    @CachePut(value="employeeData", key="#employeeDTO.officialEmail")
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        
    	Employee existingEmployee = employeeRepository.findByOfficialEmail(employeeDTO.getOfficialEmail()).orElse(null);

    	if(existingEmployee == null) {

    	    Employee employee = new Employee();

    	    employee.setName(employeeDTO.getName());
    	    employee.setEmployeeNumber(employeeDTO.getEmployeeNumber());
    	    employee.setRoleName(employeeDTO.getRoleName());
    	    employee.setDepartmentName(employeeDTO.getDepartmentName());
    	    employee.setPhone(employeeDTO.getPhone());
    	    employee.setSecondaryPhone(employeeDTO.getSecondaryPhone());
    	    employee.setPersonalEmail(employeeDTO.getPersonalEmail());
    	    employee.setOfficialEmail(employeeDTO.getOfficialEmail());
    	    employee.setOfficialPhoneNumber(employeeDTO.getOfficialPhoneNumber());
    	    employee.setIsActive(true);
    	    employee.setAddress(employeeDTO.getAddress());
    	    employee.setPincode(employeeDTO.getPincode());
    	    employee.setWarehouseNumber(employeeDTO.getWarehouseNumber());
    	    employee.setJoiningDate(employeeDTO.getJoiningDate());
    	    employee.setSalary(employeeDTO.getSalary());
    	    employee.setReporterName(employeeDTO.getReporterName());
    	    employee.setReporterEmployeeNumber(employeeDTO.getReporterEmployeeNumber());
    	    employee.setIsPresentlyWorkingInThisCompany(true);
    	    
    	    // Set ZonedDateTime attributes
    	    employee.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    	    employee.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

    	    employeeRepository.save(employee);

    	    employeeDTO.setResponseStatus("200");

    	    logger.info("Employee created successfully for official email : {}", employeeDTO.getOfficialEmail());

    	} else {

    	    logger.info("Employee already exists for official email : {}", employeeDTO.getOfficialEmail());

    	    employeeDTO.setResponseStatus("404");

    	}

    	return employeeDTO;

    	
    }
}
