package com.orderservice.kafka.consumer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.OrderItemStatusDTO;
import com.orderservice.dto.TransactionDTO;
import com.orderservice.entity.Order;
import com.orderservice.entity.OrderItem;
import com.orderservice.entity.OrderStatusEnum;
import com.orderservice.kafka.producer.KafkaProducer;
import com.orderservice.repository.OrderItemRepository;
import com.orderservice.repository.OrderRepository;
import com.orderservice.util.Constants;
import com.orderservice.util.Utility;

@Service
public class KafkaConsumer {
	
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
	
	@Value("${spring.kafka.topic.consignment.transaction}")
    private String consignmentTransaction; 
	
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
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Autowired
	private Utility utility;
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	private static final String IST = "Asia/Kolkata";
	
	
	@KafkaListener(topics = "${spring.kafka.topic.consignment.transaction}")
	public void consumeVerifyOTPEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Order Service - Topic is "+consignmentTransaction+" and message is "+message);
		
		TransactionDTO transactionDTO = objectMapper.readValue(message, TransactionDTO.class);

		String consignmentStatus = transactionDTO.getPresentStatus().toString();
		String fulfillmentType = transactionDTO.getFulfillmentType().toString();
		Integer statusValue = 0;
		
		List<OrderItem> orderItems = orderItemRepository.
				findOrderItemsByConsignmentNumber(transactionDTO.getConsignmentNumber());
		
		Order order = orderRepository.findByOrderNumber(transactionDTO.getOrderNumber()).orElse(null);
		Boolean isUpdateRequired = false;
		
		List<OrderItem> orderItemsToBeUpdated = new ArrayList<OrderItem>();
				
		if(fulfillmentType=="DELIVERY") {
			
			switch(consignmentStatus) {
			
			case "CREATED":{
				statusValue = 2;
				isUpdateRequired = false;
			};break;
			case "PACKED":{
				statusValue = 3;
				isUpdateRequired = true;
			};break;
			case "LOGISTICS_PICKED":{
				statusValue = 4;
				kafkaProducer.sendMessage(order.getCustomerEmail(), shippedEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "LOGISTICS_DELIVERED":{
				statusValue = 4;
				isUpdateRequired = true;
			};break;
			case "OUT_FOR_DELIVERY":{
				statusValue = 5;
				kafkaProducer.sendMessage(order.getCustomerEmail(), outForDeliveryEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "COMPLETED":{
				statusValue = 6;
				kafkaProducer.sendMessage(order.getCustomerEmail(), deliveredEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "BUSINESS_CANCELLED":{
				statusValue = 15;
				kafkaProducer.sendMessage(order.getCustomerEmail(), orderCancellationEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "USER_CANCELLED":{
				statusValue = 15;
				isUpdateRequired = false;
			};break;
			default: {}
			
			}
			
			
		}else if(fulfillmentType=="PICKUP"){
		
			switch(consignmentStatus) {
			
			case "CREATED":{
				statusValue = 7;
				isUpdateRequired = false;
			};break;
			case "OUT_FOR_PICKUP":{
				statusValue = 11;
				kafkaProducer.sendMessage(order.getCustomerEmail(), outForPickupEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "PICKED_UP":{
				statusValue = 13;
				kafkaProducer.sendMessage(order.getCustomerEmail(), pickedupEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "LOGISTICS_PICKED":{
				statusValue = 13;
				isUpdateRequired = false;
			};break;
			case "LOGISTICS_DELIVERED":{
				statusValue = 13;
				isUpdateRequired = false;
			};break;
			case "COMPLETED":{
				statusValue = 13;
				isUpdateRequired = false;
			};break;
			case "BUSINESS_CANCELLED":{
				statusValue = 15;
				kafkaProducer.sendMessage(order.getCustomerEmail(), pickupRequestCancelledEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = true;
			};break;
			case "USER_CANCELLED":{
				statusValue = 15;
				kafkaProducer.sendMessage(order.getCustomerEmail(), pickupRequestCancelledEmail, 
						transactionDTO.getOrderNumber(), transactionDTO.getConsignmentNumber());
				isUpdateRequired = false;
			};break;
			default: {}
			
			}
			
		}
		
		if(isUpdateRequired) {
		
			for(OrderItem orderItem: orderItems) {
				
				orderItem.setStatus(OrderStatusEnum.fromInt(statusValue));
				orderItem.setStatusLog(transactionDTO.getStatusLog());
				orderItem.setLogisticsCodeName(transactionDTO.getLogisticsCodeName());
				orderItem.setLogisticsTrackingId(transactionDTO.getLogisticsTrackingId());
				orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
				
				orderItemsToBeUpdated.add(orderItem);
				
			}
			
			orderItemRepository.saveAll(orderItemsToBeUpdated);
		
		}
		
		List<Object> listOfObjects = orderItemRepository.findOrderItemsStatusByOrderNumber(order.getOrderNumber());
		
		Integer orderStatus = utility.orderStatusUpdate(statusValue,listOfObjects,order.getStatus().getValue());
				
		if(orderStatus!=0) {
			
			OrderStatusEnum status = OrderStatusEnum.fromInt(orderStatus);
			
			order.setStatus(status);
			order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
			orderRepository.save(order);
			
		}
        
        logger.info("Kafka message consumption ended at order service");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }

}
