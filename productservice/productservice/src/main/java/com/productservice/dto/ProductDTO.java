package com.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("description")
    private String description;

    @JsonProperty("categoryId")
    private String categoryId;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;
    
    @JsonProperty("responseStatus")
    private String responseStatus;
    
    @JsonProperty("responseMessage")
    private String responseMessage;
}
