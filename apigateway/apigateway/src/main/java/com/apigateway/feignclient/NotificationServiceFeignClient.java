package com.apigateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.apigateway.dto.ValidateOtpDTO;

@FeignClient(name = "notification-service", url = "http://localhost:8087")
public interface NotificationServiceFeignClient {
	
	@PutMapping("/notification/validateOTP")
	public Boolean validateOTP(@RequestBody ValidateOtpDTO validateOtpDTO);

	@PostMapping("/auth/notification/resendEmail")
    public Boolean resendEmail(@RequestParam("email") String emailId);

}
