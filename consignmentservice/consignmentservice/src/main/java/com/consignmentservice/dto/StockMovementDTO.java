package com.consignmentservice.dto;

import com.consignmentservice.entity.StockMovementReasonEnum;
import com.consignmentservice.entity.StockMovementStatus;
import com.consignmentservice.entity.StockMovementTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class StockMovementDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @JsonProperty("skuAndStock")
    private String skuAndStock;

    @JsonProperty("transactionDate")
    private String transactionDate;

    @JsonProperty("completionDate")
    private String completionDate;

    @JsonProperty("receivedByEmployeeNumber")
    private String receivedByEmployeeNumber;

    @JsonProperty("movementType")
    private StockMovementTypeEnum movementType;  

    @JsonProperty("reason")
    private StockMovementReasonEnum reason;     

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @JsonProperty("logisticsDelivererName")
    private String logisticsDelivererName;

    @JsonProperty("logisticsDelivererNumber")
    private String logisticsDelivererNumber;

    @JsonProperty("isConsumed")
    private Boolean isConsumed;
    
    @JsonProperty("status")
    private StockMovementStatus status;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}

