package com.productservice.util;

import org.springframework.stereotype.Service;

import com.productservice.dto.CategoryDTO;
import com.productservice.dto.ListOfCategoryDTO;
import com.productservice.dto.ListOfProductDTO;
import com.productservice.dto.ListOfStringDTO;
import com.productservice.dto.ProductDTO;

@Service
public class Utility {
	
public ProductDTO addResponse(ProductDTO product, String requestType) {
		
		switch(requestType) {
		
		case "GET" : {
			
			switch(product.getResponseStatus()) {
			
			case "200": product.setResponseMessage(Constants.PRODUCT_FETCHED_SUCCESSFULLY);break;
			case "404": product.setResponseMessage(Constants.PRODUCT_NOT_FOUND);break;
			default: product.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		
		}
			
			
		};break;
		
		case "POST" : {
			
			switch(product.getResponseStatus()) {
			
			case "200": product.setResponseMessage(Constants.PRODUCT_CREATED_SUCCESSFULLY);
			case "404": product.setResponseMessage(Constants.PRODUCT_ALREADY_FOUND);
			default: product.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		
		}
			
			
		};break;
		
		case "PUT" : {
			
			switch(product.getResponseStatus()) {
			
			case "200": product.setResponseMessage(Constants.PRODUCT_UPDATED_SUCCESSFULLY);
			case "404": product.setResponseMessage(Constants.PRODUCT_NOT_FOUND);
			default: product.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		
		}
			
			
		};break;
		
		default : {
			
			product.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
		}
		
		}
		
		return product;
	}
	
	
	public CategoryDTO addResponse(CategoryDTO category, String requestType) {
	    
	    switch(requestType) {
	    
	    case "GET" : {
	        switch(category.getResponseStatus()) {
	        
	        case "200": category.setResponseMessage(Constants.CATEGORY_FETCHED_SUCCESSFULLY);break;
	        case "404": category.setResponseMessage(Constants.CATEGORY_NOT_FOUND);break;
	        default: category.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    }; break;
	    
	    case "POST" : {
	        switch(category.getResponseStatus()) {
	        
	        case "200": category.setResponseMessage(Constants.CATEGORY_CREATED_SUCCESSFULLY);break;
	        case "404": category.setResponseMessage(Constants.CATEGORY_ALREADY_FOUND);break;
	        default: category.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    case "PUT" : {
	        switch(category.getResponseStatus()) {
	        
	        case "200": category.setResponseMessage(Constants.CATEGORY_UPDATED_SUCCESSFULLY);break;
	        case "404": category.setResponseMessage(Constants.CATEGORY_NOT_FOUND);break;
	        default: category.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	        }
	    };break;
	    
	    default : {
	    	category.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
	    }
	    
	    }
	    
	    return category;
	}


	public ListOfCategoryDTO addResponse(ListOfCategoryDTO listOfcategories, String requestType) {
		
		switch(listOfcategories.getResponseStatus()) {
        
        case "200": listOfcategories.setResponseMessage(Constants.CATEGORY_FETCHED_SUCCESSFULLY);break;
        case "404": listOfcategories.setResponseMessage(Constants.CATEGORY_NOT_FOUND);break;
        default: listOfcategories.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
        }
		
		return listOfcategories;
		
	}
	
	
	public ListOfProductDTO addResponse(ListOfProductDTO listOfProducts, String requestType) {
		
		switch(listOfProducts.getResponseStatus()) {
        
        case "200": listOfProducts.setResponseMessage(Constants.PRODUCT_FETCHED_SUCCESSFULLY);break;
        case "404": listOfProducts.setResponseMessage(Constants.PRODUCT_NOT_FOUND);break;
        default: listOfProducts.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
        }
		
		return listOfProducts;
		
	}
	
	public ListOfStringDTO addResponse(ListOfStringDTO listOfStrings, String requestType) {
		
		switch(listOfStrings.getResponseStatus()) {
        
        case "200": listOfStrings.setResponseMessage(Constants.DATA_FETCHED_SUCCESSFULLY);break;
        case "404": listOfStrings.setResponseMessage(Constants.DATA_NOT_FOUND);break;
        default: listOfStrings.setResponseMessage(Constants.UNKNOWN_EXCEPTION_OCCURRED);
        }
		
		return listOfStrings;
		
	}

}
