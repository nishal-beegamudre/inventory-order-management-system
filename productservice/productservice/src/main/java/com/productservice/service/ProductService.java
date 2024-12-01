package com.productservice.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.dto.ListOfProductDTO;
import com.productservice.dto.ProductDTO;
import com.productservice.entity.Product;
import com.productservice.repository.ProductRepository;
import com.productservice.util.Constants;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @CachePut(value="productData", key="#productDTO.sku")
    public ProductDTO createProduct(ProductDTO productDTO) {
        
    	
        Product existingProduct = productRepository.findBySku(productDTO.getSku()).orElse(null);

        if (existingProduct == null) {
            
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setSku(productDTO.getSku());
            product.setPrice(productDTO.getPrice());
            product.setIsActive(productDTO.getIsActive());
            product.setDescription(productDTO.getDescription());
            product.setCategoryId(productDTO.getCategoryId());
            product.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
            product.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            
            productRepository.save(product);

            
            productDTO.setResponseStatus("200");

            logger.info("Product created successfully with SKU: {}", productDTO.getSku());

        } else {
            
            logger.info("Product already exists with SKU: {}", productDTO.getSku());

            productDTO.setResponseStatus("404");
        }

        return productDTO;
    	
    }

    @CachePut(value="productData", key="#productDTO.sku")
    public ProductDTO updateProduct(ProductDTO productDTO) {
        
    	
        Product existingProduct = productRepository.findBySku(productDTO.getSku()).orElse(null);

        if (existingProduct != null) {
        	
            existingProduct.setName(productDTO.getName() != null ? productDTO.getName() : existingProduct.getName());
            existingProduct.setPrice(productDTO.getPrice() != null ? productDTO.getPrice() : existingProduct.getPrice());
            existingProduct.setIsActive(productDTO.getIsActive() != null ? productDTO.getIsActive() : existingProduct.getIsActive());
            existingProduct.setDescription(productDTO.getDescription() != null ? productDTO.getDescription() : existingProduct.getDescription());
            existingProduct.setCategoryId(productDTO.getCategoryId() != null ? productDTO.getCategoryId() : existingProduct.getCategoryId());

            existingProduct.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            productRepository.save(existingProduct);

            productDTO.setResponseStatus("200");
            logger.info("Product updated successfully for SKU: {}", productDTO.getSku());

        } else {
        	
            logger.info("Product not found for SKU: {}", productDTO.getSku());
            productDTO.setResponseStatus("404");
        }

        return productDTO;
    	
    	
    }

    public ListOfProductDTO getAllProducts() {
        
    	ListOfProductDTO listOfProducts = new ListOfProductDTO();
    	List<ProductDTO> listOfProductDTO = new ArrayList<ProductDTO>();
    	List<Product> products = productRepository.findAll();
    	
    	if(!products.isEmpty()) {
    	
    	for(Product product: products) {
    		ProductDTO productDTO = new ProductDTO();
    		productDTO.setId(product.getId());
    		productDTO.setName(product.getName());
    		productDTO.setSku(product.getSku());
    		productDTO.setPrice(product.getPrice());
    		productDTO.setIsActive(product.getIsActive());
    		productDTO.setDescription(product.getDescription());
    		productDTO.setCategoryId(product.getCategoryId());
    		productDTO.setCreationDate(product.getCreationDate().toString());
    		productDTO.setLastModifiedDate(product.getLastModifiedDate().toString());
    		listOfProductDTO.add(productDTO);
    	}
    	
    	listOfProducts.setListOfProducts(listOfProductDTO);
    	listOfProducts.setResponseStatus("200");
    	
    	}else {
    		
    		listOfProducts.setResponseStatus("404");
    		
    	}
     
        return listOfProducts;
    	
    	
    }

    @Cacheable(value="productData", key="#sku")
    public ProductDTO getProductBySKU(String sku) {
        
    	Product product = productRepository.findBySku(sku).orElse(null);
    	ProductDTO productDTO = new ProductDTO();
    	
    	if(product!=null) {
    		
    		productDTO.setId(product.getId());
    		productDTO.setName(product.getName());
    		productDTO.setSku(product.getSku());
    		productDTO.setPrice(product.getPrice());
    		productDTO.setIsActive(product.getIsActive());
    		productDTO.setDescription(product.getDescription());
    		productDTO.setCategoryId(product.getCategoryId());
    		productDTO.setCreationDate(product.getCreationDate().toString());
    		productDTO.setLastModifiedDate(product.getLastModifiedDate().toString());
    		
    		productDTO.setResponseStatus("200");
            logger.info("Product fetched successfully for SKU: {}", productDTO.getSku());

        } else {
        	
            logger.info("Product not found for SKU: {}", productDTO.getSku());
            productDTO.setResponseStatus("404");
        }
    	 	
        return productDTO;
    }

    @Cacheable(value="productDataList", key="#listOfStrings")
    public ListOfProductDTO getProductsBySKUs(List<String> listOfStrings) {

    	ListOfProductDTO listOfProducts = new ListOfProductDTO();
    	List<ProductDTO> listOfProductDTO = new ArrayList<ProductDTO>();
    	List<Product> products = productRepository.findBySkus(listOfStrings);
    	
    	if(!products.isEmpty()) {
    	
    	for(Product product: products) {
    		ProductDTO productDTO = new ProductDTO();
    		productDTO.setId(product.getId());
    		productDTO.setName(product.getName());
    		productDTO.setSku(product.getSku());
    		productDTO.setPrice(product.getPrice());
    		productDTO.setIsActive(product.getIsActive());
    		productDTO.setDescription(product.getDescription());
    		productDTO.setCategoryId(product.getCategoryId());
    		productDTO.setCreationDate(product.getCreationDate().toString());
    		productDTO.setLastModifiedDate(product.getLastModifiedDate().toString());
    		listOfProductDTO.add(productDTO);
    	}
    	
    	listOfProducts.setListOfProducts(listOfProductDTO);
    	listOfProducts.setResponseStatus("200");
    	
    	}else {
    		
    		listOfProducts.setResponseStatus("404");
    		
    	}
     
        return listOfProducts;
    }

	public String test() {
		
		logger.info("Test");
		
		Integer i = 0;
		i++;
		
		return "Test is passed with i "+i.toString();
	}
	
	public String authTest() {
		
		logger.info("Auth Test");
		
		Integer i = 2;
		i++;
		i++;
		
		return "Auth Test is passed with i "+i.toString();
	}
}
