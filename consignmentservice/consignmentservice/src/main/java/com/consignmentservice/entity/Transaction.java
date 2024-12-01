package com.consignmentservice.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="transactions")
public class Transaction implements Serializable {

    @Id
    @GenericGenerator(name = "transaction-key-generator", strategy = "com.consignmentservice.keygenerator.TransactionKeyGenerator")
    @GeneratedValue(generator = "transaction-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="consignment_number")
    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @Column(name="order_number")
    @JsonProperty("orderNumber")
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="previous_status")
    @JsonProperty("previousStatus")
    private ConsignmentStatusEnum previousStatus;

    @Column(name="logistics_code_name")
    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @Column(name="logistics_tracking_id")
    @JsonProperty("logisticsTrackingId")
    private String logisticsTrackingId;

    @Enumerated(EnumType.STRING)
    @Column(name="present_status")
    @JsonProperty("presentStatus")
    private ConsignmentStatusEnum presentStatus;

    @Column(name="transaction_date_time")
    @JsonProperty("transactionDateTime")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime transactionDateTime;
    
    @Column(name="status_log")
    @JsonProperty("statusLog")
    private String statusLog;
    
    @Enumerated(EnumType.STRING)
    @Column(name="fulfillment_type")
    @JsonProperty("fulfillmentType")
    private ConsignmentFulfillmentTypeEnum fulfillmentType;
}
