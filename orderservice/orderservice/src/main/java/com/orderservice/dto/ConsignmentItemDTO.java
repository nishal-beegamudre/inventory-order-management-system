package com.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderservice.entity.ConsignmentFulfillmentTypeEnum;
import com.orderservice.entity.ConsignmentStatusEnum;

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
public class ConsignmentItemDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("consignmentItemNumber")
    private String consignmentItemNumber;

    @JsonProperty("orderItemNumber")
    private String orderItemNumber;

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @JsonProperty("status")
    private ConsignmentStatusEnum status;

    @JsonProperty("fulfillmentType")
    private ConsignmentFulfillmentTypeEnum fulfillmentType;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
    
    @JsonProperty("sku")
    private String sku;

    @JsonProperty("quantity")
    private Long quantity;
}
