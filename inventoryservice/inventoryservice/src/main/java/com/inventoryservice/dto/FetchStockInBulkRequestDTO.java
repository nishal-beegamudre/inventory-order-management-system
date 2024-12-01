package com.inventoryservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchStockInBulkRequestDTO {
	
	@JsonProperty("listOfWarehouseNumbers")
    private List<String> listOfWarehouseNumbers;
	
	@JsonProperty("listOfSKUs")
    private List<String> listOfSKUs;
	
}