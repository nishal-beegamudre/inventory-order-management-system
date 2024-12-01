package com.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.dto.OrderDTO;
import com.orderservice.dto.OrderItemStatusDTO;
import com.orderservice.dto.CreateOrderResponseDTO;
import com.orderservice.service.OrderService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

 
    @PostMapping("/order/createOrder")
    public CreateOrderResponseDTO createOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Creating new order: {}", orderDTO);
        return orderService.createOrder(orderDTO);
    }


    @PutMapping("/order/orderPlaced")
    public CreateOrderResponseDTO orderPlaced(@RequestBody OrderDTO orderDTO) throws JsonProcessingException {
        logger.info("Order placed: {}", orderDTO);
        return orderService.orderPlaced(orderDTO);
    }


    @PutMapping("/order/updateOrder")
    public CreateOrderResponseDTO updateOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Updating order: {}", orderDTO);
        return orderService.updateOrder(orderDTO);
    }


    @PutMapping("/order/cancelOrderByOrderNumber")
    public CreateOrderResponseDTO cancelOrderByOrderNumber
    			(@RequestParam("orderNumber") String orderNumber) {
        logger.info("Canceling order with order number: {}", orderNumber);
        return orderService.cancelOrderByOrderNumber(orderNumber);
    }


    @PutMapping("/order/returnOrderItems")
    public CreateOrderResponseDTO returnOrderItems(@RequestBody OrderDTO orderDTO) throws JsonMappingException, JsonProcessingException {
        logger.info("Returning items for order: {}", orderDTO);
        return orderService.returnOrderItems(orderDTO);
    }


    @PutMapping("/order/replaceOrderItems")
    public CreateOrderResponseDTO replaceOrderItems(@RequestBody OrderDTO orderDTO) throws JsonProcessingException {
        logger.info("Replacing items for order: {}", orderDTO);
        return orderService.replaceOrderItems(orderDTO);
    }


}
