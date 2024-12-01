package com.consignmentservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consignmentservice.dto.ConsignmentCountDTO;
import com.consignmentservice.dto.ConsignmentDTO;
import com.consignmentservice.dto.ConsignmentItemDTO;
import com.consignmentservice.dto.ConsignmentNumberCountDTO;
import com.consignmentservice.dto.ConsignmentResponseDTO;
import com.consignmentservice.dto.ListOfConsignmentDTO;
import com.consignmentservice.dto.ListOfOrderItemDTO;
import com.consignmentservice.dto.ListOfTransactionDTO;
import com.consignmentservice.dto.OrderItemDTO;
import com.consignmentservice.dto.StockMovementDTO;
import com.consignmentservice.dto.TransactionDTO;
import com.consignmentservice.entity.Consignment;
import com.consignmentservice.entity.ConsignmentFulfillmentTypeEnum;
import com.consignmentservice.entity.ConsignmentItem;
import com.consignmentservice.entity.ConsignmentStatusEnum;
import com.consignmentservice.entity.StockMovementReasonEnum;
import com.consignmentservice.entity.StockMovementStatus;
import com.consignmentservice.entity.StockMovementTypeEnum;
import com.consignmentservice.feignclient.InventoryServiceFeignClient;
import com.consignmentservice.repository.ConsignmentItemRepository;
import com.consignmentservice.repository.ConsignmentRepository;
import com.consignmentservice.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConsignmentService {

    private static final Logger logger = LoggerFactory.getLogger(ConsignmentService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsignmentRepository consignmentRepository;
    
    @Autowired
    private ConsignmentItemRepository consignmentItemRepository;
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private InventoryServiceFeignClient inventoryServiceFeignClient;

    public ListOfConsignmentDTO createConsignmentsInBulk(ListOfConsignmentDTO consignmentDTOList) throws JsonMappingException, JsonProcessingException {
        
        logger.info("Creating consignments in bulk: {}", consignmentDTOList);
        
        ListOfConsignmentDTO responseDTO = new ListOfConsignmentDTO();
        List<ConsignmentDTO> newConsignmentDTOList = new ArrayList<ConsignmentDTO>();
        List<ConsignmentDTO> consignmentList = consignmentDTOList.getListOfConsignments();
        List<Consignment> consignmentListToBeCreated = new ArrayList<Consignment>();
        List<ConsignmentItem> consignmentItemsToBeCreated = new ArrayList<ConsignmentItem>();
        Map<String,Long> orderNumberToNumberOfConsignmentsMap = 
        		new HashMap<String,Long>();
        Set<String> orderNumbers = new HashSet<String>();
        
        for(ConsignmentDTO consignmentDTO: consignmentList) {
        	orderNumbers.add(consignmentDTO.getOrderNumber());
        }
        
        List<Object> outputConsignmentCount = consignmentRepository
        		.fetchConsignmentCountByOrderNumbers(orderNumbers);
        
        List<ConsignmentCountDTO> consignmentCountDTO = new ArrayList<ConsignmentCountDTO>();
		
		for(Object object: outputConsignmentCount) {
			ConsignmentCountDTO consignment = new ConsignmentCountDTO();
			String[] stringArray = objectMapper.readValue(objectMapper.writeValueAsString(object), String[].class);
			consignment.setOrderNumber(stringArray[0]);
			consignment.setCount(Long.valueOf(stringArray[1]));
			consignmentCountDTO.add(consignment);			
		}
        
        for(ConsignmentCountDTO countDTO: consignmentCountDTO) {
        	orderNumberToNumberOfConsignmentsMap.put(countDTO.getOrderNumber(), countDTO.getCount());
        }
        
        for(ConsignmentDTO consignmentDTO: consignmentList) {
        	
        	Long value = 0L;
        	
        	if(!orderNumberToNumberOfConsignmentsMap.isEmpty()) {
        			value = (orderNumberToNumberOfConsignmentsMap.get(consignmentDTO.getOrderNumber())+1);
        	}
        			
        	value++;
        	
        	StockMovementDTO stockMovementRequestDTO = new StockMovementDTO();
            
            stockMovementRequestDTO.setWarehouseNumber(consignmentDTO.getWarehouseNumber());
            
            StringBuilder skuAndStock = new StringBuilder();
            for(ConsignmentItemDTO itemDTO: consignmentDTO.getConsignmentItems()) {
            	skuAndStock.append(itemDTO.getSku()+"_"+itemDTO.getQuantity()+";");
            }
            
            stockMovementRequestDTO.setSkuAndStock(skuAndStock.toString());
            if(consignmentDTO.getFulfillmentType()==ConsignmentFulfillmentTypeEnum.DELIVERY) {
            	stockMovementRequestDTO.setMovementType(StockMovementTypeEnum.DOWN);
                stockMovementRequestDTO.setReason(StockMovementReasonEnum.DELIVERY_FULFILLMENT);
            }else {
            	stockMovementRequestDTO.setMovementType(StockMovementTypeEnum.UP);
                stockMovementRequestDTO.setReason(StockMovementReasonEnum.RETURN_FULFILLMENT);
            }
            
            stockMovementRequestDTO.setConsignmentNumber(consignmentDTO.getOrderNumber()+"_00"+value.toString());
            stockMovementRequestDTO.setStatus(StockMovementStatus.INITIATED);
            stockMovementRequestDTO.setIsConsumed(true);
            
            StockMovementDTO stockMovementDTO = inventoryServiceFeignClient.createStockMovement(stockMovementRequestDTO);
        	
        	Consignment consignment = new Consignment();
        	
        	consignment.setConsignmentNumber(consignmentDTO.getOrderNumber()+"_00"+value.toString());
        	consignment.setOrderItems(consignmentDTO.getOrderItems());
        	consignment.setOrderNumber(consignmentDTO.getOrderNumber());
        	
        	List<ConsignmentItemDTO> consignmentItemDTOList = consignmentDTO.getConsignmentItems();
        	List<ConsignmentItemDTO> newConsignmentItemDTOList = new ArrayList<ConsignmentItemDTO>();
        	
        	StringBuilder items = new StringBuilder();
        	
        	
        	for(ConsignmentItemDTO consignmentItemDTO: consignmentItemDTOList) {

        		ConsignmentItem consignmentItem = new ConsignmentItem();
        		

        		consignmentItem.setConsignmentItemNumber("C_"+consignmentItemDTO.getOrderItemNumber());    		
        		consignmentItem.setOrderItemNumber(consignmentItemDTO.getOrderItemNumber());
        		consignmentItem.setConsignmentNumber(consignmentDTO.getOrderNumber()+"_00"+value.toString());
        		consignmentItem.setOrderNumber(consignmentDTO.getOrderNumber());
        		consignmentItem.setWarehouseNumber(consignmentDTO.getWarehouseNumber());
        		consignmentItem.setStatus(ConsignmentStatusEnum.CREATED);
        		consignmentItem.setFulfillmentType(consignmentDTO.getFulfillmentType());
        		consignmentItem.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        		consignmentItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        		consignmentItem.setSku(consignmentItemDTO.getSku());
        		consignmentItem.setQuantity(consignmentItemDTO.getQuantity());
        		
        		consignmentItemsToBeCreated.add(consignmentItem);
        		
        		newConsignmentItemDTOList.add(new ConsignmentItemDTO
        				(null,"C_"+consignmentItemDTO.getOrderItemNumber(),
        						consignmentItemDTO.getOrderItemNumber(),
        						consignmentDTO.getOrderNumber()+"_00"+value.toString(),
        						consignmentDTO.getOrderNumber(),
        						consignmentDTO.getWarehouseNumber(),ConsignmentStatusEnum.CREATED,
        						null,null,null,null,null,consignmentItemDTO.getSku(),consignmentItemDTO.getQuantity()));
        		
        		items.append("C_"+consignmentItemDTO.getOrderItemNumber()+";");
        		
        	}
        	
        	
        	consignment.setConsignmentItems(items.toString());
        	consignment.setWarehouseNumber(consignmentDTO.getWarehouseNumber());
        	consignment.setStatus(ConsignmentStatusEnum.CREATED);
        	consignment.setStatusLog("Consignment has been created");
        	consignment.setBillingAddress(consignmentDTO.getBillingAddress());
        	consignment.setShippingAddress(consignmentDTO.getShippingAddress());
        	consignment.setShippingPinCode(consignmentDTO.getShippingPinCode());
        	consignment.setCustomerName(consignmentDTO.getCustomerName());
        	consignment.setPhone(consignmentDTO.getPhone());
        	consignment.setEmail(consignmentDTO.getEmail());
        	consignment.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	consignment.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	consignment.setFulfillmentType(consignmentDTO.getFulfillmentType());
        	consignment.setStockMovementId(stockMovementDTO.getId());
        	
        	consignmentListToBeCreated.add(consignment);
        	
        	orderNumberToNumberOfConsignmentsMap.replace(consignmentDTO.getOrderNumber(), value);

        
        	TransactionDTO transactionDTO = new TransactionDTO();
        	transactionDTO.setConsignmentNumber(consignment.getConsignmentNumber());
        	transactionDTO.setOrderNumber(consignment.getOrderNumber());
        	transactionDTO.setPresentStatus(ConsignmentStatusEnum.CREATED);
        	transactionDTO.setPreviousStatus(ConsignmentStatusEnum.NO_STATUS);
        	
        	if(consignmentDTO.getFulfillmentType()==ConsignmentFulfillmentTypeEnum.DELIVERY) {
        		transactionDTO.setFulfillmentType(ConsignmentFulfillmentTypeEnum.DELIVERY);
            }else {
            	transactionDTO.setFulfillmentType(ConsignmentFulfillmentTypeEnum.PICKUP);
            }
            
            transactionService.createTransaction(transactionDTO);
            
            newConsignmentDTOList.add(new ConsignmentDTO(null,consignmentDTO.getOrderNumber()+"_00"+value.toString(),null,
            		consignmentDTO.getOrderNumber(),newConsignmentItemDTOList,consignmentDTO.getWarehouseNumber(),
            		ConsignmentStatusEnum.CREATED,null,null,null,null,null,null,null,null,null,null,null,null,null,
            		null,null,null
            		));
            
        	
        }
        
        consignmentItemRepository.saveAll(consignmentItemsToBeCreated);
        consignmentRepository.saveAll(consignmentListToBeCreated);
        
        
        
        responseDTO.setListOfConsignments(newConsignmentDTOList);      
        responseDTO.setResponseMessage(Constants.CONSIGNMENT_CREATED_SUCCESSFULLY);
        responseDTO.setResponseStatus("200");
        
        return responseDTO;
    }

    public ConsignmentResponseDTO cancelConsignmentsByOrderNumber(String orderNumber) {
        logger.info("Canceling consignments for order number: {}", orderNumber);
        
        List<Consignment> consignments = consignmentRepository.findConsignmentsByOrderNumber(orderNumber);
        List<ConsignmentItem> consignmentItems = new ArrayList<ConsignmentItem>();
        List<String> consignmentItemNumbersList = new ArrayList<String>();
        List<Consignment> consignmentsToBeUpdated = new ArrayList<Consignment>();
        List<ConsignmentItem> consignmentItemsToBeUpdated = new ArrayList<ConsignmentItem>();
        List<TransactionDTO> listOfTransactions = new ArrayList<TransactionDTO>();
        
        for(Consignment consignment: consignments) {
        	
        	String[] stringArray = consignment.getConsignmentItems().split(";");
        	ConsignmentStatusEnum enumValue = consignment.getStatus();
        	
        	consignment.setStatus(ConsignmentStatusEnum.CANCELLED);
        	consignment.setStatusLog("Consignment has been cancelled");
        	consignment.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	
        	consignmentsToBeUpdated.add(consignment);
        	consignmentItemNumbersList.addAll(Arrays.asList(stringArray).stream()
        			.map(obj -> (String) obj) 
                    .collect(Collectors.toList()));
        	
        	TransactionDTO transactionDTO = new TransactionDTO();
        	transactionDTO.setConsignmentNumber(consignment.getConsignmentNumber());
        	transactionDTO.setOrderNumber(consignment.getOrderNumber());
        	transactionDTO.setPresentStatus(ConsignmentStatusEnum.USER_CANCELLED);
        	transactionDTO.setPreviousStatus(enumValue);
            transactionDTO.setFulfillmentType(ConsignmentFulfillmentTypeEnum.DELIVERY);
        	listOfTransactions.add(transactionDTO);
        	
        }
        
        List<ConsignmentItem> consignmentItemEntries = consignmentItemRepository
        		.findAllConsignmentItemsByItemNumbers(consignmentItemNumbersList);
        
        for(ConsignmentItem consignmentItem: consignmentItemEntries) {
        	
        	consignmentItem.setStatus(ConsignmentStatusEnum.CANCELLED);
        	consignmentItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	consignmentItemsToBeUpdated.add(consignmentItem);
        	
        }
        
        
        consignmentRepository.saveAll(consignmentsToBeUpdated);
        consignmentItemRepository.saveAll(consignmentItemsToBeUpdated);
        transactionService.createTransactionsInBulk
        (new ListOfTransactionDTO(listOfTransactions,null,null));
        
        logger.info("Consignments are cancelled for the order number {} ",orderNumber);
        
        ConsignmentResponseDTO responseDTO = new ConsignmentResponseDTO();
        responseDTO.setResponseMessage(Constants.CONSIGNMENT_CANCELLED_SUCCESSFULLY);
        responseDTO.setResponseStatus("200");
        
        return responseDTO;
    }

    public ConsignmentResponseDTO cancelConsignmentItems(ListOfOrderItemDTO orderItemDTOList) throws JsonMappingException, JsonProcessingException {
        
        logger.info("Canceling consignment items: {}", orderItemDTOList);
        
        List<OrderItemDTO> orderItemsDTO = orderItemDTOList.getListOfOrderItems();
        List<String> orderItemsList = new ArrayList<String>();
        
        for(OrderItemDTO orderItem: orderItemsDTO) {
        	
        	orderItemsList.add(orderItem.getOrderItemNumber());
        	
        }
        
        List<ConsignmentItem> consignmentItems = consignmentItemRepository
        		.findAllConsignmentItemsByOrderItemNumbers(orderItemsList);
        Set<String> consignmentNumbersSet = new HashSet<String>();
        Map<String,Long> consignmentNumberToItsItemsCountMap = new HashMap<String,Long>();
        List<ConsignmentItem> consignmentItemsToBeCancelled = new ArrayList<ConsignmentItem>();
        List<Consignment> consignmentsToBeUpdated = new ArrayList<Consignment>();
        for(ConsignmentItem consignmentItem: consignmentItems) {
        	
        	consignmentItem.setStatus(ConsignmentStatusEnum.CANCELLED);
        	consignmentItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	consignmentNumbersSet.add(consignmentItem.getConsignmentNumber());
        	
        	if(consignmentNumberToItsItemsCountMap.get(consignmentItem.getConsignmentNumber())!=null) {
        		consignmentNumberToItsItemsCountMap.replace(consignmentItem.getConsignmentNumber(),
        				consignmentNumberToItsItemsCountMap.get(consignmentItem.getConsignmentNumber())+1);
        	}else {
        		consignmentNumberToItsItemsCountMap.put(consignmentItem.getConsignmentNumber(),1L);
        	}
        	
        	consignmentItemsToBeCancelled.add(consignmentItem);
        	
        }
        
        List<Object> consignmentCountObjects = consignmentItemRepository.
        		fetchConsignmentCountByConsignmentNumbers(consignmentNumbersSet);
        
        List<ConsignmentNumberCountDTO> consignmentCounts = new ArrayList<ConsignmentNumberCountDTO>();		
		for(Object object: consignmentCountObjects) {
			ConsignmentNumberCountDTO numberCount = new ConsignmentNumberCountDTO();
			String[] stringArray = objectMapper.readValue(objectMapper.writeValueAsString(object), String[].class);
			numberCount.setConsignmentNumber(stringArray[0]);
			numberCount.setCount(Long.valueOf(stringArray[1]));
			consignmentCounts.add(numberCount);			
		}
        
        
        List<Consignment> consignments = consignmentRepository.findConsignmentsByConsignmentNumbers(consignmentNumbersSet);
        Map<String,Consignment> consignmentNumberToObjectMap = new HashMap<String,Consignment>();
        List<TransactionDTO> listOfTransactions = new ArrayList<TransactionDTO>();
        
        for(Consignment consignment: consignments) {
        	consignmentNumberToObjectMap.put(consignment.getConsignmentNumber(), consignment);
        }
        
        
        for(ConsignmentNumberCountDTO countDTO: consignmentCounts) {
        	
        	Consignment consignment = consignmentNumberToObjectMap.get(countDTO.getConsignmentNumber());
        	ConsignmentStatusEnum previousStatus = consignment.getStatus();

        	
        	if(consignmentNumberToItsItemsCountMap.get(countDTO.getConsignmentNumber())<countDTO.getCount()) {
        		consignment.setStatus(ConsignmentStatusEnum.PARTIALLY_CANCELLED);
        		consignment.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        		listOfTransactions.add(new TransactionDTO
        				(null,countDTO.getConsignmentNumber(),
        				consignment.getOrderNumber(),previousStatus,null,null,
        				ConsignmentStatusEnum.PARTIALLY_CANCELLED,null,null,ConsignmentFulfillmentTypeEnum.DELIVERY,null,null));
        	}else {
        		consignment.setStatus(ConsignmentStatusEnum.CANCELLED);
        		consignment.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        		consignmentsToBeUpdated.add(consignment);
        		listOfTransactions.add(new TransactionDTO
        				(null,countDTO.getConsignmentNumber(),
        				consignment.getOrderNumber(),previousStatus,null,null,
        				ConsignmentStatusEnum.USER_CANCELLED,null,null,ConsignmentFulfillmentTypeEnum.DELIVERY,null,null));
        	}
        	
        }
        
        consignmentItemRepository.saveAll(consignmentItemsToBeCancelled);
        consignmentRepository.saveAll(consignmentsToBeUpdated);
        transactionService.createTransactionsInBulk
        (new ListOfTransactionDTO(listOfTransactions,null,null));

        
        ConsignmentResponseDTO responseDTO = new ConsignmentResponseDTO();
        responseDTO.setResponseMessage(Constants.CONSIGNMENTITEMS_CANCELLED_SUCCESSFULLY);
        responseDTO.setResponseStatus("200");
        
        return responseDTO;
    }

}
