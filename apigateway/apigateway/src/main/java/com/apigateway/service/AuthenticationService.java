package com.apigateway.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.apigateway.config.TokenProvider;
import com.apigateway.dto.DepartmentDTO;
import com.apigateway.dto.EmployeeDTO;
import com.apigateway.dto.LoginUserDTO;
import com.apigateway.dto.RegisterUserDTO;
import com.apigateway.dto.RoleDTO;
import com.apigateway.dto.SessionEntryDTO;
import com.apigateway.dto.SignupResponseDTO;
import com.apigateway.dto.UserDTO;
import com.apigateway.dto.ValidateOtpDTO;
import com.apigateway.entity.User;
import com.apigateway.feignclient.NotificationServiceFeignClient;
import com.apigateway.feignclient.UserServiceFeignClient;
import com.apigateway.kafka.producer.KafkaProducer;
import com.apigateway.repository.UserRepository;
import com.apigateway.util.Constants;
import com.apigateway.util.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
    
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceFeignClient userServiceFeignClient;
	
	@Autowired
	private NotificationServiceFeignClient notificationServiceFeignClient;
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Autowired
    private ReactiveUserDetailsService userDetailsService;
    
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private Utility utility;



    
    public SignupResponseDTO signup(RegisterUserDTO input){
    	
    	try {
    	
    		
    		User existingUser = userRepository.findByEmail(input.getOfficialEmail()).orElse(null);
    		RoleDTO role = userServiceFeignClient.getRoleByName(input.getRoleName());
    		DepartmentDTO department = userServiceFeignClient.getDepartmentByName(input.getDepartmentName());
        	if(existingUser==null) {
        		
        		if(role==null) {
	        		return new SignupResponseDTO(null,null,null,null,null,Constants.INVALID_ROLE);
        		}
        		if(department==null) {
        			return new SignupResponseDTO(null,null,null,null,null,Constants.INVALID_DEPARTMENT);
        		}
        		
        		User user = new User();
        		
                user.setName(input.getName());
                user.setEmail(input.getOfficialEmail());
                user.setUsername(input.getOfficialEmail());
                
                String password = passwordEncoder.encode(input.getPassword());
                user.setPassword(password);
                
                user.setIsValidated(false);
                
                EmployeeDTO employeeDTO = new EmployeeDTO(null,input.getEmployeeNumber(),input.getName(),input.getRoleName(),
                		input.getDepartmentName(),input.getPhone(),input.getSecondaryPhone(),input.getPersonalEmail(),
                		input.getOfficialEmail(),input.getOfficialPhoneNumber(),input.getIsActive(),input.getAddress(),
                		input.getPincode(),input.getWarehouseNumber(),input.getJoiningDate(),input.getSalary(),
                		input.getReporterName(),input.getReporterEmployeeNumber(),null,true,null,null,null);
                
                logger.info("Sending internal request to user service to create user for email ID : "+input.getOfficialEmail()
                +" and payload is : "+objectMapper.writeValueAsString(employeeDTO));
                EmployeeDTO newEmployee = userServiceFeignClient.createEmployee(employeeDTO);
                logger.info("User Creation for email : "+input.getOfficialEmail()+" and Result : "+newEmployee.getResponseStatus());
                
                if(newEmployee.getResponseStatus().equals("200")) {
                	
                	logger.info("Sending internal request to email service to send email with OTP to the email ID : "+
                			input.getOfficialEmail());
                	kafkaProducer.sendMessage(input.getOfficialEmail());
                	logger.info("Email OTP for email : "+input.getOfficialEmail()+" and Result : "+newEmployee.getResponseStatus());
                	
                	
                		
                		userRepository.save(user);
                        return new SignupResponseDTO(null,null,null,null,null,Constants.USER_CREATED_SUCCESSFULLY);
                		
                	
                }else {
                	return new SignupResponseDTO(null,null,null,null,null,Constants.ERROR_WHILE_CREATING_USER);
                }	
        	}
        	else {
        		return new SignupResponseDTO(null,null,null,null,null,Constants.USER_EXISTS_ALREADY);
        	}	
    	
    	}catch(Exception e) {
    		logger.debug("Exception occured : "+e.getMessage());
    		return new SignupResponseDTO(null,null,null,null,null,e.getMessage());
    	}
    	
        
    }

    
    public Mono<LoginResponse> authenticate(LoginUserDTO input) {

    	User user = userRepository.findByEmail(input.getEmail()).orElse(null);
    	if((user!=null)&&(passwordEncoder.encode(input.getPassword()).equals(user.getPassword()))){
    		
    		
    	}
    	StringBuilder stringBuilder = new StringBuilder();
    	Mono<UserDetails> userdetails123 = userDetailsService.findByUsername(input.getEmail())
    			.filter(u -> { stringBuilder.append("Picking for user with email "+u.getUsername()+" and password "
    			    	+u.getPassword()+" authorities "+u.getAuthorities());
    				return passwordEncoder.matches(input.getPassword(), u.getPassword()); });

    	logger.info("Now heading for authentication login");
    	logger.info("String builder data is "+stringBuilder.toString());
    	return userDetailsService.findByUsername(input.getEmail())
                .filter(u -> {
                	logger.info("Picking for user with email "+u.getUsername()+" and password "
    			    	+u.getPassword()+" authorities "+u.getAuthorities());
                	return passwordEncoder.matches(input.getPassword(), u.getPassword());
                	
                })

                .map(tokenProvider::generateToken)
                .map(LoginResponse::new)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    	
    }


	public Boolean verifyOTP(ValidateOtpDTO validateOtpDTO) {
		
		logger.info("Sending internal request to email service with email ID: "+validateOtpDTO.getEmail()+" and OTP : "+validateOtpDTO.getOtp());
		Boolean isValidationSuccessful = notificationServiceFeignClient.validateOTP(validateOtpDTO);
		logger.info("OTP Validation for email : "+validateOtpDTO.getEmail()+" and Result : "+isValidationSuccessful);
		User user = userRepository.findByEmailForNonValidatedUsers(validateOtpDTO.getEmail()).orElse(null);
		
		if(isValidationSuccessful) {
			
			user.setIsValidated(true);
			userRepository.save(user);

			return true;
				
		}	
			return false;
	}

}