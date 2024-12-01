package com.productservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productservice.dto.CategoryDTO;
import com.productservice.dto.ListOfCategoryDTO;
import com.productservice.service.CategoryService;
import com.productservice.util.Utility;

@RestController
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private Utility utility;


    @PostMapping("/product/createCategory")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) throws JsonProcessingException {
        logger.info("Creating new category with payload: ", objectMapper.writeValueAsString(categoryDTO));

        CategoryDTO category = categoryService.createCategory(categoryDTO);

        return utility.addResponse(category, "POST");
    }


    @PutMapping("/product/updateCategory")
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO) throws JsonProcessingException {
        logger.info("Updating category with payload: ", objectMapper.writeValueAsString(categoryDTO));

        CategoryDTO category = categoryService.updateCategory(categoryDTO);

        return utility.addResponse(category, "PUT");
    }


    @GetMapping("/product/getAllCategories")
    public ListOfCategoryDTO getAllCategories() {
        logger.info("Fetching all categories.");
        
        ListOfCategoryDTO listOfcategories = categoryService.getAllCategories();

        return utility.addResponse(listOfcategories, "GET");
        
    }


    @GetMapping("/product/getCategoryByName")
    public CategoryDTO getCategoryById(@RequestParam("name") String name) {
        logger.info("Fetching category by name: {}", name);
        
        CategoryDTO category = categoryService.getCategoryById(name);

        return utility.addResponse(category, "GET");
        
    }
}
