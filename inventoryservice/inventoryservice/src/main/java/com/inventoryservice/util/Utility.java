package com.inventoryservice.util;

import org.springframework.stereotype.Service;

import com.inventoryservice.dto.ListOfListOfStockDTO;
import com.inventoryservice.dto.ListOfPincodeDTO;
import com.inventoryservice.dto.ListOfStateDTO;
import com.inventoryservice.dto.ListOfStockDTO;
import com.inventoryservice.dto.LogisticsDTO;
import com.inventoryservice.dto.PincodeDTO;
import com.inventoryservice.dto.StateDTO;
import com.inventoryservice.dto.StockDTO;
import com.inventoryservice.dto.StockMovementDTO;
import com.inventoryservice.dto.WarehouseDTO;

@Service
public class Utility {

	public LogisticsDTO addResponse(LogisticsDTO logistics, String requestType) {
	    switch (requestType) {
	        case "GET":
	            switch (logistics.getResponseStatus()) {
	                case "200": logistics.setResponseMessage(Constants.LOGISTICS_FETCHED_SUCCESSFULLY); break;
	                case "404": logistics.setResponseMessage(Constants.LOGISTICS_NOT_FOUND); break;
	                default: logistics.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "POST":
	            switch (logistics.getResponseStatus()) {
	                case "200": logistics.setResponseMessage(Constants.LOGISTICS_CREATED_SUCCESSFULLY); break;
	                case "404": logistics.setResponseMessage(Constants.LOGISTICS_ALREADY_FOUND); break;
	                default: logistics.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "PUT":
	            switch (logistics.getResponseStatus()) {
	                case "200": logistics.setResponseMessage(Constants.LOGISTICS_UPDATED_SUCCESSFULLY); break;
	                case "404": logistics.setResponseMessage(Constants.LOGISTICS_NOT_FOUND); break;
	                default: logistics.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        default: logistics.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }

	    return logistics;
	}

	public PincodeDTO addResponse(PincodeDTO pincode, String requestType) {
	    switch (requestType) {
	        case "GET":
	            switch (pincode.getResponseStatus()) {
	                case "200": pincode.setResponseMessage(Constants.PINCODE_FETCHED_SUCCESSFULLY); break;
	                case "404": pincode.setResponseMessage(Constants.PINCODE_NOT_FOUND); break;
	                default: pincode.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "POST":
	            switch (pincode.getResponseStatus()) {
	                case "200": pincode.setResponseMessage(Constants.PINCODE_CREATED_SUCCESSFULLY); break;
	                case "404": pincode.setResponseMessage(Constants.PINCODE_ALREADY_FOUND); break;
	                default: pincode.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "PUT":
	            switch (pincode.getResponseStatus()) {
	                case "200": pincode.setResponseMessage(Constants.PINCODE_UPDATED_SUCCESSFULLY); break;
	                case "404": pincode.setResponseMessage(Constants.PINCODE_NOT_FOUND); break;
	                default: pincode.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        default: pincode.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }

	    return pincode;
	}

	public StateDTO addResponse(StateDTO state, String requestType) {
	    switch (requestType) {
	        case "GET":
	            switch (state.getResponseStatus()) {
	                case "200": state.setResponseMessage(Constants.STATE_FETCHED_SUCCESSFULLY); break;
	                case "404": state.setResponseMessage(Constants.STATE_NOT_FOUND); break;
	                default: state.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "POST":
	            switch (state.getResponseStatus()) {
	                case "200": state.setResponseMessage(Constants.STATE_CREATED_SUCCESSFULLY); break;
	                case "404": state.setResponseMessage(Constants.STATE_ALREADY_FOUND); break;
	                default: state.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "PUT":
	            switch (state.getResponseStatus()) {
	                case "200": state.setResponseMessage(Constants.STATE_UPDATED_SUCCESSFULLY); break;
	                case "404": state.setResponseMessage(Constants.STATE_NOT_FOUND); break;
	                default: state.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        default: state.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }

	    return state;
	}

	public StockDTO addResponse(StockDTO stock, String requestType) {
	    switch (requestType) {
	        case "GET":
	            switch (stock.getResponseStatus()) {
	                case "200": stock.setResponseMessage(Constants.STOCK_FETCHED_SUCCESSFULLY); break;
	                case "404": stock.setResponseMessage(Constants.STOCK_NOT_FOUND); break;
	                default: stock.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "POST":
	            switch (stock.getResponseStatus()) {
	                case "200": stock.setResponseMessage(Constants.STOCK_CREATED_SUCCESSFULLY); break;
	                case "404": stock.setResponseMessage(Constants.STOCK_ALREADY_FOUND); break;
	                default: stock.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "PUT":
	            switch (stock.getResponseStatus()) {
	                case "200": stock.setResponseMessage(Constants.STOCK_UPDATED_SUCCESSFULLY); break;
	                case "404": stock.setResponseMessage(Constants.STOCK_NOT_FOUND); break;
	                default: stock.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        default: stock.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }

	    return stock;
	}

	public WarehouseDTO addResponse(WarehouseDTO warehouse, String requestType) {
	    switch (requestType) {
	        case "GET":
	            switch (warehouse.getResponseStatus()) {
	                case "200": warehouse.setResponseMessage(Constants.WAREHOUSE_FETCHED_SUCCESSFULLY); break;
	                case "404": warehouse.setResponseMessage(Constants.WAREHOUSE_NOT_FOUND); break;
	                default: warehouse.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "POST":
	            switch (warehouse.getResponseStatus()) {
	                case "200": warehouse.setResponseMessage(Constants.WAREHOUSE_CREATED_SUCCESSFULLY); break;
	                case "404": warehouse.setResponseMessage(Constants.WAREHOUSE_ALREADY_FOUND); break;
	                default: warehouse.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "PUT":
	            switch (warehouse.getResponseStatus()) {
	                case "200": warehouse.setResponseMessage(Constants.WAREHOUSE_UPDATED_SUCCESSFULLY); break;
	                case "404": warehouse.setResponseMessage(Constants.WAREHOUSE_NOT_FOUND); break;
	                default: warehouse.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        default: warehouse.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }

	    return warehouse;
	}

	public StockMovementDTO addResponse(StockMovementDTO stockMovement, String requestType) {
	    switch (requestType) {
	        case "GET":
	            switch (stockMovement.getResponseStatus()) {
	                case "200": stockMovement.setResponseMessage(Constants.STOCK_MOVEMENT_FETCHED_SUCCESSFULLY); break;
	                case "404": stockMovement.setResponseMessage(Constants.STOCK_MOVEMENT_NOT_FOUND); break;
	                default: stockMovement.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "POST":
	            switch (stockMovement.getResponseStatus()) {
	                case "200": stockMovement.setResponseMessage(Constants.STOCK_MOVEMENT_CREATED_SUCCESSFULLY); break;
	                case "404": stockMovement.setResponseMessage(Constants.STOCK_MOVEMENT_ALREADY_FOUND); break;
	                default: stockMovement.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        case "PUT":
	            switch (stockMovement.getResponseStatus()) {
	                case "200": stockMovement.setResponseMessage(Constants.STOCK_MOVEMENT_UPDATED_SUCCESSFULLY); break;
	                case "404": stockMovement.setResponseMessage(Constants.STOCK_MOVEMENT_NOT_FOUND); break;
	                default: stockMovement.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	            }
	            break;

	        default: stockMovement.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }

	    return stockMovement;
	}

	public ListOfPincodeDTO addResponse(ListOfPincodeDTO pincodeList, String requestType) {
		
		switch (pincodeList.getResponseStatus()) {
        case "200": pincodeList.setResponseMessage(Constants.PINCODE_UPDATED_SUCCESSFULLY); break;
        case "404": pincodeList.setResponseMessage(Constants.PINCODE_NOT_FOUND); break;
        default: pincodeList.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
        
        
    }
		
		return pincodeList;
		
	}

	public ListOfStateDTO addResponse(ListOfStateDTO stateList, String requestType) {
		
		switch (stateList.getResponseStatus()) {
        case "200": stateList.setResponseMessage(Constants.STATE_CREATED_SUCCESSFULLY); break;
        case "404": stateList.setResponseMessage(Constants.STATE_ALREADY_FOUND); break;
        default: stateList.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
    }
		
		return stateList;
		
	}

	public ListOfStockDTO addResponse(ListOfStockDTO stockList, String requestType) {
		
		switch (requestType) {
        case "GET":
            switch (stockList.getResponseStatus()) {
                case "200": stockList.setResponseMessage(Constants.STOCK_FETCHED_SUCCESSFULLY); break;
                case "404": stockList.setResponseMessage(Constants.STOCK_NOT_FOUND); break;
                default: stockList.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
            }
            break;

        case "POST":
            switch (stockList.getResponseStatus()) {
                case "200": stockList.setResponseMessage(Constants.STOCK_CREATED_SUCCESSFULLY); break;
                case "404": stockList.setResponseMessage(Constants.STOCK_ALREADY_FOUND); break;
                default: stockList.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
            }
            break;

        case "PUT":
            switch (stockList.getResponseStatus()) {
                case "200": stockList.setResponseMessage(Constants.STOCK_UPDATED_SUCCESSFULLY); break;
                case "404": stockList.setResponseMessage(Constants.STOCK_NOT_FOUND); break;
                default: stockList.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
            }
            break;

        default: stockList.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
    }

    return stockList;
		
	}

	public ListOfListOfStockDTO addResponse(ListOfListOfStockDTO listOfListOfStocks, String requestType) {
		
		switch (listOfListOfStocks.getResponseStatus()) {
        case "200": listOfListOfStocks.setResponseMessage(Constants.STOCK_FETCHED_SUCCESSFULLY); break;
        case "404": listOfListOfStocks.setResponseMessage(Constants.STOCK_NOT_FOUND); break;
        default: listOfListOfStocks.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
    }
		
		return listOfListOfStocks;
		
	}


}
