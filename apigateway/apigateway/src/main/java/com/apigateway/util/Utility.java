package com.apigateway.util;

import java.util.Objects;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.apigateway.dto.LoginUserDTO;
import com.apigateway.dto.SignupResponseDTO;
import com.apigateway.entity.User;

@Service
public class Utility {
	
Logger logger = LoggerFactory.getLogger(Utility.class);
	
	
	public Boolean isNumerical(String userId) {
	
		Pattern pattern = Pattern.compile(Constants.REGEX_WHOLE_NUMBER);
		return pattern.matcher(userId).matches();
	}
	
	public SignupResponseDTO userPopulator(User user) {
		
		SignupResponseDTO signupResponseDTO = new SignupResponseDTO();
		
		if(Objects.nonNull(user)) {
			
			signupResponseDTO.setEmail
			(Objects.nonNull(user.getEmail())?user.getEmail():null);
			signupResponseDTO.setId
			(Objects.nonNull(user.getId())?user.getId():null);
			signupResponseDTO.setName
			(Objects.nonNull(user.getName())?user.getName():null);
		}
		
		return signupResponseDTO;
		
	}

}
