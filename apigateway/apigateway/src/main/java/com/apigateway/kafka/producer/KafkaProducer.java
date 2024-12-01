package com.apigateway.kafka.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
	
	@Value("${spring.kafka.topic.email.verifyotp}")
    private String verifyOTPEmail; 
    
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String email){		
		
    	try {

    	logger.info("Kafka message is being sent from API Gateway Service - Topic is "+verifyOTPEmail+" and email is "+email);


		kafkaTemplate.send(verifyOTPEmail,email);
		
    	}catch(Exception e) {
    		logger.debug("Exception caught "+e.getMessage());
    	}

    }


}
