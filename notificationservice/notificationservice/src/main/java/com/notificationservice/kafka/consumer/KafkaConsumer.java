package com.notificationservice.kafka.consumer;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.dto.EmailLogDTO;
import com.notificationservice.entity.EmailLog;
import com.notificationservice.entity.EmailTypeEnum;
import com.notificationservice.repository.EmailLogRepository;
import com.notificationservice.service.EmailService;

@Service
public class KafkaConsumer {
	
	@Value("${spring.kafka.topic.email.verifyotp}")
    private String verifyOTPEmail; 
	
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
    
    @Value("${spring.email.subject.verifyotp}")
    private String verifyOtpSubject;

    @Value("${spring.email.subject.shipped}")
    private String shippedSubject;

    @Value("${spring.email.subject.outfordelivery}")
    private String outForDeliverySubject;

    @Value("${spring.email.subject.delivered}")
    private String deliveredSubject;

    @Value("${spring.email.subject.outforpickup}")
    private String outForPickupSubject;

    @Value("${spring.email.subject.pickedup}")
    private String pickedUpSubject;

    @Value("${spring.email.subject.orderconfirmation}")
    private String orderConfirmationSubject;

    @Value("${spring.email.subject.ordercancellation}")
    private String orderCancellationSubject;

    @Value("${spring.email.subject.returnrequestraised}")
    private String returnRequestRaisedSubject;

    @Value("${spring.email.subject.replacementrequestraised}")
    private String replacementRequestRaisedSubject;

    @Value("${spring.email.subject.pickuprequestcancelled}")
    private String pickupRequestCancelledSubject;

    @Value("${spring.email.body.verifyotp}")
    private String verifyOtpBody;

    @Value("${spring.email.body.shipped}")
    private String shippedBody;

    @Value("${spring.email.body.outfordelivery}")
    private String outForDeliveryBody;

    @Value("${spring.email.body.delivered}")
    private String deliveredBody;

    @Value("${spring.email.body.outforpickup}")
    private String outForPickupBody;

    @Value("${spring.email.body.pickedup}")
    private String pickedUpBody;

    @Value("${spring.email.body.orderconfirmation}")
    private String orderConfirmationBody;

    @Value("${spring.email.body.ordercancellation}")
    private String orderCancellationBody;

    @Value("${spring.email.body.returnrequestraised}")
    private String returnRequestRaisedBody;

    @Value("${spring.email.body.replacementrequestraised}")
    private String replacementRequestRaisedBody;

    @Value("${spring.email.body.pickuprequestcancelled}")
    private String pickupRequestCancelledBody;
    
