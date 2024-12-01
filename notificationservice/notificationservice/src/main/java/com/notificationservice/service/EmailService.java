package com.notificationservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.notificationservice.entity.OTP;
import com.notificationservice.repository.OTPRepository;

@Service
public class EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private OTPRepository otpRepository;
    
    @Value("${spring.email.otp.subject}")
    private String emailOtpSubject;
    
    @Value("${spring.email.otp.body}")
    private String emailOtpBody;
    
    @Value("${spring.email.otp.expirytime}")
    private String otpExpiryTime;
    
    private static final String IST = "Asia/Kolkata";
    
    public void sendEmail(String emailId, String subject, String body) {
    	
    	SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailId);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    	
    	
    }
    
    
    
    public void sendEmailWithOtp(String emailId, String subject, String body) {		 
		
		String otp = generateOTP(emailId);
		String text = body+otp;
		
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailId);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
	
		
	}

	private String generateOTP(String emailId) {
		
		OTP otp = otpRepository.findByEmail(emailId).orElse(null);
		logger.info("Reached here");
		ZonedDateTime presentTime = ZonedDateTime.now(ZoneId.of(IST));
		ZonedDateTime expiryTime = presentTime.plusSeconds(Long.valueOf(otpExpiryTime));
		
		if(otp!=null) {
			otp.setIsUsed(true);
			otp.setLastModifiedDate(presentTime);
			otpRepository.save(otp);	
		}
		
		OTP newOtp = new OTP();
		newOtp.setCreationDate(presentTime);
		newOtp.setExpiryDate(expiryTime);
		newOtp.setIsUsed(false);
		newOtp.setLastModifiedDate(presentTime);
		newOtp.setEmail(emailId);

		Random random = new Random();
        Integer randomNumber = 100000 + random.nextInt(900000);
        
        newOtp.setOtp(randomNumber.toString());
        
        otpRepository.save(newOtp);
		
		return randomNumber.toString();
	}



	public Boolean validateOtp(String emailId, String otp) {
		
		OTP otpEntry = otpRepository.findActiveEntryByEmailAndOtp(emailId,otp).orElse(null);
		
		if(otpEntry==null) {
			return false;
		}else {
			
			ZonedDateTime presentTime = ZonedDateTime.now(ZoneId.of(IST));
			ZonedDateTime expiryTime = otpEntry.getExpiryDate();
			
			if(presentTime.isBefore(expiryTime)) {
				
				otpEntry.setIsUsed(true);
				otpEntry.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(IST)));
				otpRepository.save(otpEntry);
				return true;
				
			}else {
				
				logger.info("OTP is expired for email : "+emailId+" and OTP : "+otp);
				
				return false;
			}
			
			
		}
		
	}


}
