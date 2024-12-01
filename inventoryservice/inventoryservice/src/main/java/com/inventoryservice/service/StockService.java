package com.inventoryservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.dto.FetchStockInBulkRequestDTO;
import com.inventoryservice.dto.ListOfListOfStockDTO;
import com.inventoryservice.dto.ListOfStockDTO;
import com.inventoryservice.dto.ListOfStringDTO;
import com.inventoryservice.dto.StockDTO;
import com.inventoryservice.entity.Stock;
import com.inventoryservice.repository.StockRepository;
import com.inventoryservice.util.Constants;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StockRepository stockRepository;

    public StockDTO updateStock(StockDTO stockDTO) {
        
    	Stock existingStock = stockRepository.findStockByWarehouseNumberAndSku
    			(stockDTO.getWarehouseNumber(), stockDTO.getSku()).orElse(null);
    	
    	if(existingStock==null) {
    		
    		if(stockDTO.getStockCount()<=0) {
    			
    			stockDTO.setResponseMessage(Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
    			stockDTO.setResponseStatus("404");
    			logger.info("Stock update for warehouse {}, SKU {} has been"
    					+ " rejected because : {}",stockDTO.getWarehouseNumber(),
    					stockDTO.getSku(), Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
    			return stockDTO;
    			
    		}else {
    			
    			Stock stock = new Stock();
    			stock.setName(stockDTO.getSku()+"_"+stockDTO.getWarehouseNumber());
    			stock.setWarehouseNumber(stockDTO.getWarehouseNumber());
    			stock.setSku(stockDTO.getSku());
    			stock.setStockCount(stockDTO.getStockCount());
    			stock.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    			stock.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
    			
    			logger.info("Stock has been created for warehouse {}, SKU {} with count"
    					+ "{}",stockDTO.getWarehouseNumber(),
    					stockDTO.getSku(), stockDTO.getStockCount());
    			
    			stockRepository.save(stock);
    			
    		}
    		
    		
    		
    	}else {
    		
    		if((existingStock.getStockCount()+stockDTO.getStockCount())>=0) {
    			
    			existingStock.setStockCount(existingStock.getStockCount()
    					+stockDTO.getStockCount());
    			
    			logger.info("Stock has been updated for warehouse {}, SKU {} with updated count"
    					+ "{}",stockDTO.getWarehouseNumber(),
    					stockDTO.getSku(), existingStock.getStockCount());
    			
    			stockRepository.save(existingStock);
    			
    		}else {
    			
    			stockDTO.setResponseMessage(Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
    			stockDTO.setResponseStatus("404");
    			logger.info("Stock update for warehouse {}, SKU {} has been"
    					+ " rejected because : {}",stockDTO.getWarehouseNumber(),
    					stockDTO.getSku(), Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
    			
    			return stockDTO;
    			
    		}
    		
    		
    		
    	}
    	stockDTO.setResponseMessage(Constants.STOCK_UPDATED_SUCCESSFULLY);
    	stockDTO.setResponseStatus("200");
        
        return stockDTO;
    }

    public ListOfStockDTO updateStockInBulk(ListOfStockDTO listOfStockDTO) {
        
    	List<StockDTO> listOfStocks = listOfStockDTO.getListOfStocks();
    	Map<String,Double> stockNameToCountMap = new HashMap<String,Double>();
    	Set<String> stockInputNames = new HashSet<String>();
    	Map<String,Stock> stockNameToStockMap = new HashMap<String,Stock>();
    	List<StockDTO> rejectedStocksList = new ArrayList<StockDTO>();
    	List<Stock> stocksToBeUpdated = new ArrayList<Stock>();
    	List<Stock> stocksToBeCreated = new ArrayList<Stock>();
    	
    	
    	for(StockDTO stock: listOfStocks) {
    		String name = stock.getSku()+"_"+stock.getWarehouseNumber();		
    		stockNameToCountMap.put(name,stock.getStockCount());
    		stockInputNames.add(name);
    	}
    	
    	List<Stock> existingStockList = stockRepository.findStocksByName(stockInputNames);
    	
    	for(Stock stock: existingStockList) {
    		stockNameToStockMap.put(stock.getName(), stock);
    	}
    	
    	for(StockDTO stock: listOfStocks) {
    		
    		String name = stock.getSku()+"_"+stock.getWarehouseNumber();
    		
    		if(stockNameToStockMap.get(name)!=null){
    			
    			Stock existingStock = stockNameToStockMap.get(name);
    			
    			if((existingStock.getStockCount()+stock.getStockCount())>=0) {
        			
        			existingStock.setStockCount(existingStock.getStockCount()
        					+stock.getStockCount());
        			
        			logger.info("Stock will be updated for warehouse {}, SKU {} with updated count"
        					+ "{}",stock.getWarehouseNumber(),
        					stock.getSku(), existingStock.getStockCount());
        			
        			stocksToBeUpdated.add(existingStock);
        			
        		}else {
        			
        			stock.setResponseMessage(Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
        			stock.setResponseStatus("404");
        			logger.info("Stock update for warehouse {}, SKU {} has been"
        					+ " rejected because : {}",stock.getWarehouseNumber(),
        					stock.getSku(), Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
        			
        			rejectedStocksList.add(stock);
        			
        		}
    			
    			
    		}else {
    			
    			if(stock.getStockCount()<=0) {
        			
        			stock.setResponseMessage(Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
        			stock.setResponseStatus("404");
        			logger.info("Stock update for warehouse {}, SKU {} has been"
        					+ " rejected because : {}",stock.getWarehouseNumber(),
        					stock.getSku(), Constants.STOCKCOUNT_CANNOT_BE_ZERO_OR_NEGATIVE);
        			rejectedStocksList.add(stock);
        			
        		}else {
        			
        			Stock newStock = new Stock();
        			newStock.setName(stock.getSku()+"_"+stock.getWarehouseNumber());
        			newStock.setWarehouseNumber(stock.getWarehouseNumber());
        			newStock.setSku(stock.getSku());
        			newStock.setStockCount(stock.getStockCount());
        			newStock.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        			newStock.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
        			
        			logger.info("Stock will be created for warehouse {}, SKU {} with count"
        					+ "{}",stock.getWarehouseNumber(),
        					stock.getSku(), stock.getStockCount());
        			
        			stocksToBeCreated.add(newStock);
        			
        		}
    			
    			
    			
    		}
    		
    	}
    	
    	int stockListToBePushedCount = listOfStocks.size();
		int stockListToBeUpdatedCount = stocksToBeUpdated.size();
		int stockListToBeCreatedCount = stocksToBeCreated.size();
		int stockListRejectedCount= rejectedStocksList.size();
		
		stockRepository.saveAll(stocksToBeUpdated);
		stockRepository.saveAll(stocksToBeCreated);
		
		listOfStockDTO.setResponseMessage("Stock list updation has been "
				+ "completed with total input count "+stockListToBePushedCount+", "
						+ "updating existing stocks count "+stockListToBeUpdatedCount+
						", creating new stock entries count "+stockListToBeCreatedCount
						+", and rejecting entries count "+stockListRejectedCount+""
								+ "rejected stock list is attached");
		listOfStockDTO.setResponseStatus("200");
		listOfStockDTO.setListOfStocks(rejectedStocksList);
		
		logger.info("Stock list updation has been "
				+ "completed with total input count "+stockListToBePushedCount+", "
						+ "updating existing stocks count "+stockListToBeUpdatedCount+
						", creating new stock entries count "+stockListToBeCreatedCount
						+", and rejecting entries count "+stockListRejectedCount);
		
		return listOfStockDTO;
    	
    }

    public ListOfStockDTO getStocksByWarehouseNumbersAndSKUs(FetchStockInBulkRequestDTO requestDTO) throws JsonProcessingException {
        
        logger.info("Fetching stocks by warehouse numbers and SKUs: {}", requestDTO);
        
        List<String> listOfSku = requestDTO.getListOfSKUs();
        List<String> listOfWarehouses = requestDTO.getListOfWarehouseNumbers();
        List<StockDTO> stockDTOList = new ArrayList<StockDTO>();
        
        List<Stock> listOfStocks = stockRepository.
        		findAllStocksBySkusAndWarehouseNumbers(listOfSku, listOfWarehouses);
        
        ListOfStockDTO listOfStockDTO = new ListOfStockDTO();
        
        for(Stock stock: listOfStocks) {
        	
        	stockDTOList.add(new StockDTO(stock.getId(),stock.getName(),
        			stock.getWarehouseNumber(),stock.getSku(),stock.getStockCount(),
        			null,null,null,null));
        	
        }
        
        listOfStockDTO.setListOfStocks(stockDTOList);
        listOfStockDTO.setResponseMessage(Constants.STOCK_FETCHED_SUCCESSFULLY);
        listOfStockDTO.setResponseStatus("200");
        
        logger.info("Stock fetched successfully "+objectMapper.writeValueAsString(stockDTOList));
        
        return listOfStockDTO;
    }

    public ListOfStockDTO getStocksBySKUs(ListOfStringDTO listOfSKUs) throws JsonProcessingException {
    	
    	logger.info("Fetching stocks by SKUs: {}", listOfSKUs);
        
    	List<String> skuList = listOfSKUs.getListOfStrings();
    	List<Stock> stockList = stockRepository.findAllStocksBySkus(skuList);

    	List<StockDTO> stockDTOList = new ArrayList<StockDTO>();
    	ListOfStockDTO listOfStockDTO = new ListOfStockDTO();

    	
    	for(Stock stock: stockList) {

    			stockDTOList.add(new StockDTO(null,null,
        			stock.getWarehouseNumber(),stock.getSku(),stock.getStockCount(),
        			null,null,null,null));

    	}
    	
    	listOfStockDTO.setListOfStocks(stockDTOList);
    	listOfStockDTO.setResponseMessage(Constants.STOCK_FETCHED_SUCCESSFULLY);
    	listOfStockDTO.setResponseStatus("200");
    	
    	logger.info("Stock list fetched successfully with "+objectMapper.writeValueAsString(listOfStockDTO));
        
        return listOfStockDTO;
    }

    public ListOfStockDTO getStocksBySKUsAndExcludingGivenWarehouseNumbers(FetchStockInBulkRequestDTO requestDTO) throws JsonProcessingException {
        
    	
    	logger.info("Fetching stocks by excluding given warehouse numbers and SKUs: {}", requestDTO);
        
        List<String> listOfSku = requestDTO.getListOfSKUs();
        List<String> listOfWarehousesToBeExcluded = requestDTO.getListOfWarehouseNumbers();
        List<StockDTO> stockDTOList = new ArrayList<StockDTO>();
        
        List<Stock> listOfStocks = stockRepository.
        		findAllStocksBySkusAndExcludingWarehouseNumbers(listOfSku, listOfWarehousesToBeExcluded);
        
        ListOfStockDTO listOfStockDTO = new ListOfStockDTO();
        
        for(Stock stock: listOfStocks) {
        	
        	stockDTOList.add(new StockDTO(stock.getId(),stock.getName(),
        			stock.getWarehouseNumber(),stock.getSku(),stock.getStockCount(),
        			null,null,null,null));
        	
        }
        
        listOfStockDTO.setListOfStocks(stockDTOList);
        listOfStockDTO.setResponseMessage(Constants.STOCK_FETCHED_SUCCESSFULLY);
        listOfStockDTO.setResponseStatus("200");
        
        logger.info("Stock fetched successfully "+objectMapper.writeValueAsString(stockDTOList));
        
        return listOfStockDTO;
    	
    }

}
