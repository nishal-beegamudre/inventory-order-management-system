package com.consignmentservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.consignmentservice.dto.ConsignmentResponseDTO;
import com.consignmentservice.dto.ListOfTransactionDTO;
import com.consignmentservice.dto.TransactionDTO;
import com.consignmentservice.entity.Consignment;
import com.consignmentservice.entity.ConsignmentItem;
import com.consignmentservice.entity.ConsignmentStatusEnum;
import com.consignmentservice.entity.Transaction;
import com.consignmentservice.kafka.producer.KafkaProducer;
import com.consignmentservice.repository.ConsignmentItemRepository;
import com.consignmentservice.repository.ConsignmentRepository;
import com.consignmentservice.repository.TransactionRepository;
import com.consignmentservice.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${spring.kafka.topic.consignment.transaction}")
    private String consignmentTransaction;
    
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private ConsignmentRepository consignmentRepository;
    
    @Autowired
    private ConsignmentItemRepository consignmentItemRepository;

    public ConsignmentResponseDTO createTransaction(TransactionDTO transactionDTO) {
        
    	ConsignmentResponseDTO responseDTO = new ConsignmentResponseDTO();
        logger.info("Creating transaction: {}", transactionDTO);
        
        Set<String> consignmentNumbers = new HashSet<String>();
        consignmentNumbers.add(transactionDTO.getConsignmentNumber());
        
        List<Consignment> consignmentList = consignmentRepository.findConsignmentsByConsignmentNumbers(consignmentNumbers);
        List<ConsignmentItem> consignmentItemList = consignmentItemRepository.fetchConsignmentItemsByConsignmentNumbers(consignmentNumbers);
        List<ConsignmentItem> consignmentItemsToBeUpdated = new ArrayList<ConsignmentItem>();
        
        if(consignmentList.size()!=0) {
        	
        	Consignment consignment = consignmentList.get(0);
        	
            consignment.setStatus(transactionDTO.getPresentStatus() != null ? transactionDTO.getPresentStatus() : consignment.getStatus());
            consignment.setStatusLog(transactionDTO.getStatusLog() != null ? transactionDTO.getStatusLog() : consignment.getStatusLog());
            consignment.setLogisticsCodeName(transactionDTO.getLogisticsCodeName() != null ? transactionDTO.getLogisticsCodeName() : consignment.getLogisticsCodeName());
            consignment.setLogisticsTrackingId(transactionDTO.getLogisticsTrackingId() != null ? transactionDTO.getLogisticsTrackingId() : consignment.getLogisticsTrackingId());

            consignment.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
            
        
        for(ConsignmentItem consignmentItem: consignmentItemList) {
        	
            consignmentItem.setStatus(transactionDTO.getPresentStatus() != null ? transactionDTO.getPresentStatus() : consignmentItem.getStatus());
            consignmentItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
            consignmentItemsToBeUpdated.add(consignmentItem);
        }
        
        Transaction transaction = new Transaction();
        
        transaction.setConsignmentNumber(transactionDTO.getConsignmentNumber());
        transaction.setOrderNumber(transactionDTO.getOrderNumber());
        transaction.setPreviousStatus(transactionDTO.getPreviousStatus());
        transaction.setLogisticsCodeName((transactionDTO.getLogisticsCodeName()!=null)?transactionDTO.getLogisticsCodeName():"");
        transaction.setLogisticsTrackingId((transactionDTO.getLogisticsTrackingId()!=null)?transactionDTO.getLogisticsTrackingId():"");
        transaction.setPresentStatus(transactionDTO.getPresentStatus());
        transaction.setTransactionDateTime(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        transaction.setStatusLog((transactionDTO.getStatusLog()!=null)?transactionDTO.getStatusLog():"");
        transaction.setFulfillmentType(transactionDTO.getFulfillmentType());
        
        
        if(transaction.getPresentStatus()!=ConsignmentStatusEnum.CREATED) {
        	kafkaProducer.sendMessageToOrderService(transactionDTO, consignmentTransaction);
        }
        
        consignmentRepository.save(consignment);
        consignmentItemRepository.saveAll(consignmentItemsToBeUpdated);
        transactionRepository.save(transaction);
        
        logger.info("Transaction created successfully for consignment number {}, order number{}, with status {}",transactionDTO.getConsignmentNumber(),
        		transactionDTO.getOrderNumber(),transactionDTO.getPresentStatus());
        
        responseDTO.setResponseMessage(Constants.TRANSACTION_CREATED_SUCCESSFULLY);
        responseDTO.setResponseStatus("200");
        
        }else {
        	
        	responseDTO.setResponseMessage(Constants.INVALID_CONSIGNMENT_NUMBER);
            responseDTO.setResponseStatus("404");
        	
        }
 
        
        return responseDTO;
    }

	public ConsignmentResponseDTO createTransactionsInBulk(ListOfTransactionDTO listOfTransactionDTO) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		List<TransactionDTO> listOfTransactions = listOfTransactionDTO.getListOfTransactions();
		
		for(TransactionDTO transactionDTO: listOfTransactions) {
			
			Transaction transaction = new Transaction();
	        
	        transaction.setConsignmentNumber(transactionDTO.getConsignmentNumber());
	        transaction.setOrderNumber(transactionDTO.getOrderNumber());
	        transaction.setPreviousStatus(transactionDTO.getPreviousStatus());
	        transaction.setLogisticsCodeName((transactionDTO.getLogisticsCodeName()!=null)?transactionDTO.getLogisticsCodeName():"");
	        transaction.setLogisticsTrackingId((transactionDTO.getLogisticsTrackingId()!=null)?transactionDTO.getLogisticsTrackingId():"");
	        transaction.setPresentStatus(transactionDTO.getPresentStatus());
	        transaction.setTransactionDateTime(ZonedDateTime.now(ZoneId.of(Constants.IST)));
	        transaction.setStatusLog((transactionDTO.getStatusLog()!=null)?transactionDTO.getStatusLog():transaction.getStatusLog());
	        transaction.setFulfillmentType((transactionDTO.getFulfillmentType()!=null)?transactionDTO.getFulfillmentType():transaction.getFulfillmentType());
			
	        logger.info("Transaction is being created for consignment number {}, order number{}, with status {}",transactionDTO.getConsignmentNumber(),
	        		transactionDTO.getOrderNumber(),transactionDTO.getPresentStatus());
	        
	        if(transaction.getPresentStatus()!=ConsignmentStatusEnum.CREATED) {
	        	kafkaProducer.sendMessageToOrderService(transactionDTO, consignmentTransaction);
	        }
	        
	        transactionList.add(transaction);
		}
		
		transactionRepository.saveAll(transactionList);
		
		logger.info("All transactions are created successfully");
		
		ConsignmentResponseDTO responseDTO = new ConsignmentResponseDTO();
		responseDTO.setResponseMessage(Constants.TRANSACTION_CREATED_SUCCESSFULLY);
		responseDTO.setResponseStatus("200");
		
		return responseDTO;
		
	}

}