    @Autowired
    private EmailLogRepository emailLogRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ObjectMapper objectMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	private static final String IST = "Asia/Kolkata";
	
	
	@KafkaListener(topics = "${spring.kafka.topic.email.verifyotp}")
	public void consumeVerifyOTPEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+verifyOTPEmail+" and message is "+message);
		
		String email = message;
		String subject = verifyOtpSubject;
		String body = verifyOtpBody;
		
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.VERIFY_OTP);
		emailLog.setSubject(subject);
		emailLog.setBody(body);
				
		emailService.sendEmailWithOtp(email, subject, body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Verify OTP Email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
	
	
	@KafkaListener(topics = "${spring.kafka.topic.email.shipped}")
	public void consumeShippedEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+shippedEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = shippedSubject+" with Consignment number : "+consignmentNumber;
		String body = shippedBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.SHIPPED);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Shipped email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
	
	@KafkaListener(topics = "${spring.kafka.topic.email.outfordelivery}")
	public void consumeOutForDeliveryEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+outForDeliveryEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = outForDeliverySubject+" with Consignment number : "+consignmentNumber;
		String body = outForDeliveryBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.OUT_FOR_DELIVERY);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Out for delivery email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
	
	@KafkaListener(topics = "${spring.kafka.topic.email.delivered}")
	public void consumeDeliveredEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+deliveredEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = deliveredSubject+" with Consignment number : "+consignmentNumber;
		String body = deliveredBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.DELIVERED);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Delivered email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
	
	@KafkaListener(topics = "${spring.kafka.topic.email.outforpickup}")
	public void consumeOutForPickupEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+outForPickupEmail+" and message is "+message);
		
		EmailLogDTO emailLogDTO = objectMapper.readValue(message, EmailLogDTO.class);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = outForPickupSubject+" with Consignment number : "+consignmentNumber;
		String body = outForPickupBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.OUT_FOR_PICKUP);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Out for pickup email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
	
	@KafkaListener(topics = "${spring.kafka.topic.email.pickedup}")
	public void consumePickedupEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+pickedupEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = pickedUpSubject+" with Consignment number : "+consignmentNumber;
		String body = pickedUpBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.PICKED_UP);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Pickedup email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
    
    @KafkaListener(topics = "${spring.kafka.topic.email.orderconfirmation}")
	public void consumeOrderConfirmationEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+orderConfirmationEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = orderConfirmationSubject+" with Consignment number : "+consignmentNumber;
		String body = orderConfirmationBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.ORDER_CONFIRMATION);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Order confirmation email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
    
    @KafkaListener(topics = "${spring.kafka.topic.email.ordercancellation}")
	public void consumeOrderCancellationEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+orderCancellationEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = orderCancellationSubject+" with Consignment number : "+consignmentNumber;
		String body = orderCancellationBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.ORDER_CANCELLATION);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Order cancellation email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
    
    @KafkaListener(topics = "${spring.kafka.topic.email.returnrequestraised}")
	public void consumeReturnRequestRaisedEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+returnRequestRaisedEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = returnRequestRaisedSubject+" with Consignment number : "+consignmentNumber;
		String body = returnRequestRaisedBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.RETURN_REQUEST_RAISED);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Return request raised email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
    
    @KafkaListener(topics = "${spring.kafka.topic.email.replacementrequestraised}")
	public void consumeReplacementRequestRaisedEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+replacementRequestRaisedEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = replacementRequestRaisedSubject+" with Consignment number : "+consignmentNumber;
		String body = replacementRequestRaisedBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.REPLACEMENT_REQUEST_RAISED);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Replacement request raised email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }
    
    @KafkaListener(topics = "${spring.kafka.topic.email.pickuprequestcancelled}")
	public void consumePickupRequestCancelledEmail(String message){
        
		try {
			
		logger.info("Kafka message is received at Notification Service - Topic is "+pickupRequestCancelledEmail+" and message is "+message);
		
		String[] stringArray = message.split(";");
		String email = stringArray[0];
		String orderNumber = stringArray[1];
		String consignmentNumber = stringArray.length>2?stringArray[2]:" ";
		
		String subject = pickupRequestCancelledSubject+" with Consignment number : "+consignmentNumber;
		String body = pickupRequestCancelledBody+consignmentNumber;
		
		logger.info("email is {}",email);
		logger.info("orderNumber is {}",orderNumber);
		logger.info("consignmentNumber is {}",consignmentNumber);
		logger.info("subject is {}",subject);
		logger.info("body is {}",body);
		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(email);
		emailLog.setConsignmentNumber(consignmentNumber);
		emailLog.setOrderNumber(orderNumber);
		emailLog.setEmailRecipientDateTime(ZonedDateTime.now(ZoneId.of(IST)));
		emailLog.setEmailType(EmailTypeEnum.PICKUP_REQUEST_CANCELLED);
		emailLog.setSubject(subject);
		emailLog.setBody(body);

		emailService.sendEmail(email,subject,body);
		
		emailLogRepository.save(emailLog);
        
        logger.info("Pickup request cancelled email has been triggered for email : ");
        
		}catch(Exception e) {
        	logger.debug("Exception occurred "+e.getMessage());
        }
        
        
    }


}

