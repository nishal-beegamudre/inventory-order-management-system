package com.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notificationservice.entity.EmailTypeEnum;

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
public class EmailLogDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("emailType")
    private EmailTypeEnum emailType;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("emailRecipientDateTime")
    private String emailRecipientDateTime;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
