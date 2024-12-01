package com.orderservice.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.OrderItemStatusDTO;
import com.orderservice.entity.OrderStatusEnum;

@Service
public class Utility {
	
	@Autowired
	private ObjectMapper objectMapper;

	public Integer orderStatusUpdate(Integer statusValue, List<Object> listOfObjects, Integer value) throws JsonMappingException, JsonProcessingException {
		
		List<OrderItemStatusDTO> orderStatusItems = new ArrayList<OrderItemStatusDTO>();
		
		for(Object object: listOfObjects) {
			OrderItemStatusDTO status = new OrderItemStatusDTO();
			String[] stringArray = objectMapper.readValue(objectMapper.writeValueAsString(object), String[].class);
			status.setOrderItemNumber(stringArray[1]);
			status.setStatus(OrderStatusEnum.valueOf(stringArray[0]));
			orderStatusItems.add(status);			
		}
		
		
		int smallest = 100;
		
		for(OrderItemStatusDTO status: orderStatusItems) {
			
			if(status.getStatus().getValue()<smallest) {
				smallest = status.getStatus().getValue();
			}
			
		}
		
		if(smallest>value) {
			return smallest;
		}
		
		return 0;
		
	}

}
