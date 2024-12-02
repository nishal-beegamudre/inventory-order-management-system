package com.orderservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.ConsignmentResponseDTO;
import com.orderservice.dto.CreateOrderResponseDTO;
import com.orderservice.dto.ListOfStockDTO;
import com.orderservice.dto.ListOfStringDTO;
import com.orderservice.dto.OrderDTO;
import com.orderservice.dto.OrderItemDTO;
import com.orderservice.dto.OrderItemStatusDTO;
import com.orderservice.dto.StateDTO;
import com.orderservice.dto.StockDTO;
import com.orderservice.entity.Order;
import com.orderservice.entity.OrderItem;
import com.orderservice.entity.OrderStatusEnum;
import com.orderservice.feignclient.ConsignmentServiceFeignClient;
import com.orderservice.feignclient.InventoryServiceFeignClient;
import com.orderservice.kafka.producer.KafkaProducer;
import com.orderservice.keygenerator.OrderNumberGenerator;
import com.orderservice.repository.OrderItemRepository;
import com.orderservice.repository.OrderRepository; 
import com.orderservice.util.Constants;
import com.orderservice.util.Utility;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private InventoryServiceFeignClient inventoryServiceFeignClient;
    
    @Autowired
    private ConsignmentServiceFeignClient consignmentServiceFeignClient;
    
    @Autowired
    private OrderNumberGenerator orderNumberGenerator;
    
    @Autowired
    private KafkaProducer kafkaProducer;
    
    @Autowired
    private Utility utility;
    
    @Value("${spring.kafka.topic.email.orderconfirmation}")
    private String orderConfirmationEmail; 
	
	@Value("${spring.kafka.topic.email.ordercancellation}")
    private String orderCancellationEmail; 

    @Value("${spring.kafka.topic.email.returnrequestraised}")
    private String returnRequestRaisedEmail; 
    
    @Value("${spring.kafka.topic.email.replacementrequestraised}")
    private String replacementRequestRaisedEmail; 
    
    @Value("${spring.consignment.strategy.orderitem.count}")
    private String consignmentStrategyOrderItemCount;

    @CachePut(value="orderNumber", key="'singleEntry'", cacheManager="orderNumberCacheManager")
    public CreateOrderResponseDTO createOrder(OrderDTO orderDTO) {
        logger.info("Creating order draft: {}", orderDTO);
        
        List<OrderItemDTO> orderItemsList = orderDTO.getOrderItems();
        ListOfStringDTO listOfStringDTO = new ListOfStringDTO();
        List<String> listOfStrings = new ArrayList<String>();
        Map<String,Long> orderItemSkuToCountMap = new HashMap<String,Long>();
        Set<String> rejectedSKUSet = new HashSet<String>();
        
        for(OrderItemDTO orderItemDTO: orderItemsList) {
        	listOfStrings.add(orderItemDTO.getSku());
        	rejectedSKUSet.add(orderItemDTO.getSku());
        	orderItemSkuToCountMap.put(orderItemDTO.getSku(), orderItemDTO.getQuantity());
        }
        listOfStringDTO.setListOfStrings(listOfStrings);
        
        ListOfStockDTO listOfStockDTO = inventoryServiceFeignClient.getStocksBySKUs(listOfStringDTO);
        List<StockDTO> stockList = listOfStockDTO.getListOfStocks();
        
        for(StockDTO stock: stockList) {
        	
        	if(orderItemSkuToCountMap.get(stock.getSku())<=stock.getStockCount()) {
        		rejectedSKUSet.remove(stock.getSku());
        	}
        	
        	if(rejectedSKUSet.size()==0) {
        		break;
        	}
        	
        }
        
        if(rejectedSKUSet.size()==0) {
        	
        	Order order = new Order();

        	// Order number generation, customer ID fetching 
        	
        	order.setGrandTotal(orderDTO.getGrandTotal());
        	order.setTax(orderDTO.getTax());
        	order.setDiscount(orderDTO.getDiscount());
        	order.setShippingFee(orderDTO.getShippingFee());
        	order.setPayableTotal(orderDTO.getPayableTotal());
        	order.setBillingAddress(orderDTO.getBillingAddress());
        	order.setShippingAddress(orderDTO.getShippingAddress());
        	order.setShippingPincode(orderDTO.getShippingPincode());
        	order.setCustomerEmail(orderDTO.getCustomerEmail());
        	order.setCustomerPhone(orderDTO.getCustomerPhone());
        	order.setCustomerName(orderDTO.getCustomerName());
        	order.setStatus(OrderStatusEnum.DRAFT);
        	order.setStatusLog("");
        	order.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	order.setIsConsumed(true);
        	
        	StringBuilder orderNumberStringBuilder = new StringBuilder();
        	Integer orderItemCount = 1;
        	
        	CreateOrderResponseDTO orderNumberResponse = getOrderNumber(); 
        	if((orderNumberResponse!=null)&&(orderNumberResponse.getOrderNumber()!=null)){
        		orderNumberStringBuilder.append(orderNumberGenerator.generate(orderNumberResponse.getOrderNumber()));
        	}else {
        		orderNumberStringBuilder.append(orderNumberGenerator.generate("0"));
        	}
        	
        	String orderNumberString = orderNumberStringBuilder.toString();
        	order.setOrderNumber(orderNumberString);
        	
        	List<OrderItem> orderItemsToBeCreated = new ArrayList<OrderItem>();
        	
        	for(OrderItemDTO orderItemDTO: orderDTO.getOrderItems()) {
        		
        		OrderItem orderItem = new OrderItem();

        		orderItem.setOrderItemNumber(orderNumberString+"."+orderItemCount.toString());
        		orderItem.setSku(orderItemDTO.getSku());
        		orderItem.setUnitPrice(orderItemDTO.getUnitPrice());
        		orderItem.setSalesPrice(orderItemDTO.getSalesPrice());
        		orderItem.setDiscount(orderItemDTO.getDiscount());
        		orderItem.setShippingFee(orderItemDTO.getShippingFee());
        		orderItem.setTax(orderItemDTO.getTax());
        		orderItem.setQuantity(orderItemDTO.getQuantity());
        		orderItem.setTotalPrice(orderItemDTO.getTotalPrice());
        		orderItem.setTotalSalesPrice(orderItemDTO.getTotalSalesPrice());
        		orderItem.setStatus(OrderStatusEnum.DRAFT);
        		orderItem.setOrderNumber(orderNumberString);

        		orderItem.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        		orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        		
        		orderItemsToBeCreated.add(orderItem);
        		orderItemCount++;
        		
        	}
        	
        	orderRepository.save(order);
        	orderItemRepository.saveAll(orderItemsToBeCreated);
        	
        	CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
        	responseDTO.setResponseMessage(Constants.DRAFT_ORDER_CREATED_SUCCESSFULLLY);
        	responseDTO.setResponseStatus("200");
        	responseDTO.setOrderNumber(orderNumberString);
        	
        	logger.info("Order draft is created successfully with order number {} for customer {} ",
        			orderNumberString,orderDTO.getCustomerEmail());
        	
        	return responseDTO;
	
        }else {
        	
        	CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
        	responseDTO.setRejectedSKUs(rejectedSKUSet);
        	responseDTO.setResponseMessage(Constants.INSUFFICIENT_STOCK);
        	responseDTO.setResponseStatus("404");
        	
        	CreateOrderResponseDTO orderNumberResponse = getOrderNumber();
        	responseDTO.setOrderNumber(orderNumberResponse.getOrderNumber());
        	
        	logger.info("Order draft creation is rejected for the customer with email {} "
        			+ "because of insufficient stock for the following SKUs {}",orderDTO.getCustomerEmail(),
        			responseDTO.getRejectedSKUs());
        	
        	return responseDTO;
        	
        }
        
    }
    
    @Cacheable(value="orderNumber", key="'singleEntry'", cacheManager="orderNumberCacheManager")
    public CreateOrderResponseDTO getOrderNumber() {
    	
    	String lastOrderNumber = orderRepository.getLastOrderNumber();
    	
    	CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
    	responseDTO.setOrderNumber(lastOrderNumber);
    	logger.info("Order number fetched {}",lastOrderNumber);
    	return responseDTO;
    	
    }

    public CreateOrderResponseDTO orderPlaced(OrderDTO orderDTO) throws JsonProcessingException {
        logger.info("Order placed: {}", orderDTO);
        
        Order order = orderRepository.findByOrderNumber(orderDTO.getOrderNumber()).orElse(null);
        
    	order.setBillingAddress(orderDTO.getBillingAddress());
    	order.setShippingAddress(orderDTO.getShippingAddress());
    	order.setShippingPincode(orderDTO.getShippingPincode());
    	order.setCustomerEmail(orderDTO.getCustomerEmail());
    	order.setCustomerPhone(orderDTO.getCustomerPhone());
    	order.setCustomerName(orderDTO.getCustomerName());
    	order.setStatus(OrderStatusEnum.CREATED);
    	order.setStatusLog("Order Placed");
    	order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    	order.setIsConsumed(false);
        
    	List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderNumber(order.getOrderNumber());
    	
    	List<OrderItem> updatedOrderItems = assignWarehouseNumbersToOrderItems(orderItems,order.getShippingPincode());
    	
    	CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
    	
    	if(updatedOrderItems!=null) {
    	
	    	orderRepository.save(order);
	    	orderItemRepository.saveAll(updatedOrderItems);
	    	
	    	responseDTO.setOrderNumber(orderDTO.getOrderNumber());
	    	responseDTO.setResponseMessage(Constants.ORDER_PLACED_SUCCESSFULLY);
	    	responseDTO.setResponseStatus("200");
	    	
	    	logger.info("Order placed successfully with number {}",orderDTO.getOrderNumber());
	    	
	    	kafkaProducer.sendMessage(order.getCustomerEmail(), orderConfirmationEmail, 
	        		order.getOrderNumber(), "NO_CONSIGNMENT");
    	
    	}else {
    		
    		
	    	responseDTO.setOrderNumber(orderDTO.getOrderNumber());
	    	responseDTO.setResponseMessage(Constants.INSUFFICIENT_STOCK);
	    	responseDTO.setResponseStatus("404");
	    	
	    	logger.info("Order place failed due to insufficient stock : order {}",orderDTO.getOrderNumber());
    		
    	}
    	
        return responseDTO;
    }

    public CreateOrderResponseDTO updateOrder(OrderDTO orderDTO) {
        logger.info("Updating order: {}", orderDTO);
        CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
        Order order = orderRepository.findByOrderNumber(orderDTO.getOrderNumber()).orElse(null);
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderNumber(orderDTO.getOrderNumber());
        List<OrderItem> orderItemsToBeUpdated = new ArrayList<OrderItem>();
        Map<String,OrderItemDTO> orderItemNumberToObjectMap = new HashMap<String,OrderItemDTO>();
        
        for(OrderItemDTO orderItemDTO: orderDTO.getOrderItems()) {
        	
        	orderItemNumberToObjectMap.put(orderItemDTO.getOrderItemNumber(), orderItemDTO);
        	
        }
        
        if (order != null) {
        	
            order.setGrandTotal((orderDTO.getGrandTotal() == null) ? order.getGrandTotal() : orderDTO.getGrandTotal());
            order.setTax((orderDTO.getTax() == null) ? order.getTax() : orderDTO.getTax());
            order.setDiscount((orderDTO.getDiscount() == null) ? order.getDiscount() : orderDTO.getDiscount());
            order.setShippingFee((orderDTO.getShippingFee() == null) ? order.getShippingFee() : orderDTO.getShippingFee());
            order.setPayableTotal((orderDTO.getPayableTotal() == null) ? order.getPayableTotal() : orderDTO.getPayableTotal());
            order.setCustomerId((orderDTO.getCustomerId() == null) ? order.getCustomerId() : orderDTO.getCustomerId());
            order.setBillingAddress((orderDTO.getBillingAddress() == null) ? order.getBillingAddress() : orderDTO.getBillingAddress());
            order.setShippingAddress((orderDTO.getShippingAddress() == null) ? order.getShippingAddress() : orderDTO.getShippingAddress());
            order.setShippingPincode((orderDTO.getShippingPincode() == null) ? order.getShippingPincode() : orderDTO.getShippingPincode());
            order.setCustomerEmail((orderDTO.getCustomerEmail() == null) ? order.getCustomerEmail() : orderDTO.getCustomerEmail());
            order.setCustomerPhone((orderDTO.getCustomerPhone() == null) ? order.getCustomerPhone() : orderDTO.getCustomerPhone());
            order.setCustomerName((orderDTO.getCustomerName() == null) ? order.getCustomerName() : orderDTO.getCustomerName());
            order.setStatus((orderDTO.getStatus() == null) ? order.getStatus() : orderDTO.getStatus());
            order.setStatusLog((orderDTO.getStatusLog() == null) ? order.getStatusLog() : orderDTO.getStatusLog());
            order.setLogisticsCodeName((orderDTO.getLogisticsCodeName() == null) ? order.getLogisticsCodeName() : orderDTO.getLogisticsCodeName());
            order.setLogisticsTrackingId((orderDTO.getLogisticsTrackingId() == null) ? order.getLogisticsTrackingId() : orderDTO.getLogisticsTrackingId());
            order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
            
            for(OrderItem orderItem: orderItems) {
            	
            	OrderItemDTO orderItemDTO = orderItemNumberToObjectMap.get(orderItem.getOrderItemNumber());
            	
                orderItem.setSku((orderItemDTO.getSku() == null) ? orderItem.getSku() : orderItemDTO.getSku());
                orderItem.setUnitPrice((orderItemDTO.getUnitPrice() == null) ? orderItem.getUnitPrice() : orderItemDTO.getUnitPrice());
                orderItem.setSalesPrice((orderItemDTO.getSalesPrice() == null) ? orderItem.getSalesPrice() : orderItemDTO.getSalesPrice());
                orderItem.setDiscount((orderItemDTO.getDiscount() == null) ? orderItem.getDiscount() : orderItemDTO.getDiscount());
                orderItem.setShippingFee((orderItemDTO.getShippingFee() == null) ? orderItem.getShippingFee() : orderItemDTO.getShippingFee());
                orderItem.setTax((orderItemDTO.getTax() == null) ? orderItem.getTax() : orderItemDTO.getTax());
                orderItem.setQuantity((orderItemDTO.getQuantity() == null) ? orderItem.getQuantity() : orderItemDTO.getQuantity());
                orderItem.setTotalPrice((orderItemDTO.getTotalPrice() == null) ? orderItem.getTotalPrice() : orderItemDTO.getTotalPrice());
                orderItem.setTotalSalesPrice((orderItemDTO.getTotalSalesPrice() == null) ? orderItem.getTotalSalesPrice() : orderItemDTO.getTotalSalesPrice());
                orderItem.setStatus((orderItemDTO.getStatus() == null) ? orderItem.getStatus() : orderItemDTO.getStatus());
                orderItem.setConsignmentItemNumber((orderItemDTO.getConsignmentItemNumber() == null) ? orderItem.getConsignmentItemNumber() : orderItemDTO.getConsignmentItemNumber());
                orderItem.setConsignmentNumber((orderItemDTO.getConsignmentNumber() == null) ? orderItem.getConsignmentNumber() : orderItemDTO.getConsignmentNumber());
                orderItem.setOrderNumber((orderItemDTO.getOrderNumber() == null) ? orderItem.getOrderNumber() : orderItemDTO.getOrderNumber());
                orderItem.setWarehouseNumber((orderItemDTO.getWarehouseNumber() == null) ? orderItem.getWarehouseNumber() : orderItemDTO.getWarehouseNumber());
                
                // Set last modified date
                orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
            	
            	orderItemsToBeUpdated.add(orderItem);
            }
            
            orderRepository.save(order);
            orderItemRepository.saveAll(orderItemsToBeUpdated);
            
        	responseDTO.setResponseMessage(Constants.ORDER_UPDATED_SUCCESSFULLY);
        	responseDTO.setResponseStatus("200");

            logger.info("Order updated successfully for order number: {}", orderDTO.getOrderNumber());
        } else {
            logger.info("Order does not exist for order number: {}", orderDTO.getOrderNumber());
            
            
        	responseDTO.setResponseMessage(Constants.ORDER_NOT_FOUND);
        	responseDTO.setResponseStatus("404");

        }

        return responseDTO;
    }

    public CreateOrderResponseDTO cancelOrderByOrderNumber(String orderNumber) {
        logger.info("Canceling order with order number: {}", orderNumber);
        
        Order order = orderRepository.findByOrderNumber(orderNumber).orElse(null);
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderNumber(orderNumber);
        List<OrderItem> orderItemsToBeUpdated = new ArrayList<OrderItem>();
        
        order.setStatus(OrderStatusEnum.CANCELLED);
        order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        
        for(OrderItem orderItem: orderItems) {
        	
        	orderItem.setStatus(OrderStatusEnum.CANCELLED);
        	orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	orderItemsToBeUpdated.add(orderItem);
        }
        
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemsToBeUpdated);
        
        ConsignmentResponseDTO responseDTO = consignmentServiceFeignClient.cancelConsignmentsByOrderNumber(orderNumber);
        CreateOrderResponseDTO orderResponseDTO = new CreateOrderResponseDTO();
        orderResponseDTO.setResponseMessage(Constants.ORDER_CANCELLED_SUCCESSFULLY);
        orderResponseDTO.setResponseStatus("200");
        orderResponseDTO.setOrderNumber(orderNumber);
        
        kafkaProducer.sendMessage(order.getCustomerEmail(), orderCancellationEmail, 
        		orderNumber, " ");
        
        return orderResponseDTO;
    }

    public CreateOrderResponseDTO returnOrderItems(OrderDTO orderDTO) throws JsonMappingException, JsonProcessingException {
        logger.info("Returning order items: {}", orderDTO);
        
        List<OrderItemDTO> orderItemsDTO = orderDTO.getOrderItems();
        List<String> orderItemNumbers = new ArrayList<String>();
        
        for(OrderItemDTO orderItemDTO: orderItemsDTO) {
        	orderItemNumbers.add(orderItemDTO.getOrderItemNumber());
        }
        
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByItemNumbers(orderItemNumbers);
        Order order = orderRepository.findByOrderNumber(orderDTO.getOrderNumber()).orElse(null);
        List<Object> orderItemsStatus = 
        		orderItemRepository.findOrderItemsStatusByOrderNumber(orderDTO.getOrderNumber());
        List<OrderItem> orderItemsToBeUpdated = new ArrayList<OrderItem>();
        
        Integer orderStatus = utility.orderStatusUpdate(7,orderItemsStatus,order.getStatus().getValue());
		
		if(orderStatus!=0) {
			OrderStatusEnum status = OrderStatusEnum.fromInt(orderStatus);	
			order.setStatus(status);
		}
        
        for(OrderItem orderItem: orderItems) {
        	
        	orderItem.setStatus(OrderStatusEnum.RETURN_REQUEST_CREATED);
        	orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	orderItemsToBeUpdated.add(orderItem);
        	
        }
        
        order.setIsConsumed(false);
		order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
		orderRepository.save(order);
		orderItemRepository.saveAll(orderItemsToBeUpdated);
		
		CreateOrderResponseDTO orderResponseDTO = new CreateOrderResponseDTO();
        orderResponseDTO.setResponseMessage(Constants.RETURN_REQUEST_INITIATED_SUCCESSFULLY);
        orderResponseDTO.setResponseStatus("200");
        orderResponseDTO.setOrderNumber(orderDTO.getOrderNumber());
        
        kafkaProducer.sendMessage(order.getCustomerEmail(), returnRequestRaisedEmail, 
        		orderDTO.getOrderNumber(), " ");
        
        logger.info("Return request initiated for order items {} ",orderItemNumbers);
        
        return orderResponseDTO;
		
    }

    public CreateOrderResponseDTO replaceOrderItems(OrderDTO orderDTO) throws JsonProcessingException {
        logger.info("Replacing order items: {}", orderDTO);
        
        List<OrderItemDTO> orderItemsDTO = orderDTO.getOrderItems();
        List<String> orderItemNumbers = new ArrayList<String>();
        
        for(OrderItemDTO orderItemDTO: orderItemsDTO) {
        	orderItemNumbers.add(orderItemDTO.getOrderItemNumber());
        }
        
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByItemNumbers(orderItemNumbers);
        Order order = orderRepository.findByOrderNumber(orderDTO.getOrderNumber()).orElse(null);
        List<Object> orderItemsStatus = 
        		orderItemRepository.findOrderItemsStatusByOrderNumber(orderDTO.getOrderNumber());
        List<OrderItem> orderItemsToBeUpdated = new ArrayList<OrderItem>();
        List<OrderItem> orderItemsToBeCreated = new ArrayList<OrderItem>();
        
        Integer orderItemCount = 1;
        Integer orderStatus = utility.orderStatusUpdate(8,orderItemsStatus,order.getStatus().getValue());
		
		if(orderStatus!=0) {
			OrderStatusEnum status = OrderStatusEnum.fromInt(orderStatus);	
			order.setStatus(status);
		}
		
		Order replacementOrder = new Order();
		replacementOrder.setGrandTotal(0.0);
		replacementOrder.setTax(0.0);
		replacementOrder.setDiscount(0.0);
		replacementOrder.setShippingFee(0.0);
		replacementOrder.setPayableTotal(0.0);
		replacementOrder.setBillingAddress(order.getBillingAddress());
		replacementOrder.setShippingAddress(order.getShippingAddress());
		replacementOrder.setShippingPincode(order.getShippingPincode());
		replacementOrder.setCustomerEmail(order.getCustomerEmail());
		replacementOrder.setCustomerPhone(order.getCustomerPhone());
		replacementOrder.setCustomerName(order.getCustomerName());
		replacementOrder.setStatus(OrderStatusEnum.CREATED);
		replacementOrder.setStatusLog("");
		replacementOrder.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
		replacementOrder.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
		replacementOrder.setIsConsumed(false);
		
		StringBuilder orderNumberStringBuilder = new StringBuilder();
		CreateOrderResponseDTO orderNumberResponse = getOrderNumber(); 
    	if((orderNumberResponse!=null)&&(orderNumberResponse.getOrderNumber()!=null)){
    		orderNumberStringBuilder.append(orderNumberGenerator.generate(orderNumberResponse.getOrderNumber()));
    	}else {
    		orderNumberStringBuilder.append(orderNumberGenerator.generate("0"));
    	}
    	
    	String orderNumberString = orderNumberStringBuilder.toString();
    	replacementOrder.setOrderNumber(orderNumberString);
        
        for(OrderItem orderItem: orderItems) {
        	
        	orderItem.setStatus(OrderStatusEnum.REPLACEMENT_REQUEST_CREATED);
        	orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	orderItemsToBeUpdated.add(orderItem);
        	
        	OrderItem replacementOrderItem = new OrderItem();

        	replacementOrderItem.setOrderItemNumber(orderNumberString+"."+orderItemCount.toString());
        	replacementOrderItem.setSku(orderItem.getSku());
        	replacementOrderItem.setUnitPrice(0.0);
        	replacementOrderItem.setSalesPrice(0.0);
        	replacementOrderItem.setDiscount(0.0);
        	replacementOrderItem.setShippingFee(0.0);
        	replacementOrderItem.setTax(0.0);
        	replacementOrderItem.setQuantity(orderItem.getQuantity());
        	replacementOrderItem.setTotalPrice(0.0);
        	replacementOrderItem.setTotalSalesPrice(0.0);
        	replacementOrderItem.setStatus(OrderStatusEnum.CREATED);
        	replacementOrderItem.setOrderNumber(orderNumberString);

        	replacementOrderItem.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        	replacementOrderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    		
    		orderItemsToBeCreated.add(replacementOrderItem);
    		orderItemCount++;
        	
        }
        
        order.setIsConsumed(false);
		order.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
		
		List<OrderItem> orderItemsWithWarehouseNumbers = 
				assignWarehouseNumbersToOrderItems(orderItemsToBeCreated,order.getShippingPincode());
		
		if(orderItemsWithWarehouseNumbers==null) {
			
			CreateOrderResponseDTO orderResponseDTO = new CreateOrderResponseDTO();
	        orderResponseDTO.setResponseMessage(Constants.INSUFFICIENT_STOCK);
	        orderResponseDTO.setResponseStatus("404");
	        return orderResponseDTO;
		}
		
		orderRepository.save(order);
		orderRepository.save(replacementOrder);
		orderItemRepository.saveAll(orderItemsToBeUpdated);
		orderItemRepository.saveAll(orderItemsWithWarehouseNumbers);
		
		CreateOrderResponseDTO orderResponseDTO = new CreateOrderResponseDTO();
        orderResponseDTO.setResponseMessage(Constants.REPLACEMENT_REQUEST_INITIATED_SUCCESSFULLY);
        orderResponseDTO.setResponseStatus("200");
        orderResponseDTO.setOrderNumber(orderDTO.getOrderNumber());
        
        kafkaProducer.sendMessage(order.getCustomerEmail(), replacementRequestRaisedEmail, 
        		orderDTO.getOrderNumber(), " ");
        
        logger.info("Replacement request initiated for order items {} ",orderItemNumbers);
        
        return orderResponseDTO;
        
    }

	private List<OrderItem> assignWarehouseNumbersToOrderItems(List<OrderItem> orderItems, String pincode) throws JsonProcessingException {
		
		Integer skuCount = orderItems.size();
		Integer orderItemStrategyCount = Integer.valueOf(consignmentStrategyOrderItemCount);
		Set<String> setOfSKUs = new HashSet<String>();
		List<String> listOfSKUs = new ArrayList<String>();
		Map<String,Long> skuToQtyMap = new HashMap<String,Long>();
		Map<String,Set<String>> warehouseToListOfSKU = new HashMap<String,Set<String>>();
		HashMap<String,HashMap<String,Double>> warehouseToSKUWithCountMap = new HashMap<String,HashMap<String,Double>>();
		Boolean isExecutionCompleted = false;
		Map<String,Set<String>> finalWarehouseToSKUListMap = new HashMap<String,Set<String>>();
		
		for(OrderItem orderItem: orderItems) {
			setOfSKUs.add(orderItem.getSku());
			listOfSKUs.add(orderItem.getSku());
			skuToQtyMap.put(orderItem.getSku(), orderItem.getQuantity());
		}
		
		StateDTO state = inventoryServiceFeignClient.getStateByPinCode(pincode);
		
		
		ListOfStringDTO requestDTO = new ListOfStringDTO();
		requestDTO.setListOfStrings(listOfSKUs);
		
		ListOfStockDTO listOfStockDTO = inventoryServiceFeignClient.getStocksBySKUs(requestDTO);
		List<StockDTO> stockList = listOfStockDTO.getListOfStocks();
		
		for(StockDTO stockDTO: stockList) {
			if((warehouseToListOfSKU.get(stockDTO.getWarehouseNumber())!=null)&&(skuToQtyMap.get(stockDTO.getSku())<=stockDTO.getStockCount())) {
				Set<String> skuList = warehouseToListOfSKU.get(stockDTO.getWarehouseNumber());
				skuList.add(stockDTO.getSku());
				warehouseToListOfSKU.replace(stockDTO.getWarehouseNumber(), skuList);
				
				HashMap<String,Double> skuCountHashMap = warehouseToSKUWithCountMap.get(stockDTO.getWarehouseNumber());
				skuCountHashMap.put(stockDTO.getSku(), stockDTO.getStockCount());
				warehouseToSKUWithCountMap.replace(stockDTO.getWarehouseNumber(),skuCountHashMap);
			}else if(skuToQtyMap.get(stockDTO.getSku())<=stockDTO.getStockCount()){
				Set<String> skuList = new HashSet<String>();
				skuList.add(stockDTO.getSku());
				warehouseToListOfSKU.put(stockDTO.getWarehouseNumber(), skuList);
				
				HashMap<String,Double> skuCountHashMap = new HashMap<String,Double>();
				skuCountHashMap.put(stockDTO.getSku(), stockDTO.getStockCount());
				warehouseToSKUWithCountMap.put(stockDTO.getWarehouseNumber(), skuCountHashMap);
			}	
		}
		
		Set<String> firstSet = (warehouseToListOfSKU.get(state.getFirstWarehouseNumber())!=null)?warehouseToListOfSKU.get(state.getFirstWarehouseNumber()):new HashSet<String>();
		Set<String> secondSet = (warehouseToListOfSKU.get(state.getSecondWarehouseNumber())!=null)?warehouseToListOfSKU.get(state.getSecondWarehouseNumber()):new HashSet<String>();
		Set<String> thirdSet = (warehouseToListOfSKU.get(state.getThirdWarehouseNumber())!=null)?warehouseToListOfSKU.get(state.getThirdWarehouseNumber()):new HashSet<String>();
		
		ListOfStockDTO listOfStockRequestDTO = new ListOfStockDTO();
		List<StockDTO> stockListRequestDTO = new ArrayList<StockDTO>();
		
		
		
		if(skuCount>orderItemStrategyCount) {
			
			Map<String,Integer> warehouseToSKUCount = new HashMap<String,Integer>();
			Boolean isSingleWarehouseFound = false;
			List<String> singleWarehouseNumbers = new ArrayList<String>();
			String finalSelectedWarehouse = null;
			
			for(String warehouse: warehouseToListOfSKU.keySet()) {
				
				if(warehouseToListOfSKU.get(warehouse).size()==skuCount) {
					singleWarehouseNumbers.add(warehouse);
					isSingleWarehouseFound=true;
				}
				
				warehouseToSKUCount.put(warehouse, warehouseToListOfSKU.get(warehouse).size());
				
			}
			
			if(isSingleWarehouseFound&&(singleWarehouseNumbers.size()==1)) {
				finalSelectedWarehouse = singleWarehouseNumbers.get(0);
				finalWarehouseToSKUListMap.put(finalSelectedWarehouse, setOfSKUs);
				setOfSKUs.clear();
				isExecutionCompleted = true;
				
			}else if(isSingleWarehouseFound&&(singleWarehouseNumbers.size()>1)) {
								
				if(singleWarehouseNumbers.contains(state.getFirstWarehouseNumber())) {
					finalSelectedWarehouse = state.getFirstWarehouseNumber();
				}else if(singleWarehouseNumbers.contains(state.getSecondWarehouseNumber())) {
					finalSelectedWarehouse = state.getSecondWarehouseNumber();
				}else if(singleWarehouseNumbers.contains(state.getThirdWarehouseNumber())) {
					finalSelectedWarehouse = state.getThirdWarehouseNumber();
				}else {
					finalSelectedWarehouse = singleWarehouseNumbers.get(0);
				}
				
				finalWarehouseToSKUListMap.put(finalSelectedWarehouse, setOfSKUs);
				setOfSKUs.clear();
				isExecutionCompleted = true;
			}
			
			if(finalSelectedWarehouse!=null) {
			
				for(String sku: skuToQtyMap.keySet()) {
					StockDTO stockDTO = new StockDTO(null,null,finalSelectedWarehouse,
									sku,(-1)*Double.valueOf(skuToQtyMap.get(sku).toString())
									,null,null,null,null);
					stockListRequestDTO.add(stockDTO);
				}
				listOfStockRequestDTO.setListOfStocks(stockListRequestDTO);
				ListOfStockDTO responseDTO = inventoryServiceFeignClient.updateStockInBulk(listOfStockRequestDTO);
				logger.info("Response after deducting from stock is "+objectMapper.writeValueAsString(responseDTO));
				if(responseDTO.getResponseStatus().equals("200")) {
					logger.info("Entered 200");
					for(OrderItem orderItem: orderItems) {
						orderItem.setWarehouseNumber(finalSelectedWarehouse);
						orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
						orderItem.setStatus(OrderStatusEnum.CREATED);
					}
					
					return orderItems;
					
				}else {
					logger.info("Did not enter 200");
					return null;
				}
			
			}
			
		}if(!isExecutionCompleted) {
			
			Boolean hasFirstSetGotSKUs = false;
			Boolean hasSecondSetGotSKUs = false;
			Boolean hasThirdSetGotSKUs = false;
			
			Set<String> firstSetValidSKUs = new HashSet<String>();
			Set<String> secondSetValidSKUs = new HashSet<String>();
			Set<String> thirdSetValidSKUs = new HashSet<String>();
			
			for(String sku: firstSet) {
				
				HashMap<String,Double> hashMapValue = warehouseToSKUWithCountMap.get(state.getFirstWarehouseNumber());
				Double count = 0.0;
				count = (hashMapValue!=null)?hashMapValue.get(sku):0.0;
				
				if(skuToQtyMap.get(sku)<=count) {
					hasFirstSetGotSKUs = true;
					firstSetValidSKUs.add(sku);
					StockDTO stockDTO = new StockDTO(null,null,state.getFirstWarehouseNumber(),
							sku,(-1)*Double.valueOf(skuToQtyMap.get(sku).toString())
							,null,null,null,null);
					stockListRequestDTO.add(stockDTO);
				}
			}
			
			if(hasFirstSetGotSKUs) {
				if(!secondSet.isEmpty()) {secondSet.removeAll(firstSetValidSKUs);}
				if(!thirdSet.isEmpty()) {thirdSet.removeAll(firstSetValidSKUs);}
				if(!setOfSKUs.isEmpty()) {setOfSKUs.removeAll(firstSetValidSKUs);}
				if(!firstSet.isEmpty()) { finalWarehouseToSKUListMap.put(state.getFirstWarehouseNumber(), firstSetValidSKUs);}
			}
			
			for(String sku: secondSet) {
				
				HashMap<String,Double> hashMapValue = warehouseToSKUWithCountMap.get(state.getSecondWarehouseNumber());
				Double count = 0.0;
				count = (hashMapValue!=null)?hashMapValue.get(sku):0.0;
				
				if(skuToQtyMap.get(sku)<=count) {
					hasSecondSetGotSKUs = true;
					secondSetValidSKUs.add(sku);
				StockDTO stockDTO = new StockDTO(null,null,state.getSecondWarehouseNumber(),
						sku,(-1)*Double.valueOf(skuToQtyMap.get(sku).toString())
						,null,null,null,null);
				stockListRequestDTO.add(stockDTO);
				}
			}
			
			if(hasSecondSetGotSKUs) {
				if(!thirdSet.isEmpty()) {thirdSet.removeAll(secondSetValidSKUs);}
				if(!setOfSKUs.isEmpty()) {setOfSKUs.removeAll(secondSetValidSKUs);}
				if(!secondSet.isEmpty()) {finalWarehouseToSKUListMap.put(state.getSecondWarehouseNumber(), secondSetValidSKUs);}
			}
			
			for(String sku: thirdSet) {
				HashMap<String,Double> hashMapValue = warehouseToSKUWithCountMap.get(state.getThirdWarehouseNumber());
				Double count = 0.0;
				count = (hashMapValue!=null)?hashMapValue.get(sku):0.0;
				
				if(skuToQtyMap.get(sku)<=count) {
					hasThirdSetGotSKUs = true;
					thirdSetValidSKUs.add(sku);
					StockDTO stockDTO = new StockDTO(null,null,state.getThirdWarehouseNumber(),
							sku,(-1)*Double.valueOf(skuToQtyMap.get(sku).toString())
							,null,null,null,null);
					stockListRequestDTO.add(stockDTO);
				}
			}
			
			if(hasThirdSetGotSKUs) {
				if(!setOfSKUs.isEmpty()) {setOfSKUs.removeAll(thirdSetValidSKUs);}
				if(!thirdSet.isEmpty()) {finalWarehouseToSKUListMap.put(state.getThirdWarehouseNumber(), thirdSetValidSKUs);}
			}
			
			if(setOfSKUs.size()>0) {
				
				List<StockDTO> filteredStocks = 
						stockList.stream().filter(a->{return setOfSKUs.contains(a.getSku());}).toList();
				warehouseToListOfSKU.clear();
				SortedMap<Integer,String> skuCountToWarehouseMap = new TreeMap<Integer,String>(Comparator.reverseOrder());
				
				for(StockDTO stockDTO: filteredStocks) {						
					if((warehouseToListOfSKU.get(stockDTO.getWarehouseNumber())!=null)&&(skuToQtyMap.get(stockDTO.getSku())<=stockDTO.getStockCount())) {
						Set<String> skuList = warehouseToListOfSKU.get(stockDTO.getWarehouseNumber());
						skuList.add(stockDTO.getSku());
						warehouseToListOfSKU.replace(stockDTO.getWarehouseNumber(), skuList);
					}else if(skuToQtyMap.get(stockDTO.getSku())<=stockDTO.getStockCount()) {
						Set<String> skuList = new HashSet<String>();
						skuList.add(stockDTO.getSku());
						warehouseToListOfSKU.put(stockDTO.getWarehouseNumber(), skuList);
					}else {
						
					}
				}
				
				for(String warehouse: warehouseToListOfSKU.keySet()) {
					
					if(skuCountToWarehouseMap.get((100)*warehouseToListOfSKU.get(warehouse).size())==null) {
					
						skuCountToWarehouseMap.put((100)*(warehouseToListOfSKU.get(warehouse).size()),warehouse);
					}else {
						int i=1;
						int value = (100)*warehouseToListOfSKU.get(warehouse).size();
						while(skuCountToWarehouseMap.get(value)!=null) {
							value++;
						}
						skuCountToWarehouseMap.put(value,warehouse);
					}
				}
				
				while(setOfSKUs.size()>0) {
					Set<String> selectedSKUs = warehouseToListOfSKU.get
							(skuCountToWarehouseMap.get(skuCountToWarehouseMap.firstKey()));
					if(!selectedSKUs.isEmpty()) {
						selectedSKUs.retainAll(setOfSKUs);
						finalWarehouseToSKUListMap.
						put(skuCountToWarehouseMap.get(skuCountToWarehouseMap.firstKey()), selectedSKUs);
						if(!setOfSKUs.isEmpty()) {setOfSKUs.removeAll(selectedSKUs);}
						skuCountToWarehouseMap.remove(skuCountToWarehouseMap.firstKey());
						if(skuCountToWarehouseMap.isEmpty()) {break;}
					}
				}
				
				if(setOfSKUs.size()>0) {
					return null;
				}
			
			}
			
		}
		
		Map<String,String> finalSKUToWarehouseMap = new HashMap<String,String>();
		
		for (Map.Entry<String, Set<String>> entry : finalWarehouseToSKUListMap.entrySet()) {
            String warehouseNumber = entry.getKey();
            Set<String> skus = entry.getValue();

            for (String sku : skus) {
            	finalSKUToWarehouseMap.put(sku, warehouseNumber);
            }
        }
		
		for(String sku: listOfSKUs) {
			
			StockDTO stockDTO = new StockDTO(null,null,finalSKUToWarehouseMap.get(sku),
					sku,(-1)*Double.valueOf(skuToQtyMap.get(sku).toString())
					,null,null,null,null);
			stockListRequestDTO.add(stockDTO);
		}
		
		listOfStockRequestDTO.setListOfStocks(stockListRequestDTO);
		ListOfStockDTO responseDTO = inventoryServiceFeignClient.updateStockInBulk(listOfStockRequestDTO);
		logger.info("Response after deducting from stock is "+objectMapper.writeValueAsString(responseDTO));
		if(responseDTO.getResponseStatus().equals("200")) {
			
			for(OrderItem orderItem: orderItems) {
				orderItem.setWarehouseNumber(finalSKUToWarehouseMap.get(orderItem.getSku()));
				orderItem.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
				orderItem.setStatus(OrderStatusEnum.CREATED);
			}
			
			return orderItems;
			
		}else {
			
			return null;
		}
		
	}

}
