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
@Table(name="order_items")
public class OrderItem implements Serializable {

    @Id
    @GenericGenerator(name = "order-item-key-generator", strategy = "com.orderservice.keygenerator.OrderItemKeyGenerator")
    @GeneratedValue(generator = "order-item-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="order_item_number")
    @JsonProperty("orderItemNumber")
    private String orderItemNumber;

    @Column(name="product_id")
    @JsonProperty("productId")
    private String productId;

    @Column(name="sku")
    @JsonProperty("sku")
    private String sku;

    @Column(name="unit_price")
    @JsonProperty("unitPrice")
    private Double unitPrice;

    @Column(name="sales_price")
    @JsonProperty("salesPrice")
    private Double salesPrice;

    @Column(name="discount")
    @JsonProperty("discount")
    private Double discount;

    @Column(name="shipping_fee")
    @JsonProperty("shippingFee")
    private Double shippingFee;

    @Column(name="tax")
    @JsonProperty("tax")
    private Double tax;

    @Column(name="quantity")
    @JsonProperty("quantity")
    private Long quantity;

    @Column(name="total_price")
    @JsonProperty("totalPrice")
    private Double totalPrice;

    @Column(name="total_sales_price")
    @JsonProperty("totalSalesPrice")
    private Double totalSalesPrice;

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

    @Column(name="consignment_item_number")
    @JsonProperty("consignmentItemNumber")
    private String consignmentItemNumber;

    @Column(name="consignment_number")
    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @Column(name="order_number")
    @JsonProperty("orderNumber")
    private String orderNumber;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
