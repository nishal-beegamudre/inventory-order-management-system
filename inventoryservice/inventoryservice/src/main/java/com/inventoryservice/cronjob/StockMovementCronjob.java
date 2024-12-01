package com.inventoryservice.cronjob;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.ListOfStockDTO;
import com.inventoryservice.dto.StockDTO;
import com.inventoryservice.entity.StockMovement;
import com.inventoryservice.entity.StockMovementTypeEnum;
import com.inventoryservice.repository.StockMovementRepository;
import com.inventoryservice.repository.StockRepository;
import com.inventoryservice.service.StockService;

@Component
public class StockMovementCronjob {
	
private static final Logger logger = LoggerFactory.getLogger(StockMovementCronjob.class);

	@Autowired
	private StockMovementRepository stockMovementRepository;
	
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Scheduled(cron="0 */15 * * * *")
	public void consumeStockMovements() throws JsonProcessingException {
		
		logger.info("Consuming stock movements is started");
		
		List<StockMovement> stockMovements = stockMovementRepository.findAllNonConsumed();
		
		ListOfStockDTO listOfStockDTO = new ListOfStockDTO();
		List<StockDTO> stockList = new ArrayList<StockDTO>();
		
		for(StockMovement stockMovement: stockMovements) {
			
			logger.info("Stock Movement for the entry is started {}",objectMapper.writeValueAsString(stockMovement));
			
			Integer positiveOrNegative = 0;
			
			if(stockMovement.getMovementType()==StockMovementTypeEnum.UP) {
				positiveOrNegative = 1;
			}else {
				positiveOrNegative = -1;
			}
			
			String[] skuValues = stockMovement.getSkuAndStock().split(";");
			Map<String,Double> skuToCountMap = new HashMap<String,Double>();
			
			for(String skuValue: skuValues) {
				String[] skuAndValue = skuValue.split("_");
				skuToCountMap.put(skuAndValue[0], Double.valueOf(skuAndValue[1]));
			}
			
			for(String sku: skuToCountMap.keySet()) {
				StockDTO stockDTO = new StockDTO();
				stockDTO.setSku(sku);
				stockDTO.setStockCount(positiveOrNegative*skuToCountMap.get(sku));
				stockDTO.setWarehouseNumber(stockMovement.getWarehouseNumber());
				stockList.add(stockDTO);
			}
			
			listOfStockDTO.setListOfStocks(stockList);
			
			ListOfStockDTO responseDTO = stockService.updateStockInBulk(listOfStockDTO);
			
			if(responseDTO.getResponseStatus()=="200") {
				stockMovement.setIsConsumed(true);
				stockMovementRepository.save(stockMovement);
				logger.info("Stock Movement for the entry is successful {}",objectMapper.writeValueAsString(stockMovement));
			}
			
			logger.info("Stock Movement for the entry is failed {}",objectMapper.writeValueAsString(responseDTO));
			
		}
		
		logger.info("Consuming stock movements is ended");
		
	}

}
