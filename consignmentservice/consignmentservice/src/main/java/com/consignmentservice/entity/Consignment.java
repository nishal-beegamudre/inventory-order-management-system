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
@Table(name="consignments")
public class Consignment implements Serializable {

    @Id
    @GenericGenerator(name = "consignment-key-generator", strategy = "com.consignmentservice.keygenerator.ConsignmentKeyGenerator")
    @GeneratedValue(generator = "consignment-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="consignment_number")
    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @Column(name="order_items")
    @JsonProperty("orderItems")
    private String orderItems;
    
    @Column(name="order_number")
    @JsonProperty("orderNumber")
    private String orderNumber;

    @Column(name="consignment_items")
    @JsonProperty("consignmentItems")
    private String consignmentItems;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    @JsonProperty("status")
    private ConsignmentStatusEnum status;

    @Column(name="status_log")
    @JsonProperty("statusLog")
    private String statusLog;

    @Column(name="billing_address")
    @JsonProperty("billingAddress")
    private String billingAddress;

    @Column(name="shipping_address")
    @JsonProperty("shippingAddress")
    private String shippingAddress;

    @Column(name="shipping_pincode")
    @JsonProperty("shippingPinCode")
    private String shippingPinCode;

    @Column(name="customer_name")
    @JsonProperty("customerName")
    private String customerName;

    @Column(name="phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name="email")
    @JsonProperty("email")
    private String email;

    @Column(name="logistics_code_name")
    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @Column(name="logistics_tracking_id")
    @JsonProperty("logisticsTrackingId")
    private String logisticsTrackingId;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;

    @Column(name="fulfillment_completion_date_time")
    @JsonProperty("fulfillmentCompletionDateTime")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime fulfillmentCompletionDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name="fulfillment_type")
    @JsonProperty("fulfillmentType")
    private ConsignmentFulfillmentTypeEnum fulfillmentType;

    @Column(name="stock_movement_id")
    @JsonProperty("stockMovementId")
    private String stockMovementId;
}
