package com.notificationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notificationservice.dto.ValidateOtpDTO;
import com.notificationservice.service.EmailService;


@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@Value("${spring.email.body.verifyotp}")
    private String verifyOtpBody;
	
	@Value("${spring.email.subject.verifyotp}")
    private String verifyOtpSubject;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	
	@PutMapping("/notification/validateOTP")
	public Boolean validateOTP(@RequestBody ValidateOtpDTO validateOtpDTO) {
		
		String email = validateOtpDTO.getEmail();
		String otp = validateOtpDTO.getOtp().toString();
		
		try {
			
			logger.info("Validation of OTP for the email : "+email+" with OTP : "+otp);
			
			Boolean isSuccessful = emailService.validateOtp(email,otp);
			
			logger.info("Validation of OTP for the email : "+email+" with OTP : "+otp + " and Result is : "+isSuccessful);
			
			return isSuccessful;
			
			}catch(Exception e) {
				logger.info("An exception occurred while sending email to "+email +" for OTP. "+e.getMessage());
				return false;
			}
		
	}
	
	@PostMapping("/auth/notification/resendEmail")
    public Boolean resendEmail(@RequestParam("email") String emailId) {
		
		try {
		
		logger.info("An email is scheduled to be triggered for the email : "+emailId+" with OTP.");
		
		emailService.sendEmailWithOtp(emailId,verifyOtpSubject,verifyOtpBody);
		
		logger.info("An email has been successfully triggered for the email : "+emailId+" with OTP.");
		
		return true;
		
		}catch(Exception e) {
			logger.info("An exception occurred while sending email to : "+emailId+" for OTP. "+e.getMessage());
			return false;
		}
		
	}

}

