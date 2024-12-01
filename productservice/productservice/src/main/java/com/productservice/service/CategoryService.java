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
import com.productservice.dto.CategoryDTO;
import com.productservice.dto.ListOfCategoryDTO;
import com.productservice.entity.Category;
import com.productservice.repository.CategoryRepository;
import com.productservice.util.Constants;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @CachePut(value="categoryData", key="#categoryDTO.name")
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        
    	
        Category existingCategory = categoryRepository.findByName(categoryDTO.getName()).orElse(null);

        if (existingCategory == null) {
            
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());
            category.setIsActive(categoryDTO.getIsActive());
            category.setCreationDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));
            category.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            
            categoryRepository.save(category);

            
            categoryDTO.setResponseStatus("200");

            logger.info("Category created successfully with name: {}", categoryDTO.getName());

        } else {
            
            logger.info("Category already exists with name: {}", categoryDTO.getName());

            categoryDTO.setResponseStatus("404");
        }

        return categoryDTO;
    	
    	
    }

    @CachePut(value="categoryData", key="#categoryDTO.name")
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        
        Category existingCategory = categoryRepository.findByName(categoryDTO.getName()).orElse(null);

        if (existingCategory != null) {
        	
            existingCategory.setDescription(categoryDTO.getDescription() != null ? categoryDTO.getDescription() : existingCategory.getDescription());
            existingCategory.setIsActive(categoryDTO.getIsActive() != null ? categoryDTO.getIsActive() : existingCategory.getIsActive());

            existingCategory.setLastModifiedDate(ZonedDateTime.now(ZoneId.of(Constants.IST)));

            categoryRepository.save(existingCategory);

            categoryDTO.setResponseStatus("200");
            logger.info("Category updated successfully for name: {}", categoryDTO.getName());

        } else {
        	
            logger.info("Category not found for name: {}", categoryDTO.getName());
            categoryDTO.setResponseStatus("404");
        }

        return categoryDTO;
    	
    	
    }

    public ListOfCategoryDTO getAllCategories() {
        
    	ListOfCategoryDTO listOfCategories = new ListOfCategoryDTO();
    	List<CategoryDTO> listOfCategoryDTO = new ArrayList<CategoryDTO>();
    	List<Category> categories = categoryRepository.findAll();
    	
    	if(!categories.isEmpty()) {
    	
    	for(Category category: categories) {

    		CategoryDTO categoryDTO = new CategoryDTO();

    		categoryDTO.setId(category.getId());
    		categoryDTO.setName(category.getName());
    		categoryDTO.setDescription(category.getDescription());
    		categoryDTO.setIsActive(category.getIsActive());
    		categoryDTO.setCreationDate(category.getCreationDate().toString());
    		categoryDTO.setLastModifiedDate(category.getLastModifiedDate().toString());
    		
    		listOfCategoryDTO.add(categoryDTO);
    	}
    	
    	listOfCategories.setListOfCategories(listOfCategoryDTO);
    	listOfCategories.setResponseStatus("200");
    	
    	}else {
    		
    		listOfCategories.setResponseStatus("404");
    		
    	}
     
        return listOfCategories;
    	
    	
    }

    @Cacheable(value="categoryData", key="#name")
    public CategoryDTO getCategoryById(String name) {
        
    	Category category = categoryRepository.findByName(name).orElse(null);
    	CategoryDTO categoryDTO = new CategoryDTO();
    	
    	if(category!=null) {

    		categoryDTO.setId(category.getId());
    		categoryDTO.setName(category.getName());
    		categoryDTO.setDescription(category.getDescription());
    		categoryDTO.setIsActive(category.getIsActive());
    		categoryDTO.setCreationDate(category.getCreationDate().toString());
    		categoryDTO.setLastModifiedDate(category.getLastModifiedDate().toString());

    		
    		categoryDTO.setResponseStatus("200");
            logger.info("Category fetched successfully for Id: {}", categoryDTO.getId());

        } else {
        	
            logger.info("Category not found for Id: {}", categoryDTO.getId());
            categoryDTO.setResponseStatus("404");
        }
    	 	
        return categoryDTO;
    	
    	
    }
}
