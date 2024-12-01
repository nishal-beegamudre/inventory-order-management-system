package com.consignmentservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.consignmentservice.dto.ConsignmentResponseDTO;
import com.consignmentservice.dto.TransactionDTO;
import com.consignmentservice.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TransactionController {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsignmentController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionService transactionService;
	

    @PostMapping("/consignment/createTransaction")
    public ConsignmentResponseDTO createTransaction(@RequestBody TransactionDTO transactionDTO) {
        logger.info("Creating transaction: {}", transactionDTO);
        return transactionService.createTransaction(transactionDTO);
    }

}
