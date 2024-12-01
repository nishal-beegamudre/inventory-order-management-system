package com.orderservice.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
	
    @Value("${spring.kafka.topic.email.orderconfirmation}")
    private String orderConfirmationEmail; 
	
	@Value("${spring.kafka.topic.email.ordercancellation}")
    private String orderCancellationEmail; 

    @Value("${spring.kafka.topic.email.returnrequestraised}")
    private String returnRequestRaisedEmail; 
    
    @Value("${spring.kafka.topic.email.replacementrequestraised}")
    private String replacementRequestRaisedEmail; 
    
    @Value("${spring.kafka.topic.email.pickuprequestcancelled}")
    private String pickupRequestCancelledEmail; 
    
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String email, String topicName, String orderNumber, String consignmentNumber){		
		
    	try {

    	logger.info("Kafka message is being sent from Order service - Topic is "
    	+topicName+", email is "+email+", order number is "+orderNumber+
    	", consignment number is "+consignmentNumber);

    	String message = email+";"+orderNumber+";";

		kafkaTemplate.send(topicName,message);
		
    	}catch(Exception e) {
    		logger.debug("Exception caught "+e.getMessage());
    	}

    }

}
