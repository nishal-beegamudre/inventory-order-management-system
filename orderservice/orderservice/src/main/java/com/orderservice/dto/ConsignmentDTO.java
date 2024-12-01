package com.orderservice.dto;

import java.util.List;

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
public class ConsignmentDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @JsonProperty("orderItems")
    private String orderItems;
    
    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("consignmentItems")
    private List<ConsignmentItemDTO> consignmentItems;

    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @JsonProperty("status")
    private ConsignmentStatusEnum status;

    @JsonProperty("statusLog")
    private String statusLog;

    @JsonProperty("billingAddress")
    private String billingAddress;

    @JsonProperty("shippingAddress")
    private String shippingAddress;

    @JsonProperty("shippingPinCode")
    private String shippingPinCode;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @JsonProperty("logisticsTrackingId")
    private String logisticsTrackingId;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("fulfillmentCompletionDateTime")
    private String fulfillmentCompletionDateTime;

    @JsonProperty("fulfillmentType")
    private ConsignmentFulfillmentTypeEnum fulfillmentType;

    @JsonProperty("stockMovementId")
    private String stockMovementId;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}

