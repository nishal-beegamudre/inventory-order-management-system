package com.orderservice.entity;

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
@Table(name="orders")
public class Order implements Serializable {

    @Id
    @GenericGenerator(name = "order-key-generator", strategy = "com.orderservice.keygenerator.OrderKeyGenerator")
    @GeneratedValue(generator = "order-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="order_number")
    @JsonProperty("orderNumber")
    private String orderNumber;

    @Column(name="grand_total")
    @JsonProperty("grandTotal")
    private Double grandTotal;

    @Column(name="tax")
    @JsonProperty("tax")
    private Double tax;

    @Column(name="discount")
    @JsonProperty("discount")
    private Double discount;

    @Column(name="shipping_fee")
    @JsonProperty("shippingFee")
    private Double shippingFee;

    @Column(name="payable_total")
    @JsonProperty("payableTotal")
    private Double payableTotal;

    @Column(name="customer_id")
    @JsonProperty("customerId")
    private String customerId;

    @Column(name="billing_address")
    @JsonProperty("billingAddress")
    private String billingAddress;

    @Column(name="shipping_address")
    @JsonProperty("shippingAddress")
    private String shippingAddress;

    @Column(name="shipping_pincode")
    @JsonProperty("shippingPincode")
    private String shippingPincode;

    @Column(name="customer_email")
    @JsonProperty("customerEmail")
    private String customerEmail;

    @Column(name="customer_phone")
    @JsonProperty("customerPhone")
    private String customerPhone;

    @Column(name="customer_name")
    @JsonProperty("customerName")
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    @JsonProperty("status")
    private OrderStatusEnum status;

    @Column(name="status_log")
    @JsonProperty("statusLog")
    private String statusLog;

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

    @Column(name="delivery_date")
    @JsonProperty("deliveryDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime deliveryDate;

    @Column(name="pickup_date")
    @JsonProperty("pickupDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime pickupDate;
    
    @Column(name="is_consumed")
    @JsonProperty("isConsumed")
    private Boolean isConsumed;
}
