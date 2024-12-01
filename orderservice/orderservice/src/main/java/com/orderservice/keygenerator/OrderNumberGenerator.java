package com.orderservice.keygenerator;

import org.springframework.stereotype.Service;

@Service
public class OrderNumberGenerator{ 
    
    public String generate(String orderNumber) {
        long nextValue = Long.valueOf(orderNumber);
        nextValue++;
        return String.format("%010d", nextValue); 
    }

}
