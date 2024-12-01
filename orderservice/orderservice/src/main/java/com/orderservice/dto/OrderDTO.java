package com.orderservice.dto;

import java.util.List;

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
public class OrderDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("grandTotal")
    private Double grandTotal;

    @JsonProperty("tax")
    private Double tax;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("shippingFee")
    private Double shippingFee;

    @JsonProperty("payableTotal")
    private Double payableTotal;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("billingAddress")
    private String billingAddress;

    @JsonProperty("shippingAddress")
    private String shippingAddress;

    @JsonProperty("shippingPincode")
    private String shippingPincode;

    @JsonProperty("customerEmail")
    private String customerEmail;

    @JsonProperty("customerPhone")
    private String customerPhone;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("status")
    private OrderStatusEnum status;

    @JsonProperty("statusLog")
    private String statusLog;

    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @JsonProperty("logisticsTrackingId")
    private String logisticsTrackingId;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("deliveryDate")
    private String deliveryDate;

    @JsonProperty("pickupDate")
    private String pickupDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
    
    @JsonProperty("orderItems")
    private List<OrderItemDTO> orderItems;
    
    @JsonProperty("isEmailTriggerNeeded")
    private Boolean isEmailTriggerNeeded;
    
    @JsonProperty("isConsumed")
    private Boolean isConsumed;
}
