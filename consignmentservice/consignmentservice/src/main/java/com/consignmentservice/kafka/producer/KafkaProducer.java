package com.consignmentservice.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.consignmentservice.dto.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaProducer {
	
	@Value("${spring.kafka.topic.email.shipped}")
    private String shippedEmail; 
	
	@Value("${spring.kafka.topic.email.outfordelivery}")
    private String outForDeliveryEmail; 

    @Value("${spring.kafka.topic.email.delivered}")
    private String deliveredEmail; 
    
    @Value("${spring.kafka.topic.email.outforpickup}")
    private String outForPickupEmail; 
    
    @Value("${spring.kafka.topic.email.pickedup}")
    private String pickedupEmail; 
    
    @Autowired
    private ObjectMapper objectMapper;
    
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageToNotificationService(String email, String topicName, String orderNumber, String consignmentNumber){		
		
    	try {

    	logger.info("Kafka message is being sent from Consignment Service - Topic is "
    	+topicName+", email is "+email+", order number is "+orderNumber+
    	", consignment number is "+consignmentNumber);

    	String message = email+";"+orderNumber+";"+consignmentNumber;

		kafkaTemplate.send(topicName,message);
		
    	}catch(Exception e) {
    		logger.debug("Exception caught "+e.getMessage());
    	}

    }
    
    public void sendMessageToOrderService(TransactionDTO transactionDTO, String topicName) {
    	
    	try {
    		
    		String message = objectMapper.writeValueAsString(transactionDTO);

        	logger.info("Kafka message is being sent from Consignment Service - Topic is "
        	+topicName+", payload is "+message);

    		kafkaTemplate.send(topicName,message);
    		
        	}catch(Exception e) {
        		logger.debug("Exception caught "+e.getMessage());
        	}
    	
    	
    }


}

