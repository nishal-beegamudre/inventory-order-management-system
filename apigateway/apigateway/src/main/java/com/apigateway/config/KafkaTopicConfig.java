package com.apigateway.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.email.verifyotp}")
    private String verifyOTP; 

    @Bean
    public NewTopic verifyOTPTopic(){
        return TopicBuilder.name(verifyOTP)
                .build();
    }
    
}

