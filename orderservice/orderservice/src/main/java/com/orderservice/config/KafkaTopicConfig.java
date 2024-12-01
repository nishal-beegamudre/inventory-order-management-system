package com.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

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

    @Bean
    public NewTopic orderConfirmationEmailTopic(){
        return TopicBuilder.name(orderConfirmationEmail)
                .build();
    }
    
    @Bean
    public NewTopic orderCancellationEmailTopic(){
        return TopicBuilder.name(orderCancellationEmail)
                .build();
    }
    
    @Bean
    public NewTopic returnRequestRaisedEmailTopic(){
        return TopicBuilder.name(returnRequestRaisedEmail)
                .build();
    }
    
    @Bean
    public NewTopic replacementRequestRaisedEmailTopic(){
        return TopicBuilder.name(replacementRequestRaisedEmail)
                .build();
    }
    
    @Bean
    public NewTopic pickupRequestCancelledEmailTopic(){
        return TopicBuilder.name(pickupRequestCancelledEmail)
                .build();
    }
    
    @Bean
    public NewTopic shippedEmailTopic(){
        return TopicBuilder.name(shippedEmail)
                .build();
    }
    
    @Bean
    public NewTopic outForDeliveryEmailTopic(){
        return TopicBuilder.name(outForDeliveryEmail)
                .build();
    }
    
    @Bean
    public NewTopic deliveredEmailTopic(){
        return TopicBuilder.name(deliveredEmail)
                .build();
    }
    
    @Bean
    public NewTopic outForPickupEmailTopic(){
        return TopicBuilder.name(outForPickupEmail)
                .build();
    }
    
    @Bean
    public NewTopic pickedupEmailTopic(){
        return TopicBuilder.name(pickedupEmail)
                .build();
    }
    
}

