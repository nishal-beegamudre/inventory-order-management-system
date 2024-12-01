package com.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderservice.entity.OrderStatusEnum;

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
public class OrderItemDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("orderItemNumber")
    private String orderItemNumber;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("unitPrice")
    private Double unitPrice;

    @JsonProperty("salesPrice")
    private Double salesPrice;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("shippingFee")
    private Double shippingFee;

    @JsonProperty("tax")
    private Double tax;

    @JsonProperty("quantity")
    private Long quantity;

    @JsonProperty("totalPrice")
    private Double totalPrice;

    @JsonProperty("totalSalesPrice")
    private Double totalSalesPrice;

    @JsonProperty("status")
    private OrderStatusEnum status;
    
    @JsonProperty("statusLog")
    private String statusLog;
    
    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @JsonProperty("logisticsTrackingId")
    private String logisticsTrackingId;

    @JsonProperty("consignmentItemNumber")
    private String consignmentItemNumber;

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
