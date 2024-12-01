package com.consignmentservice.dto;

import com.consignmentservice.entity.ConsignmentFulfillmentTypeEnum;
import com.consignmentservice.entity.ConsignmentStatusEnum;
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
public class TransactionDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("previousStatus")
    private ConsignmentStatusEnum previousStatus;

    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @JsonProperty("logisticsTrackingId")
    private String logisticsTrackingId;

    @JsonProperty("presentStatus")
    private ConsignmentStatusEnum presentStatus;

    @JsonProperty("transactionDateTime")
    private String transactionDateTime;
    
    @JsonProperty("statusLog")
    private String statusLog;
    
    @JsonProperty("fulfillmentType")
    private ConsignmentFulfillmentTypeEnum fulfillmentType;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
