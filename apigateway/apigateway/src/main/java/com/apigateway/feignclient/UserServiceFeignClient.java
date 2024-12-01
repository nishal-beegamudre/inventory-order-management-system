package com.apigateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.apigateway.dto.DepartmentDTO;
import com.apigateway.dto.EmployeeDTO;
import com.apigateway.dto.RoleDTO;

@FeignClient(name = "user-service", url = "http://localhost:8084")
public interface UserServiceFeignClient {
	
	@GetMapping("/user/getRoleByName")
    public RoleDTO getRoleByName(@RequestParam("name") String name);
	
	@GetMapping("/user/getDepartmentByName")
    public DepartmentDTO getDepartmentByName(@RequestParam("name") String name);
	
	@PostMapping("/user/createEmployee")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO);

}
