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
@Table(name="consignment_items")
public class ConsignmentItem implements Serializable {

    @Id
    @GenericGenerator(name = "consignment-item-key-generator", strategy = "com.consignmentservice.keygenerator.ConsignmentItemKeyGenerator")
    @GeneratedValue(generator = "consignment-item-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="consignment_item_number")
    @JsonProperty("consignmentItemNumber")
    private String consignmentItemNumber;

    @Column(name="order_item_number")
    @JsonProperty("orderItemNumber")
    private String orderItemNumber;

    @Column(name="consignment_number")
    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @Column(name="order_number")
    @JsonProperty("orderNumber")
    private String orderNumber;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    @JsonProperty("status")
    private ConsignmentStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name="fulfillment_type")
    @JsonProperty("fulfillmentType")
    private ConsignmentFulfillmentTypeEnum fulfillmentType;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
    
    @Column(name="sku")
    @JsonProperty("sku")
    private String sku;

    @Column(name="quantity")
    @JsonProperty("quantity")
    private Long quantity;
}
