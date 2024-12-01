package com.orderservice.cronjob;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.ConsignmentDTO;
import com.orderservice.dto.ConsignmentItemDTO;
import com.orderservice.dto.ListOfConsignmentDTO;
import com.orderservice.dto.OrderItemStatusDTO;
import com.orderservice.entity.ConsignmentFulfillmentTypeEnum;
import com.orderservice.entity.ConsignmentStatusEnum;
import com.orderservice.entity.Order;
import com.orderservice.entity.OrderItem;
import com.orderservice.entity.OrderStatusEnum;
import com.orderservice.feignclient.ConsignmentServiceFeignClient;
import com.orderservice.feignclient.InventoryServiceFeignClient;
import com.orderservice.repository.OrderItemRepository;
import com.orderservice.repository.OrderRepository;
import com.orderservice.util.Constants;
import com.orderservice.util.Utility;

@Component
public class PickupConsignmentCreationCronjob {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ConsignmentServiceFeignClient consignmentServiceFeignClient;
	
	@Autowired
	private InventoryServiceFeignClient inventoryServiceFeignClient;
	
	@Autowired
	private Utility utility;
	
	private static final Logger logger = LoggerFactory.getLogger(PickupConsignmentCreationCronjob.class);
	
	@Scheduled(cron="0 */30 * * * *")
	public void createPickupConsignments() throws JsonMappingException, JsonProcessingException {
		
		logger.info("createPickupConsignments cronjob started");
		
		List<Order> orders = orderRepository.findAllNonConsumedOrders();
		StringBuilder orderItemsString = new StringBuilder();
		
		List<String> listOfOrderStatuses = new ArrayList<String>();
		listOfOrderStatuses.add(OrderStatusEnum.RETURN_REQUEST_CREATED.toString());
		listOfOrderStatuses.add(OrderStatusEnum.REPLACEMENT_REQUEST_CREATED.toString());
		
		for(Order order: orders) {
			
			logger.info("order & order items update started for order {}",order.getOrderNumber());
			
			List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderNumberAndStatus
					(order.getOrderNumber(),listOfOrderStatuses);
			
			Map<String,List<OrderItem>> warehouseToOrderItemMap = new HashMap<String,List<OrderItem>>();
			Map<String,OrderItem> orderItemNumberToObjectMap = new HashMap<String,OrderItem>();
			
			for(OrderItem orderItem: orderItems) {
				
				if(warehouseToOrderItemMap.get(orderItem.getWarehouseNumber())!=null) {
					List<OrderItem> orderItemList = warehouseToOrderItemMap.get(orderItem.getWarehouseNumber());
					orderItemList.add(orderItem);
					warehouseToOrderItemMap.replace(orderItem.getWarehouseNumber(), orderItemList);
				}else {
					List<OrderItem> orderItemList = new ArrayList<OrderItem>();
					orderItemList.add(orderItem);
					warehouseToOrderItemMap.put(orderItem.getWarehouseNumber(), orderItemList);
				}
				
				orderItemNumberToObjectMap.put(orderItem.getOrderItemNumber(), orderItem);
				
			}
			
			if(!orderItems.isEmpty()) {
				
				List<ConsignmentDTO> consignmentListToBeCreated = new ArrayList<ConsignmentDTO>();
				
				ConsignmentDTO consignment = new ConsignmentDTO();
	        	
	        	for(String warehouseNumber: warehouseToOrderItemMap.keySet()) {
	        	
	        	List<OrderItem> orderItemsListFromMap = warehouseToOrderItemMap.get(warehouseNumber);
	        	List<ConsignmentItemDTO> consignmentItems = new ArrayList<ConsignmentItemDTO>();
	        	
	        	StringBuilder items = new StringBuilder();
	        	
	        	for(OrderItem orderItem: orderItemsListFromMap) {
	        		
					orderItemsString.append(orderItem.getOrderItemNumber()+";");

	        		ConsignmentItemDTO consignmentItem = new ConsignmentItemDTO();

	        		consignmentItem.setConsignmentItemNumber("CR_"+orderItem.getOrderItemNumber());
	        		consignmentItem.setSku(orderItem.getSku());
	        		consignmentItem.setQuantity(orderItem.getQuantity());
	        		consignmentItem.setOrderItemNumber(orderItem.getOrderItemNumber());
	        		consignmentItem.setOrderNumber(orderItem.getOrderNumber());
	        		consignmentItem.setWarehouseNumber(warehouseNumber);
	        		consignmentItem.setStatus(ConsignmentStatusEnum.CREATED);
	        		consignmentItem.setFulfillmentType(ConsignmentFulfillmentTypeEnum.PICKUP);
	        		
	        		consignmentItems.add(consignmentItem);
	        		
	        		items.append("CR_"+orderItem.getOrderItemNumber()+";");
	        		
	        	}
	        	
	        	consignment.setOrderItems(orderItemsString.toString());
	        	consignment.setConsignmentItems(consignmentItems);
	        	consignment.setWarehouseNumber(warehouseNumber);
	        	consignment.setStatus(ConsignmentStatusEnum.CREATED);
	        	consignment.setStatusLog("Consignment has been created");
	        	consignment.setBillingAddress(order.getBillingAddress());
	        	consignment.setShippingAddress(order.getShippingAddress());
	        	consignment.setShippingPinCode(order.getShippingPincode());
	        	consignment.setCustomerName(order.getCustomerName());
	        	consignment.setPhone(order.getCustomerPhone());
	        	consignment.setEmail(order.getCustomerEmail());
	        	consignment.setFulfillmentType(ConsignmentFulfillmentTypeEnum.PICKUP);
	        	consignment.setOrderNumber(order.getOrderNumber());
	        	
	        	consignmentListToBeCreated.add(consignment);
	        	
	        	}
	        	
	        	ListOfConsignmentDTO listOfConsignmentDTO = new ListOfConsignmentDTO();
	        	listOfConsignmentDTO.setListOfConsignments(consignmentListToBeCreated);
	        	
	        	ListOfConsignmentDTO responseListOfConsignments =  consignmentServiceFeignClient
	        			.createConsignmentsInBulk(listOfConsignmentDTO);
	        		        	
	        	order.setIsConsumed(true);
	        	order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
	        	
	        	List<OrderItem> orderItemsToBeUpdated = new ArrayList<OrderItem>();
	        	
	        	for(ConsignmentDTO consignmentDTO: responseListOfConsignments.getListOfConsignments()) {
	        		
	        		for(ConsignmentItemDTO consignmentItemDTO: consignmentDTO.getConsignmentItems()) {
	        			
	        			OrderItem orderItem = orderItemNumberToObjectMap.get(consignmentItemDTO.getOrderItemNumber());
	        			
	        			orderItem.setConsignmentItemNumber(consignmentItemDTO.getConsignmentItemNumber());
	        			orderItem.setConsignmentNumber(consignmentItemDTO.getConsignmentNumber());
	        			orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
	        			orderItem.setStatus(orderItem.getStatus()==OrderStatusEnum.RETURN_REQUEST_CREATED?
	        					OrderStatusEnum.RETURN_REQUEST_CONFIRMED:OrderStatusEnum.REPLACEMENT_REQUEST_CONFIRMED);
	        			
	        			orderItemsToBeUpdated.add(orderItem);
	        			
	        		}
	        		
	        	}
	        	
	        	List<Object> orderItemStatus = orderItemRepository.
	    				findOrderItemsStatusByOrderNumber(order.getOrderNumber());
	    		
	    		Integer orderStatus = utility.orderStatusUpdate(9,orderItemStatus,order.getStatus().getValue());
	    				
	    		if(orderStatus!=0) {	
	    			OrderStatusEnum status = OrderStatusEnum.fromInt(orderStatus);		
	    			order.setStatus(status);	    			
	    		}
	        	
	        	logger.info("order & order items updated for order {}",order.getOrderNumber());
	        	
	        	orderRepository.save(order);
	        	orderItemRepository.saveAll(orderItemsToBeUpdated);
	        					
			}
			
		}
		
		logger.info("createPickupConsignments cronjob ended");
		
	}
		

}
