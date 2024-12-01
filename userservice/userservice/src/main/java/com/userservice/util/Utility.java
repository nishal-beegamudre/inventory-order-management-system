package com.userservice.util;

import org.springframework.stereotype.Service;

import com.userservice.dto.CustomerDTO;
import com.userservice.dto.DepartmentDTO;
import com.userservice.dto.EmployeeDTO;
import com.userservice.dto.RoleDTO;

@Service
public class Utility {

	public CustomerDTO addResponse(CustomerDTO customer, String requestType) {
		
		switch(requestType) {
		
		case "GET" : {
			
			switch(customer.getResponseStatus()) {
			
			case "200": customer.setResponseMessage(Constants.CUSTOMER_FETCHED_SUCCESSFULLY);break;
			case "404": customer.setResponseMessage(Constants.CUSTOMER_NOT_FOUND);break;
			default: customer.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		
		}
			
			
		};break;
		
		case "POST" : {
			
			switch(customer.getResponseStatus()) {
			
			case "200": customer.setResponseMessage(Constants.CUSTOMER_CREATED_SUCCESSFULLY);break;
			case "404": customer.setResponseMessage(Constants.CUSTOMER_ALREADY_FOUND);break;
			default: customer.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		
		}
			
			
		};break;
		
		case "PUT" : {
			
			switch(customer.getResponseStatus()) {
			
			case "200": customer.setResponseMessage(Constants.CUSTOMER_UPDATED_SUCCESSFULLY);break;
			case "404": customer.setResponseMessage(Constants.CUSTOMER_NOT_FOUND);break;
			default: customer.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		
		}
			
			
		};break;
		
		default : {
			
			customer.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		}
		
		}
		
		return customer;
	}
	
	
	public RoleDTO addResponse(RoleDTO role, String requestType) {
	    
	    switch(requestType) {
	    
	    case "GET" : {
	        switch(role.getResponseStatus()) {
	        
	        case "200": role.setResponseMessage(Constants.ROLE_FETCHED_SUCCESSFULLY);break;
	        case "404": role.setResponseMessage(Constants.ROLE_NOT_FOUND);break;
	        default: role.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    }; break;
	    
	    case "POST" : {
	        switch(role.getResponseStatus()) {
	        
	        case "200": role.setResponseMessage(Constants.ROLE_CREATED_SUCCESSFULLY);break;
	        case "404": role.setResponseMessage(Constants.ROLE_ALREADY_FOUND);break;
	        default: role.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    case "PUT" : {
	        switch(role.getResponseStatus()) {
	        
	        case "200": role.setResponseMessage(Constants.ROLE_UPDATED_SUCCESSFULLY);break;
	        case "404": role.setResponseMessage(Constants.ROLE_NOT_FOUND);break;
	        default: role.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    default : {
	    	role.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }
	    
	    }
	    
	    return role;
	}

	
	public DepartmentDTO addResponse(DepartmentDTO department, String requestType) {
	    
	    switch(requestType) {
	    
	    case "GET" : {
	        switch(department.getResponseStatus()) {
	        
	        case "200": department.setResponseMessage(Constants.DEPARTMENT_FETCHED_SUCCESSFULLY);break;
	        case "404": department.setResponseMessage(Constants.DEPARTMENT_NOT_FOUND);break;
	        default: department.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    case "POST" : {
	        switch(department.getResponseStatus()) {
	        
	        case "200": department.setResponseMessage(Constants.DEPARTMENT_CREATED_SUCCESSFULLY);break;
	        case "404": department.setResponseMessage(Constants.DEPARTMENT_ALREADY_FOUND);break;
	        default: department.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    case "PUT" : {
	        switch(department.getResponseStatus()) {
	        
	        case "200": department.setResponseMessage(Constants.DEPARTMENT_UPDATED_SUCCESSFULLY);break;
	        case "404": department.setResponseMessage(Constants.DEPARTMENT_NOT_FOUND);break;
	        default: department.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    default : {
	    	
	    	department.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    	
	    }
	    
	}
	    
	    return department;
	}

	
	public EmployeeDTO addResponse(EmployeeDTO employee, String requestType) {
	    
	    switch(requestType) {
	    
	    case "GET" : {
	        switch(employee.getResponseStatus()) {
	        
	        case "200": employee.setResponseMessage(Constants.EMPLOYEE_FETCHED_SUCCESSFULLY);break;
	        case "404": employee.setResponseMessage(Constants.EMPLOYEE_NOT_FOUND);break;
	        default: employee.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    case "POST" : {
	        switch(employee.getResponseStatus()) {
	        
	        case "200": employee.setResponseMessage(Constants.EMPLOYEE_CREATED_SUCCESSFULLY);break;
	        case "404": employee.setResponseMessage(Constants.EMPLOYEE_ALREADY_FOUND);break;
	        default: employee.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    case "PUT" : {
	        switch(employee.getResponseStatus()) {
	        
	        case "200": employee.setResponseMessage(Constants.EMPLOYEE_UPDATED_SUCCESSFULLY);break;
	        case "404": employee.setResponseMessage(Constants.EMPLOYEE_NOT_FOUND);break;
	        default: employee.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    default : {
	    	
	    	employee.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    	
	    }
	    
	    }
	    
	    return employee;
	}


}
