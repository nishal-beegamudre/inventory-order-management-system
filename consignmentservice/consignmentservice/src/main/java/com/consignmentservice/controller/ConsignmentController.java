package com.consignmentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.consignmentservice.dto.ConsignmentResponseDTO;
import com.consignmentservice.dto.ListOfConsignmentDTO;
import com.consignmentservice.dto.ListOfOrderItemDTO;
import com.consignmentservice.service.ConsignmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ConsignmentController {

    private static final Logger logger = LoggerFactory.getLogger(ConsignmentController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsignmentService consignmentService;


    @PostMapping("/consignment/createConsignmentsInBulk")
    public ListOfConsignmentDTO createConsignmentsInBulk(@RequestBody ListOfConsignmentDTO consignmentDTOList) throws JsonMappingException, JsonProcessingException {
        logger.info("Creating consignments in bulk: {}", consignmentDTOList);
        return consignmentService.createConsignmentsInBulk(consignmentDTOList);
    }


    @PutMapping("/consignment/cancelConsignmentsByOrderNumber")
    public ConsignmentResponseDTO cancelConsignmentsByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
        logger.info("Canceling consignments for order number: {}", orderNumber);
        return consignmentService.cancelConsignmentsByOrderNumber(orderNumber);
    }


    @PutMapping("/consignment/cancelConsignmentItems")
    public ConsignmentResponseDTO cancelConsignmentItems(@RequestBody ListOfOrderItemDTO orderItemDTOList) throws JsonMappingException, JsonProcessingException {
        logger.info("Canceling consignment items: {}", orderItemDTOList);
        return consignmentService.cancelConsignmentItems(orderItemDTOList);
    }
}

