package com.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.dto.ListOfProductDTO;
import com.productservice.dto.ProductDTO;
import com.productservice.dto.ListOfStringDTO;
import com.productservice.service.ProductService;
import com.productservice.util.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/product/createProduct")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) throws JsonProcessingException {
        logger.info("Creating new product with payload: ", objectMapper.writeValueAsString(productDTO));

        ProductDTO product = productService.createProduct(productDTO);

        return utility.addResponse(product, "POST");
    }


    @PutMapping("/product/updateProduct")
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) throws JsonProcessingException {
        logger.info("Updating product with payload: ", objectMapper.writeValueAsString(productDTO));

        ProductDTO product = productService.updateProduct(productDTO);

        return utility.addResponse(product, "PUT");
    }


    @GetMapping("/product/getAllProducts")
    public ListOfProductDTO getAllProducts() {
        logger.info("Fetching all products.");
        
        ListOfProductDTO productList = productService.getAllProducts();

        return utility.addResponse(productList, "GET");
        
    }


    @GetMapping("/product/getProductBySKU")
    public ProductDTO getProductBySKU(@RequestParam("sku") String sku) {
        logger.info("Fetching product by SKU: {}", sku);
        
        ProductDTO product = productService.getProductBySKU(sku);

        return utility.addResponse(product, "GET");
       
    }


    @GetMapping("/product/getProductsBySKUs")
    public ListOfProductDTO getProductsBySKUs(@RequestBody ListOfStringDTO skuList) throws JsonProcessingException {
        logger.info("Fetching products by SKUs: {}", objectMapper.writeValueAsString(skuList));
        
        ListOfProductDTO productList = productService.getProductsBySKUs(skuList.getListOfStrings());
        
        return utility.addResponse(productList, "GET");
    }
    
    @GetMapping("/auth/product/test")
    public String test() {
    	
    	String output = productService.test();
    	
    	return output;
    	
    }
    
    @GetMapping("/product/test")
    public String testWithoutAuth() {
    	
    	String output = productService.authTest();
    	
    	return output;
    	
    }
}
